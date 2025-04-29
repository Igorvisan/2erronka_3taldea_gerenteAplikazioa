package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;

public class ErreserbaDbKudeaketa {
    public static ObservableList<Erreserba> getAllErreserbak() {
        String query = "SELECT id, izena, data, pertsonaKop, mahaiZenbakia, kantzelatuta, updateData, updateBy FROM erreserba";
        ObservableList<Erreserba> erreserbenLista = FXCollections.observableArrayList();

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String erreserbaIzena = rs.getString("izena");
                Date erreserbaData = rs.getDate("data");
                int pertsonaKopurua = rs.getInt("pertsonaKop");
                String mahaiZenbakia = rs.getString("mahaiZenbakia");
                boolean kantzelatuta = rs.getBoolean("kantzelatuta");
                Date updateData = rs.getDate("updateData");
                String updateBy = rs.getString("updateBy");

                // Crear un objeto Erreserba con los valores obtenidos
                Erreserba erreserba = new Erreserba(id, erreserbaIzena, erreserbaData, pertsonaKopurua, mahaiZenbakia, kantzelatuta, updateData, updateBy);

                // Añadir a la lista
                erreserbenLista.add(erreserba);
            }

        } catch (SQLException e) {
            System.err.println("Errorea datuak eskuratzerakoan: " + e.getMessage());
        }

        return erreserbenLista;
    }

    public static boolean erreserbaGehitu(Erreserba erreserba) {
        // Consulta para insertar la nueva reserva
        String insertQuery = "INSERT INTO erreserba (izena, data, pertsonaKop, mahaiZenbakia) VALUES (?, ?, ?, ?)";

        // Consulta para verificar si la mesa ya está reservada en esa fecha
        String checkReservaQuery = "SELECT COUNT(*) FROM erreserba WHERE mahaiZenbakia = ? AND data = ?";

        try (Connection conn = DbKonexioa.getKonexioa()) {
            // Paso 1: Verificar que el número de personas no sea 0 o negativo
            if (erreserba.getPertsonaKopurua() <= 0 || erreserba.getPertsonaKopurua() > 8) {
                mezuaPantailaratu("Errorea", "Pertsona kopurua ezin da 0 edo negatiboa izan.", Alert.AlertType.ERROR);
                return false;  // Retornar false si hay un error en la validación
            }

            // Paso 3: Validar el mes y el día de la fecha
            LocalDate fechaReserva = erreserba.getErreserbaDate().toLocalDate();
            int mes = fechaReserva.getMonthValue();
            int dia = fechaReserva.getDayOfMonth();

            // Comprobar que el mes esté entre 1 y 12 y el día entre 1 y 31
            if (mes < 1 || mes > 12) {
                mezuaPantailaratu("Errorea", "Mesak 1 eta 12 artean egon behar du.", Alert.AlertType.ERROR);
                return false;
            }

            if (dia < 1 || dia > 31) {
                mezuaPantailaratu("Errorea", "Eguna 1 eta 31 artean egon behar da.", Alert.AlertType.ERROR);
                return false;
            }

            // Paso 4: Verificar si la mesa ya está reservada en esa fecha
            try (PreparedStatement checkStmt = conn.prepareStatement(checkReservaQuery)) {
                // Cambiar a setString para mahaiZenbakia
                checkStmt.setString(1, erreserba.getMahaiZenbakia());
                checkStmt.setDate(2, erreserba.getErreserbaDate());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        mezuaPantailaratu(
                                "Errorea",
                                "Mahaia jada erreserbatuta dago egun horretan.",
                                Alert.AlertType.ERROR
                        );
                        return false;  // Retornar false si la mesa ya está reservada en esa fecha
                    }
                }
            }

            // Paso 5: Insertar la nueva reserva en la base de datos
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                // Depuración: Verificar el valor de pertsona_kopurua
                System.out.println("Insertando Pertsona kopurua: " + erreserba.getPertsonaKopurua());

                insertStmt.setString(1, erreserba.getErreserbaIzena());
                insertStmt.setDate(2, erreserba.getErreserbaDate());
                insertStmt.setInt(3, erreserba.getPertsonaKopurua());
                // Cambiar a setString para mahaiZenbakia
                insertStmt.setString(4, erreserba.getMahaiZenbakia());

                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    // Obtener el ID autogenerado
                    try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            erreserba.setId(generatedId);

                            // Mostrar mensaje de éxito
                            mezuaPantailaratu(
                                    "Erreserba Gehitu",
                                    "Erreserba zuzen gehitu da. Honako hau da bere ID-a: " + generatedId,
                                    Alert.AlertType.INFORMATION
                            );
                            return true;  // Retornar true si la inserción es exitosa
                        }
                    }
                } else {
                    mezuaPantailaratu("Errorea", "Ez da erreserba gehitu.", Alert.AlertType.ERROR);
                    return false;  // Retornar false si no se insertó ninguna fila
                }
            }
        } catch (SQLException e) {
            // Manejar excepciones de SQL
            mezuaPantailaratu("Errorea", "Errorea erreserba gehitzean: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;  // Retornar false en caso de error SQL
        }

        // Asegurarse de retornar false si no se cumple ninguna condición para insertar
        return false;
    }

    public static void erreserbaEzabatu(int erreserbaId) {
        String query = "DELETE FROM erreserba WHERE id = ?"; // Query para eliminar un trabajador por ID

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, erreserbaId); // Usar el ID del trabajador

            System.out.println("Ejecutando consulta: " + stmt.toString()); // Imprimir la consulta para depuración

            int filasAfectadas = stmt.executeUpdate();
            System.out.println("Filas afectadas: " + filasAfectadas); // Ver cuántas filas se han afectado

            if (filasAfectadas > 0) {
                String izena = "Erreserba Ezabatu";
                String mezuLuzea = "Erreserba arrakastaz ezabatu da.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.INFORMATION);
            } else {
                String izena = "Errorea";
                String mezuLuzea = "Ez da erreserba aurkitu.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            String izena = "Errorea";
            String mezuLuzea = "Errorea erreserba ezabatzean: " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            e.printStackTrace(); // Imprimir el error completo para depuración
        }
    }

    public static boolean editatuErreserba(Erreserba erreserbaEditatua) {
        // Consulta para verificar si la mesa ya está reservada en esa fecha (excluyendo la reserva actual)
        String checkReservaQuery = "SELECT COUNT(*) FROM erreserba WHERE mahaiZenbakia = ? AND data = ? AND id != ?";

        try (Connection conn = DbKonexioa.getKonexioa()) {
            // 1. Verificar que el número de personas no sea 0 o negativo
            if (erreserbaEditatua.getPertsonaKopurua() <= 0 || erreserbaEditatua.getPertsonaKopurua() > 8) {
                mezuaPantailaratu("Errorea", "Pertsona kopurua ezin da 0 edo 8 baino gehiago izan.", Alert.AlertType.ERROR);
                return false;
            }

            // 2. Validar la fecha de la reserva (mes y día)
            LocalDate fechaReserva = erreserbaEditatua.getErreserbaDate().toLocalDate();
            int mes = fechaReserva.getMonthValue();
            int dia = fechaReserva.getDayOfMonth();

            if (mes < 1 || mes > 12) {
                mezuaPantailaratu("Errorea", "Hilabeteak 1 eta 12 artean egon behar du.", Alert.AlertType.ERROR);
                return false;
            }
            if (dia < 1 || dia > 31) {
                mezuaPantailaratu("Errorea", "Egunak 1 eta 31 artean egon behar da.", Alert.AlertType.ERROR);
                return false;
            }

            // 3. Verificar si la mesa ya está reservada para esa fecha (excluyendo la misma reserva)
            try (PreparedStatement checkStmt = conn.prepareStatement(checkReservaQuery)) {
                // Cambiar a setString para mahaiZenbakia
                checkStmt.setString(1, erreserbaEditatua.getMahaiZenbakia());
                checkStmt.setDate(2, erreserbaEditatua.getErreserbaDate());
                checkStmt.setInt(3, erreserbaEditatua.getId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        mezuaPantailaratu(
                                "Errorea",
                                "Mahaia jada erreserbatuta dago egun horretan.",
                                Alert.AlertType.ERROR
                        );
                        return false;
                    }
                }
            }

            // 4. Realizar la actualización de la reserva en la base de datos
            String query = "UPDATE erreserba SET izena = ?, data = ?, pertsonaKop = ?, mahaiZenbakia = ?, updateData = ?, updateBy = ? WHERE id = ?";
            Date currenteDate = new Date();
            SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, erreserbaEditatua.getErreserbaIzena());
                stmt.setDate(2, erreserbaEditatua.getErreserbaDate());
                stmt.setInt(3, erreserbaEditatua.getPertsonaKopurua());
                // Cambiar a setString para mahaiZenbakia
                stmt.setString(4, erreserbaEditatua.getMahaiZenbakia());
                stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                stmt.setString(6, erreserbaEditatua.getUpdatedBy());
                stmt.setInt(7, erreserbaEditatua.getId());

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0; // Retorna true si la actualización fue exitosa
            }
        } catch (SQLException e) {
            System.err.println("Error actualizando los datos de la reserva: " + e.getMessage());
            mezuaPantailaratu("Errorea", "Errorea erreserba editatzean: " + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }
}


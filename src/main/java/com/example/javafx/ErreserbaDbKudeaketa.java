package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;

public class ErreserbaDbKudeaketa {
    public static ObservableList<Erreserba> getAllErreserbak() {
        String query = "SELECT id, erreserba_izena, erreserba_data, pertsona_kopurua, mahaia_id FROM erreserba";
        ObservableList<Erreserba> erreserbenLista = FXCollections.observableArrayList();

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String erreserbaIzena = rs.getString("erreserba_izena");
                Date erreserbaData = rs.getDate("erreserba_data");  // Aquí obtenemos la fecha
                int pertsonaKopurua = rs.getInt("pertsona_kopurua");
                int mahaiaId = rs.getInt("mahaia_id");

                // Crear un objeto Erreserba con los valores obtenidos
                Erreserba erreserba = new Erreserba(id, erreserbaIzena, erreserbaData, pertsonaKopurua, mahaiaId);

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
        String insertQuery = "INSERT INTO erreserba (erreserba_izena, erreserba_data, pertsona_kopurua, mahaia_id) VALUES (?, ?, ?, ?)";

        // Consulta para obtener el 'gehienezko_kopurua' del 'mahaia'
        String selectQuery = "SELECT gehienezko_kopurua FROM mahaia WHERE id = ?";

        // Consulta para verificar si la mesa ya está reservada en esa fecha
        String checkReservaQuery = "SELECT COUNT(*) FROM erreserba WHERE mahaia_id = ? AND erreserba_data = ?";

        try (Connection conn = DbKonexioa.getKonexioa()) {
            // Paso 1: Verificar que el número de personas no sea 0 o negativo
            if (erreserba.getPertsonaKopurua() <= 0) {
                mezuaPantailaratu("Errorea", "Pertsona kopurua ezin da 0 edo negatiboa izan.", Alert.AlertType.ERROR);
                return false;  // Retornar false si hay un error en la validación
            }

            // Paso 2: Verificar el número máximo de personas permitidas para la mesa
            int gehienezkoKopurua = 0;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setInt(1, erreserba.getMahiaId());
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        gehienezkoKopurua = rs.getInt("gehienezko_kopurua");
                    } else {
                        mezuaPantailaratu("Errorea", "Mahaia ez da existitzen.", Alert.AlertType.ERROR);
                        return false;  // Retornar false si no se encuentra la mesa
                    }
                }
            }

            // Validar si el número de personas excede el máximo permitido
            if (gehienezkoKopurua < erreserba.getPertsonaKopurua()) {
                mezuaPantailaratu(
                        "Errorea",
                        "Ezingo da erreserba egin: Mahaian gehienezko pertsonen kopurua gainditzen du.",
                        Alert.AlertType.ERROR
                );
                return false;  // Retornar false si el número de personas excede el límite
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
                checkStmt.setInt(1, erreserba.getMahiaId());
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
                insertStmt.setInt(4, erreserba.getMahiaId());

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

}


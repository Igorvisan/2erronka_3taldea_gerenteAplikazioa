package com.example.javafx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;

public class LangileaDbKudeaketa {

        public static int erabiltzaileaKomprobatu(String erab, String pasa) {
        String query = "SELECT postua FROM langilea WHERE izena = ? AND pasahitza = ?";

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, erab);
            stmt.setString(2, pasa);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String lanPostua = rs.getString("postua");
                if ("Gerentea".equalsIgnoreCase(lanPostua)) {
                    System.out.println("Es gerente");
                    return 1; // gerentea da
                } else {
                    System.out.println("No es gerente");
                    return 0; // ez da gerentea
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea autentikazioan: " + e.getMessage());
        }

        return -1; // ez da existitzen edo errorea
    }

    public static ObservableList<Langilea> getAllLangileak() {
        String query = "SELECT id, dni, izena, abizena, korreoa, postua, pasahitza, telefonoa, txatBaimena, updateData, updateBy FROM langilea";
        ObservableList<Langilea> langileenLista = FXCollections.observableArrayList();

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Langilea langilea = new Langilea(
                    rs.getInt("id"),
                    rs.getString("dni"),
                    rs.getString("izena"),
                    rs.getString("abizena"),
                    rs.getString("korreoa"),
                    rs.getString("postua"),
                    rs.getString("pasahitza"),
                    rs.getString("telefonoa"),
                    rs.getBoolean("txatBaimena"),
                    rs.getDate("updateData"),
                    rs.getString("updateBy")
                );
                langileenLista.add(langilea);
            }

        } catch (SQLException e) {
            System.err.println("Errorea datuak eskuratzerakoan: " + e.getMessage());
        }

        return langileenLista;
    }

    public static boolean langileaGehitu(Langilea langilea) {
        boolean opreacionRealizada = false;
        String query = "INSERT INTO langilea (izena, dni, korreoa, telefonoa, pasahitza, postua, txatBaimena) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Establecer los valores para los campos en la base de datos
            stmt.setString(1, langilea.getIzena());
            stmt.setString(2, langilea.getDni());
            stmt.setString(3, langilea.getEmail());
            stmt.setString(4, langilea.getTelefonoa());
            stmt.setString(5, langilea.getPasahitza());
            stmt.setString(6, langilea.getLanPostua());
            stmt.setBoolean(7, langilea.isTxatBaimena()); // Cambiado índice a 5

            // Ejecutar la actualización de la base de datos
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener el ID autoincrementado después de insertar
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1); // Obtener el ID autoincremental generado
                        langilea.setId(generatedId); // Establecer el ID generado al objeto Langilea

                        // Mostrar mensaje de éxito
                        /*String izena = "Langilea Gehitu";
                        String mezuLuzea = "Langilea zuzen gehitu da. Honako hau da bere ID-a: " + generatedId;
                        mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.INFORMATION);*/
                        return opreacionRealizada = true;
                    }
                }
            } else {
                // Si no se inserta ninguna fila, mostrar mensaje de error
                /*String izena = "Errorea";
                String mezuLuzea = "Ez da langilea gehitu.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);*/
                return opreacionRealizada = false;
            }

        } catch (SQLException e) {
            // Si ocurre un error, mostrar el mensaje y trazar la excepción
            /*String izena = "Errorea";
            String mezuLuzea = "Errorea langilea gehitzean: " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);*/

            e.printStackTrace();
        }
        return opreacionRealizada;
    }

    public static boolean langileaEzabatu(int langileId) {
        boolean opreacionRealizada = false;
        String query = "DELETE FROM langilea WHERE id = ?"; // Query para eliminar un trabajador por ID

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, langileId); // Usar el ID del trabajador

            System.out.println("Ejecutando consulta: " + stmt.toString()); // Imprimir la consulta para depuración

            int filasAfectadas = stmt.executeUpdate();
            System.out.println("Filas afectadas: " + filasAfectadas); // Ver cuántas filas se han afectado

            if (filasAfectadas > 0) {
                /*String izena = "Langilea Ezabatu";
                String mezuLuzea = "Langilea arrakastaz ezabatu da.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.INFORMATION);*/
                System.out.println("Se ha eliminado al trabajador");
                opreacionRealizada = true;
            } else {
                /*String izena = "Errorea";
                String mezuLuzea = "Ez da langilea aurkitu.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);*/
                System.out.println("No se ha podido eliminar al trabajador");
                opreacionRealizada = false;
            }

        } catch (SQLException e) {
            /*String izena = "Errorea";
            String mezuLuzea = "Errorea langilea ezabatzean: " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);*/

            e.printStackTrace(); // Imprimir el error completo para depuración
        }

        // Añadir esta línea para devolver el resultado
        return opreacionRealizada;
    }

    public static boolean editatuLangilea(Langilea langilea) {
        // Consulta SQL para actualizar los datos del trabajador
        String query = "UPDATE langilea SET izena = ?, abizena = ?, dni = ?, korreoa = ?, postua = ?, pasahitza = ?, telefonoa = ?, txatBaimena = ?, updateData = ?, updateBy = ? WHERE id = ?";

        try (Connection conn = DbKonexioa.getKonexioa(); // Obtención de la conexión a la base de datos
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Asignar los parámetros al PreparedStatement
            stmt.setString(1, langilea.getIzena());
            stmt.setString(2, langilea.getAbizena());
            stmt.setString(3, langilea.getDni());
            stmt.setString(4, langilea.getEmail());
            stmt.setString(5, langilea.getLanPostua());
            stmt.setString(6, langilea.getPasahitza());
            stmt.setString(7, langilea.getTelefonoa());
            stmt.setBoolean(8, langilea.isTxatBaimena());
            stmt.setDate(9, langilea.getUpdateData());
            stmt.setString(10, langilea.getUpdateBy());
            stmt.setInt(11, langilea.getId());

            // Ejecutar la actualización y obtener el número de filas afectadas
            int rowsAffected = stmt.executeUpdate();

            // Si se actualizaron filas, la operación fue exitosa
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Captura y muestra los errores de SQL con más detalle
            System.err.println("Error al actualizar los datos del empleado con ID " + langilea.getId() + ": " + e.getMessage());
            e.printStackTrace(); // Imprime la traza de la excepción para diagnóstico
            return false;
        }
    }
    public static Langilea langileaLortuIzenaBidez(String izena) {
        String query = "SELECT id, dni, izena, abizena, korreoa, postua, pasahitza, telefonoa, txatBaimena, updateData, updateBy FROM langilea WHERE izena = ?";
        Langilea langilea = null;

        Langilea langileIzenaBidez = null;
        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, izena);  // Establecemos el nombre del trabajador para la consulta
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Si se encuentra un resultado, crear el objeto Langilea con los datos obtenidos
                langileIzenaBidez = new Langilea(
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getString("izena"),
                        rs.getString("abizena"),
                        rs.getString("korreoa"),
                        rs.getString("postua"),
                        rs.getString("pasahitza"),
                        rs.getString("telefonoa"),
                        rs.getBoolean("txatBaimena"),
                        rs.getDate("updateData"),
                        rs.getString("updateBy")
                );
            }

        } catch (SQLException e) {
            System.err.println("Errorea langilea eskuratzerakoan: " + e.getMessage());
        }
        return langileIzenaBidez;  // Devuelve el objeto Langilea si lo encontró, o null si no lo encontró
    }

}

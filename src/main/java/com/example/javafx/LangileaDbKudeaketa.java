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
        String query = "SELECT lan_postua FROM langile WHERE izena = ? AND pasahitza = ?";

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, erab);
            stmt.setString(2, pasa);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String lanPostua = rs.getString("lan_postua");
                if ("Gerentea".equalsIgnoreCase(lanPostua)) {
                    return 1; // gerentea da
                } else {
                    return 0; // ez da gerentea
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea autentikazioan: " + e.getMessage());
        }

        return -1; // ez da existitzen edo errorea
    }

    public static ObservableList<Langilea> getAllLangileak() {
        String query = "SELECT id, izena, email, lan_postua, pasahitza FROM langile";
        ObservableList<Langilea> langileenLista = FXCollections.observableArrayList();

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Langilea langilea = new Langilea(
                        rs.getInt("id"),
                        rs.getString("izena"),
                        rs.getString("email"),
                        rs.getString("lan_postua"),
                        rs.getString("pasahitza")

                );
                langileenLista.add(langilea);
            }

        } catch (SQLException e) {
            System.err.println("Errorea datuak eskuratzerakoan: " + e.getMessage());
        }

        return langileenLista;
    }

    public static void langileaGehitu(Langilea langilea) {
        String query = "INSERT INTO langile (izena, email, pasahitza, lan_postua) VALUES (?, ?, ?, ?)";

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Establecer los valores para los campos en la base de datos
            stmt.setString(1, langilea.getIzena());
            stmt.setString(2, langilea.getEmail());
            stmt.setString(3, langilea.getPasahitza());
            stmt.setString(4, langilea.getLanPostua());

            // Ejecutar la actualización de la base de datos
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener el ID autoincrementado después de insertar
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1); // Obtener el ID autoincremental generado
                        langilea.setId(generatedId); // Establecer el ID generado al objeto Langilea

                        // Mostrar mensaje de éxito
                        String izena = "Langilea Gehitu";
                        String mezuLuzea = "Langilea zuzen gehitu da. Honako hau da bere ID-a: " + generatedId;
                        mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.INFORMATION);
                    }
                }
            } else {
                // Si no se inserta ninguna fila, mostrar mensaje de error
                String izena = "Errorea";
                String mezuLuzea = "Ez da langilea gehitu.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            // Si ocurre un error, mostrar el mensaje y trazar la excepción
            String izena = "Errorea";
            String mezuLuzea = "Errorea langilea gehitzean: " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);

            e.printStackTrace();
        }
    }
    public static void langileaEzabatu(int langileId) {
        String query = "DELETE FROM langile WHERE id = ?"; // Query para eliminar un trabajador por ID

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, langileId); // Usar el ID del trabajador

            System.out.println("Ejecutando consulta: " + stmt.toString()); // Imprimir la consulta para depuración

            int filasAfectadas = stmt.executeUpdate();
            System.out.println("Filas afectadas: " + filasAfectadas); // Ver cuántas filas se han afectado

            if (filasAfectadas > 0) {
                String izena = "Langilea Ezabatu";
                String mezuLuzea = "Langilea arrakastaz ezabatu da.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.INFORMATION);
            } else {
                String izena = "Errorea";
                String mezuLuzea = "Ez da langilea aurkitu.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            String izena = "Errorea";
            String mezuLuzea = "Errorea langilea ezabatzean: " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            e.printStackTrace(); // Imprimir el error completo para depuración
        }
    }
    public static boolean editatuLangilea(Langilea langilea) {
        String query = "UPDATE langile SET izena = ?, email = ?, lan_postua = ?, pasahitza = ? WHERE id = ?";

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Asignar los parámetros al PreparedStatement
            stmt.setString(1, langilea.getIzena());
            stmt.setString(2, langilea.getEmail());
            stmt.setString(3, langilea.getLanPostua());
            stmt.setString(4, langilea.getPasahitza());
            stmt.setInt(5, langilea.getId());

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando los datos del empleado: " + e.getMessage());
            return false;
        }
    }


}

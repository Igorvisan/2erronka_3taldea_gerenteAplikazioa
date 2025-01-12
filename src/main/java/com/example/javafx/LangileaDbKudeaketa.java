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
        String query = "SELECT id, izena, email, lan_postua FROM langile";
        ObservableList<Langilea> langileenLista = FXCollections.observableArrayList();

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Langilea langilea = new Langilea(
                        rs.getInt("id"),
                        rs.getString("izena"),
                        rs.getString("email"),
                        "",
                        rs.getString("lan_postua")
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


            stmt.setString(1, langilea.getIzena());
            stmt.setString(2, langilea.getEmail());
            stmt.setString(3, langilea.getPasahitza());
            stmt.setString(4, langilea.getLanPostua());

            // Ejecutar la actualización de la base de datos
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener el id generado
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1); // El primer campo es el id autoincremental
                        langilea.setId(generatedId); // Establecer el id generado al objeto Langilea
                        String izena = "Langilea Gehitu";
                        String mezuLuzea = "Langilea zuzen gehitu da. Honako hau da bere id-a: " + generatedId;
                        mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.INFORMATION);
                    }
                }
            }

        } catch (SQLException e) {
            String izena = "Errorea";
            String mezuLuzea = "Errorea langilea gehitzean: " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
        }
    }

}

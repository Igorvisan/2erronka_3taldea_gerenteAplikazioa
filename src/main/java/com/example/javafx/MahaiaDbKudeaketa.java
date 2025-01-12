package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;

public class MahaiaDbKudeaketa {

    public static ObservableList<Mahaia> getAllMahaiak() {
        String query = "SELECT id, gehienezko_kopurua, libre FROM mahaia";
        ObservableList<Mahaia> mahaienLista = FXCollections.observableArrayList();

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Mahaia mahaia = new Mahaia(
                        rs.getInt("id"),
                        rs.getInt("gehienezko_kopurua"),
                        rs.getBoolean("libre")
                );
                mahaienLista.add(mahaia);
            }

        } catch (SQLException e) {
            System.err.println("Errorea datuak eskuratzerakoan: " + e.getMessage());
        }

        return mahaienLista;
    }

    public static void mahaiaGehitu(Mahaia mahaia) {
        String query = "INSERT INTO mahaia (id, gehienezko_kopurua, libre) VALUES (?, ?, ?)";

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Establecer los valores para los campos en la base de datos
            stmt.setInt(1, mahaia.getId());
            stmt.setInt(2, mahaia.getGehienezkoKopurua());
            stmt.setBoolean(3, mahaia.isLibre()); // Establece el valor de libre como booleano

            // Ejecutar la actualización de la base de datos
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        mahaia.setId(generatedId);

                        String izena = "Mahaia Gehitu";
                        String mezuLuzea = "Mahaia zuzen gehitu da. Honako hau da bere zenbakia: " + generatedId;
                        mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.INFORMATION);
                    }
                }
            } else {
                String izena = "Errorea";
                String mezuLuzea = "Ez da mahaia gehitu. Berriro saiatu";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            String izena = "Errorea";
            String mezuLuzea = "Errorea mahaia gehitzean: " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

}

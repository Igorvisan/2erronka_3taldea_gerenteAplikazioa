package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}

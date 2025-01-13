package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

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
}

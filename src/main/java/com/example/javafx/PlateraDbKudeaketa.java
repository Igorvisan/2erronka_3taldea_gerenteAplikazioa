package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;

public class PlateraDbKudeaketa {

    public static ObservableList<Platera> getAllPlaterak() {
        String query = "SELECT izena, deskribapena, kategoria, kantitatea, prezioa, menu FROM platera ";
        ObservableList<Platera> platerenLista = FXCollections.observableArrayList();

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String izena = rs.getString("izena");
                String deskribapena = rs.getString("deskribapena");
                String kategoria = rs.getString("kategoria");
                int kantitatea = rs.getInt("kantitatea");
                float prezioa = rs.getFloat("prezioa");
                boolean menu = rs.getBoolean("menu");

                Platera platera = new Platera(izena, deskribapena, kategoria, kantitatea, prezioa, menu);
                platerenLista.add(platera);
            }
        }catch (SQLException e) {
            System.err.println("Errorea datuak eskuratzerakoan: " + e.getMessage());
        }
        return platerenLista;
    }

    public static ObservableList<Produktua> getAllProduktuak() {
        ObservableList<Produktua> produktuenLista = FXCollections.observableArrayList();

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM produktua")) {

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Produktua produktua = new Produktua(
                        rs.getString("izena"),
                        rs.getInt("stock"),
                        rs.getInt("max"),
                        rs.getInt("min"),
                        rs.getFloat("prezioa")
                );
                produktuenLista.add(produktua);
            }

            return produktuenLista;

        } catch (SQLException e) {
            e.printStackTrace();
            mezuaPantailaratu("Error", "No se pudieron cargar los productos", Alert.AlertType.ERROR);
            return FXCollections.emptyObservableList();
        }
    }

    public static void plateraGehitu(Platera platera) {
        String query = "INSERT INTO platera (izena, deskribapena, kategoria, kantitatea, prezioa, menu) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection conn = DbKonexioa.getKonexioa();
        PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, platera.getIzena());
            statement.setString(2, platera.getDeskribapena());
            statement.setString(3, platera.getKategoria());
            statement.setInt(4, platera.getKantitatea());
            statement.setFloat(5, platera.getPrezioa());
            statement.setBoolean(6, platera.getMenu());

            int affectedRows = statement.executeUpdate();

            if(affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        platera.setId(generatedId);

                        //Mostrar Mensaje de confirmacion
                        String izena = "Success";
                        String mezuLuzea = "Platera zuzen gehitu da. Honako hau da bere ID-a: " + generatedId;
                        mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.CONFIRMATION);
                    }
                }
            }else{
                String izena = "Error";
                String mezuLuzea = "Ez da platera gehitu";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            }
        }catch (SQLException e) {
            String  izena = "Errorea";
            String mezuLuzea = "Ezin izan dira datuak ezarri";
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
        }
    }
}

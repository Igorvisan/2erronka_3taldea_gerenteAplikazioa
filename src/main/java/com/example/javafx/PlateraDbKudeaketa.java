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
        String query = "SELECT id, izena, deskribapena, kategoria, kantitatea, prezioa, menu FROM platera WHERE deletedAt IS NULL";
        ObservableList<Platera> platerenLista = FXCollections.observableArrayList();

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idPlatera = rs.getInt("id");
                String izena = rs.getString("izena");
                String deskribapena = rs.getString("deskribapena");
                String kategoria = rs.getString("kategoria");
                int kantitatea = rs.getInt("kantitatea");
                float prezioa = rs.getFloat("prezioa");
                boolean menu = rs.getBoolean("menu");

                Platera platera = new Platera(idPlatera, izena, deskribapena, kategoria, kantitatea, prezioa, menu);
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
                int id = rs.getInt("id");
                String izena = rs.getString("izena");
                int stock = rs.getInt("stock");
                int max = rs.getInt("max");
                int min = rs.getInt("min");
                float prezioa = rs.getFloat("prezioa");

                produktuenLista.add(new Produktua(id, izena, stock, max, min, prezioa));
            }

            return produktuenLista;

        } catch (SQLException e) {
            e.printStackTrace();
            mezuaPantailaratu("Error", "No se pudieron cargar los productos", Alert.AlertType.ERROR);
            return FXCollections.emptyObservableList();
        }
    }

    public static int plateraGehitu(Platera platera, String createdByString) {
        int createdById = -1;
        String selectQuery = "SELECT id FROM langilea WHERE izena = ?";
        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            selectStmt.setString(1, createdByString);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                createdById = rs.getInt("id");
            } else {
                mezuaPantailaratu("Error", "Ez da langilearen ID aurkitu", Alert.AlertType.ERROR);
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mezuaPantailaratu("Errorea", "Errorea langilearen ID bilatzerakoan", Alert.AlertType.ERROR);
            return -1;
        }

        String query = "INSERT INTO platera (izena, deskribapena, kategoria, kantitatea, prezioa, menu, createdAt, createdBy) VALUES (?, ?, ?, ?, ?, ?, NOW(), ?)";
        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, platera.getIzena());
            statement.setString(2, platera.getDeskribapena());
            statement.setString(3, platera.getKategoria());
            statement.setInt(4, platera.getKantitatea());
            statement.setFloat(5, platera.getPrezioa());
            statement.setBoolean(6, platera.getMenu());
            statement.setInt(7, createdById);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        platera.setId(generatedId);
                        String izena = "Success";
                        String mezuLuzea = "Platera zuzen gehitu da. Honako hau da bere ID-a: " + generatedId;
                        mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.CONFIRMATION);
                        return generatedId;
                    }
                }
            } else {
                mezuaPantailaratu("Error", "Ez da platera gehitu", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mezuaPantailaratu("Errorea", "Ezin izan dira datuak ezarri", Alert.AlertType.ERROR);
        }
        return -1;
    }


    public static int getProduktuIzenaBtName(String produktuaIzena) {
        String query = "SELECT id FROM produktua WHERE izena = ?";
        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, produktuaIzena);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Si no encuentra el producto
    }

    public static void insterPlateraProduktuak(int idPlatera, int idProduktu, int kantitatea){
        String query = "INSERT INTO platera_produktua (platera_id, produktua_id, kantitatea) VALUES (?, ?, ?)";
        try(Connection conn = DbKonexioa.getKonexioa();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, idPlatera);
            stmt.setInt(2, idProduktu);
            stmt.setInt(3, kantitatea);
            stmt.executeUpdate();

        }catch(SQLException e) {
            e.printStackTrace();
            String mezua = "Errorea";
            String mezuLuzea = "Ezin izan dira datuak ezarri";
            mezuaPantailaratu(mezua, mezuLuzea, Alert.AlertType.ERROR);
        }
    }

    public static ObservableList<PlateraProduktua> getProduktuakByPlateraId(int plateraId) {
        ObservableList<PlateraProduktua> lista = FXCollections.observableArrayList();

        String query = "SELECT p.izena AS produktua, pp.kantitatea AS kantitatea "
                + "FROM platera_produktua pp "
                + "JOIN produktua p ON p.id = pp.produktua_id "
                + "WHERE pp.platera_id = ?";

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, plateraId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String produktua = rs.getString("produktua");
                int kantitatea = rs.getInt("kantitatea");
                lista.add(new PlateraProduktua(produktua, kantitatea));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String mezua = "Error";
            String mezuLuzea = "Ezin izar dira datuak kargatu";
            mezuaPantailaratu(mezua, mezuLuzea, Alert.AlertType.ERROR);
        }

        return lista;
    }

    public static void updatePlateraProduktua(int plateraId, int oldProductId, int newProductId, int nuevaCantidad) {
        String query = "UPDATE platera_produktua SET produktua_id = ?, kantitatea = ? WHERE platera_id = ? AND produktua_id = ?";
        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, newProductId);
            stmt.setInt(2, nuevaCantidad);
            stmt.setInt(3, plateraId);
            stmt.setInt(4, oldProductId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int updatePlatera(Platera platera, String updateByString) {
        int createdById = -1;
        String selectQuery = "SELECT id FROM langilea WHERE izena = ?";
        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            selectStmt.setString(1, updateByString);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                createdById = rs.getInt("id");
            } else {
                mezuaPantailaratu("Error", "Ez da langilearen ID aurkitu", Alert.AlertType.ERROR);
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mezuaPantailaratu("Errorea", "Errorea langilearen ID bilatzerakoan", Alert.AlertType.ERROR);
            return -1;
        }
        String query = "UPDATE platera SET izena = ?, deskribapena = ?, kategoria = ?, kantitatea = ?, prezioa = ?, menu = ?, updatedAt = NOW(), updatedBy = ? WHERE id = ?";
        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, platera.getIzena());
            stmt.setString(2, platera.getDeskribapena());
            stmt.setString(3, platera.getKategoria());
            stmt.setInt(4, platera.getKantitatea());
            stmt.setFloat(5, platera.getPrezioa());
            stmt.setBoolean(6, platera.getMenu());
            stmt.setInt(7, createdById);
            stmt.setInt(8, platera.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                mezuaPantailaratu("Success", "Platera eguneratu da", Alert.AlertType.CONFIRMATION);
            } else {
                mezuaPantailaratu("Error", "Ez da platera eguneratu", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mezuaPantailaratu("Error", "Errorea datuak eguneratzerakoan", Alert.AlertType.ERROR);
        }
        return -1;
    }

    public static void deletePlatera(int plateraId, String deletedByName) {
        int langileId = -1;
        String selectLangile = "SELECT id FROM langilea WHERE izena = ?";

        // 1. Obtener el ID del langilea usando el nombre
        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement selectStmt = conn.prepareStatement(selectLangile)) {

            selectStmt.setString(1, deletedByName);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                langileId = rs.getInt("id");
            } else {
                mezuaPantailaratu("Error", "Ez da langilearen ID aurkitu", Alert.AlertType.ERROR);
                return; // Si no se encuentra, se detiene la operación
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mezuaPantailaratu("Error", "Ez da lortu langilearen ID", Alert.AlertType.ERROR);
            return;
        }

        // 2. Actualizar la fila de platera para marcarla como eliminada (borrado lógico)
        String query = "UPDATE platera SET deletedAt = NOW(), deletedBy = ? WHERE id = ?";
        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, langileId);  // Aquí se asigna el ID obtenido del langilea
            stmt.setInt(2, plateraId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                mezuaPantailaratu("Success", "Platera ezabatuta markatu da", Alert.AlertType.CONFIRMATION);
            } else {
                mezuaPantailaratu("Error", "Ez da platera ezabatu", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mezuaPantailaratu("Errorea", "Datuak ezabatzerakoan arazo bat izan da", Alert.AlertType.ERROR);
        }
    }

}


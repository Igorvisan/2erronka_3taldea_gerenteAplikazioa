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
        String query = "SELECT id, kopurua, mahaiZenbakia, habilitado FROM mahaia";
        ObservableList<Mahaia> mahaienLista = FXCollections.observableArrayList();

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int komentsalKopurua = rs.getInt("kopurua");
                int mahaiZenbakia = rs.getInt("mahaiZenbakia");
                boolean habilitado = rs.getBoolean("habilitado");

                Mahaia mahaia = new Mahaia(id, mahaiZenbakia, komentsalKopurua, habilitado);
                mahaienLista.add(mahaia);
            }

        } catch (SQLException e) {
            System.err.println("Errorea datuak eskuratzerakoan: " + e.getMessage());
        }

        return mahaienLista;
    }


    public static void mahaiaGehitu(Mahaia mahaia) {
        String query = "INSERT INTO mahaia (mahaiZenbakia, kopurua, habilitado) VALUES (?, ?, ?)";

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Establecer los valores para los campos en la base de datos
            stmt.setInt(1, mahaia.getMahaiZenbakia());
            stmt.setInt(2, mahaia.getKomentsalKopurua());
            stmt.setBoolean(3, mahaia.isHabilitado());

            // Ejecutar la actualización de la base de datos
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener el ID autoincrementado después de insertar
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1); // Obtener el ID autoincremental generado
                        mahaia.setId(generatedId); // Establecer el ID generado al objeto Langilea

                        // Mostrar mensaje de éxito
                        String izena = "mahaia Gehitu";
                        String mezuLuzea = "mahaia zuzen gehitu da. Honako hau da bere ID-a: " + generatedId;
                        mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.INFORMATION);
                    }
                }
            } else {
                // Si no se inserta ninguna fila, mostrar mensaje de error
                String izena = "Errorea";
                String mezuLuzea = "Ez da mahaia gehitu.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            // Si ocurre un error, mostrar el mensaje y trazar la excepción
            String izena = "Errorea";
            String mezuLuzea = "Errorea mahaia gehitzean: " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);

            e.printStackTrace();
        }
    }

    public static boolean editatuMahaia(Mahaia mahaia) {
        String query = "UPDATE mahaia SET kopurua = ?, mahaiZenbakia = ?, habilitado = ? WHERE id = ?";

        try (Connection conn = DbKonexioa.getKonexioa(); // Obtención de la conexión a la base de datos
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Asignar los parámetros al PreparedStatement
            stmt.setInt(1, mahaia.getKomentsalKopurua());
            stmt.setInt(2, mahaia.getMahaiZenbakia());
            stmt.setBoolean(3, mahaia.isHabilitado());
            stmt.setInt(4, mahaia.getId());

            // Ejecutar la actualización y obtener el número de filas afectadas
            int rowsAffected = stmt.executeUpdate();

            // Si se actualizaron filas, la operación fue exitosa
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Captura y muestra los errores de SQL con más detalle
            System.err.println("Error al actualizar los datos del empleado con ID " + mahaia.getId() + ": " + e.getMessage());
            return false;
        }
    }

    public static void mahaiaEzabatu(int mahaiaId) {
        String query = "DELETE FROM mahaia WHERE id = ?";

        try (Connection conn = DbKonexioa.getKonexioa();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Establecer el ID de la mesa a eliminar
            stmt.setInt(1, mahaiaId);

            // Ejecutar la actualización de la base de datos
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                String izena = "Mahaia Ezabatuta";
                String mezuLuzea = "Mahaia zuzen ezabatu da.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.INFORMATION);
            } else {
                String izena = "Errorea";
                String mezuLuzea = "Ez da mahaia ezabatu. Berriro saiatu mesedez.";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            String izena = "Errorea";
            String mezuLuzea = "Errorea mahaia ezabatzean: " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public static void terrazaAbilitatu(){
        String query = "UPDATE mahaia SET habilitado = ? WHERE id IN(13, 14, 15, 16)";
        try(Connection conn = DbKonexioa.getKonexioa();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            if(conn == null){
                String izena = "Errorea";
                String mezuLuzea = "Ez da datu basera konektatu";
                mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
                return;
            }

            stmt.setBoolean(1, true);
            int affectedRows = stmt.executeUpdate();

            if(affectedRows > 0){
                String titulua = "Mahaia Abilitatuta";
                String mezuLuzea = "Mahaia zuzen abilitatu da.";
                mezuaPantailaratu(titulua, mezuLuzea, Alert.AlertType.INFORMATION);
            }else{
                String titulua = "Errorea";
                String mezuLuzea = "Ez da mahaia abilitatu.";
                mezuaPantailaratu(titulua, mezuLuzea, Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            // Si ocurre un error, mostrar el mensaje y trazar la excepción
            String izena = "Errorea";
            String mezuLuzea = "Errorea mahaia abilitatzean: " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public static void terrazaDesabilitatu(){
        String query = "UPDATE mahaia SET habilitado = ? WHERE id IN(13, 14, 15, 16)";

        try(Connection conn = DbKonexioa.getKonexioa();
        PreparedStatement stmt = conn.prepareStatement(query)){
            if(conn == null){
                String izena = "Errorea";
                String mezuLuzuea = "Ez da datu basera konektatu";
                mezuaPantailaratu(izena, mezuLuzuea, Alert.AlertType.ERROR);
            }

            stmt.setBoolean(1, false);
            int affectedRows = stmt.executeUpdate();
            if(affectedRows > 0){
                String titulua = "ONGI";
                String mezuLuzea = "Mahaia zuzen desabilitatu da.";
                mezuaPantailaratu(titulua, mezuLuzea, Alert.AlertType.INFORMATION);
            }else{
                String titulua = "Errorea";
                String mezuLuzea = "Ez da mahaia desabilitatu.";
                mezuaPantailaratu(titulua, mezuLuzea, Alert.AlertType.ERROR);
            }
        }catch (SQLException e){
            String izena = "Errorea";
            String mezuLuzea = "Ez dira mahaiak desabilitatu " + e.getMessage();
            mezuaPantailaratu(izena, mezuLuzea, Alert.AlertType.ERROR);
        }
    }


}

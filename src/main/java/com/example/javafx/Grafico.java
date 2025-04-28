package com.example.javafx;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Grafico {
    private String mahaiZenbakia;  // Definido como String para compatibilidad con JasperReports
    private Integer kantidad;      // Cantidad de reservas por mesa

    // Constructor actualizado
    public Grafico(String mahaiZenbakia, Integer kantidad) {
        this.mahaiZenbakia = mahaiZenbakia;  // Asegura que sea String
        this.kantidad = kantidad;
    }

    // Getters para JasperReports
    public String getMahaiZenbakia() {
        return mahaiZenbakia;  // Devuelve String
    }

    public Integer getKantidad() {
        return kantidad;  // Devuelve Integer
    }

    // Metodo estático para obtener reservas por mesa
    public static List<Grafico> getReservasPorMesa(Connection conn) throws SQLException {
        List<Grafico> data = new ArrayList<>();
        String query = "SELECT mahaiZenbakia, COUNT(*) AS kantidad FROM erreserba GROUP BY mahaiZenbakia";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Obtener mahaiZenbakia directamente como String
                String mesa = rs.getString("mahaiZenbakia"); // No se necesita conversión
                int kantidad = rs.getInt("kantidad");
                data.add(new Grafico(mesa, kantidad)); // Asegura que mahaiZenbakia sea String
            }
        }

        // Imprimir datos en consola para depuración
        System.out.println("Reservas por Mesa:");
        for (Grafico g : data) {
            System.out.println("Mesa: " + g.getMahaiZenbakia() + " | Cantidad: " + g.getKantidad());
        }

        return data;
    }
}
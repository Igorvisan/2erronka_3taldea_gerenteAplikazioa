package com.example.javafx;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GraficoBebida {
    private String izena;
    private Integer cantidad;

    public GraficoBebida(String izena, Integer cantidad) {
        this.izena = izena;
        this.cantidad = cantidad;
    }

    // Getters para JasperReports
    public String getIzena() { return izena; }
    public Integer getCantidad() { return cantidad; }

    // Metodo estático para obtener bebidas por cantidad
    public static List<GraficoBebida> getBebidasPorCantidad(Connection conn) throws SQLException {
        List<GraficoBebida> data = new ArrayList<>();
        String query = "SELECT izena, COUNT(*) AS cantidad FROM eskaera GROUP BY izena";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nombre = rs.getString("izena");
                int cantidad = rs.getInt("cantidad");
                data.add(new GraficoBebida(nombre, cantidad));
            }
        }

        // Imprimir datos en consola (opcional)
        System.out.println("Bebidas por Cantidad:");
        for (GraficoBebida g : data) {
            System.out.println("Bebida: " + g.getIzena() + " | Cantidad: " + g.getCantidad());
        }

        return data;
    }
}
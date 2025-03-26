package com.example.javafx;

public class Produktua {
    private int id;
    private String izena;
    private int stock;
    private int max;
    private int min;
    private float prezioa;

    public Produktua(){}

    public Produktua(int id, String izena, int stock, int max, int min, float prezioa) {
        this.id = id;
        this.izena = izena;
        this.stock = stock;
        this.max = max;
        this.min = min;
        this.prezioa = prezioa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getMax() { return max; }
    public void setMax(int max) { this.max = max; }
    public int getMin() { return min; }
    public void setMin(int min) { this.min = min; }
    public float getPrezioa() { return prezioa; }
    public void setPrezioa(float prezioa) { this.prezioa = prezioa; }

}

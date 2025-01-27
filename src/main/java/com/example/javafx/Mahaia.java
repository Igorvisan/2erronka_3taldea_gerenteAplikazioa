package com.example.javafx;

public class Mahaia {
    private int id;
    private int gehienezkoKopurua;
    private String izena;

    // Constructor con todos los parámetros
    public Mahaia(int id, int gehienezkoKopurua, String izena) {
        this.id = id;
        this.gehienezkoKopurua = gehienezkoKopurua;
        this.izena = izena;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGehienezkoKopurua() {
        return gehienezkoKopurua;
    }

    public void setGehienezkoKopurua(int gehienezkoKopurua) {
        this.gehienezkoKopurua = gehienezkoKopurua;
    }

    public String getIzena() {
        return izena;
    }
    public void setIzena(String izena) {
        this.izena = izena;
    }
}

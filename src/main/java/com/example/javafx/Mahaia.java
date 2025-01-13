package com.example.javafx;

public class Mahaia {
    private int id;
    private int gehienezkoKopurua;

    // Constructor con todos los parámetros
    public Mahaia(int id, int gehienezkoKopurua) {
        this.id = id;
        this.gehienezkoKopurua = gehienezkoKopurua;
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
}

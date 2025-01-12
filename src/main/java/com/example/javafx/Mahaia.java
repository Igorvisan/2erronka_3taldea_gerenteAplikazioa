package com.example.javafx;

public class Mahaia {
    private int id;
    private int gehienezkoKopurua;
    private boolean libre;

    public Mahaia() {}
    public Mahaia(int id, int gehienezkoKopurua, boolean libre) {
        this.id = id;
        this.gehienezkoKopurua = gehienezkoKopurua;
        this.libre = libre;
    }
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
        this.gehienezkoKopurua= gehienezkoKopurua;
    }
    public boolean isLibre() {
        return libre;
    }
    public void setLibre(boolean libre) {
        this.libre = libre;
    }
}

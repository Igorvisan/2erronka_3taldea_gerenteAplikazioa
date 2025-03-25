package com.example.javafx;

public class Mahaia {
    private int id;
    private int mahaiZenbakia;
    private int komentsalKopurua;
    private boolean habilitado;

    // Constructor con todos los parámetros
    public Mahaia(int id, int mahaiZenbakia, int komentsalKopurua, boolean habilitado) {
        this.id = id;
        this.mahaiZenbakia = mahaiZenbakia;
        this.komentsalKopurua = komentsalKopurua;
        this.habilitado = habilitado;
    }

    public Mahaia() {}

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMahaiZenbakia() {
        return mahaiZenbakia;
    }

    public void setMahaiZenbakia(int mahaiZenbakia) {
        this.mahaiZenbakia = mahaiZenbakia;
    }

    public int getKomentsalKopurua() {
        return komentsalKopurua;
    }
    public void setKomentsalKopurua(int komentsalKopurua) {
        this.komentsalKopurua = komentsalKopurua;
    }
    public boolean isHabilitado() {
        return habilitado;
    }
    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}

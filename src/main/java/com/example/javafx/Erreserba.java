package com.example.javafx;

import java.util.Date;

public class Erreserba {
    private int id;
    private String erreserbaIzena;
    private Date erreserbaDate;
    private int pertsonaKopurua;
    private int mahaiZenbakia;
    private boolean kantzelatuta;
    private Date updateData;
    private String updatedBy;

    public Erreserba() {}

    public Erreserba(int id, String erreserbaIzena, Date erreserbaDate, int pertsonaKopurua, int mahaiZenbakia, boolean kantzelatuta, Date updateData, String updatedBy) {
        this.id = id;
        this.erreserbaIzena = erreserbaIzena;
        this.erreserbaDate = erreserbaDate;
        this.pertsonaKopurua = pertsonaKopurua;
        this.mahaiZenbakia = mahaiZenbakia;
        this.kantzelatuta = kantzelatuta;
        this.updateData = updateData;
        this.updatedBy = updatedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getErreserbaIzena() {
        return erreserbaIzena;
    }

    public void setErreserbaIzena(String erreserbaIzena) {
        this.erreserbaIzena = erreserbaIzena;
    }

    public java.sql.Date getErreserbaDate() {
        return (java.sql.Date) erreserbaDate;
    }

    public void setErreserbaDate(Date erreserbaDate) {
        this.erreserbaDate = erreserbaDate;
    }

    public int getPertsonaKopurua() {
        return pertsonaKopurua;
    }

    public void setPertsonaKopurua(int pertsonaKopurua) {
        this.pertsonaKopurua = pertsonaKopurua;
    }

    public int getMahaiZenbakia() {
        return mahaiZenbakia;
    }

    public void setMahaiZenbakia(int mahaiZenbakia) {
        this.mahaiZenbakia = mahaiZenbakia;
    }

    public boolean isKantzelatuta() {
        return kantzelatuta;
    }

    public void setKantzelatuta(boolean kantzelatuta) {
        this.kantzelatuta = kantzelatuta;
    }

    public java.sql.Date getUpdateData() {
        return (java.sql.Date) updateData;
    }

    public void setUpdateData(Date updateData) {
        this.updateData = updateData;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}

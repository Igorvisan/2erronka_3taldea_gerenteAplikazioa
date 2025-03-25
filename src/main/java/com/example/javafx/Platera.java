package com.example.javafx;

import java.util.Date;

public class Platera {
    private int id;
    private String izena;
    private String deskribapena;
    private String kategoria;
    private int kantitatea;
    private float prezioa;
    private boolean menu;
    private Date createdAt;
    private int createdBy;
    private Date updatedAt;
    private int updatedBy;
    private Date deletedAt;
    private int deletedBy;

    public Platera() {}

    public Platera(String izena, String deskribapena, String kategoria, int kantitatea, float prezioa, boolean menu) {
        this.izena = izena;
        this.deskribapena = deskribapena;
        this.kategoria = kategoria;
        this.kantitatea = kantitatea;
        this.prezioa = prezioa;
        this.menu = menu;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }
    public String getDeskribapena() { return deskribapena; }
    public void setDeskribapena(String describapena) { this.deskribapena = describapena; }
    public String getKategoria() { return kategoria; }
    public void setKategoria(String kategoria) { this.kategoria = kategoria; }
    public void setKantitatea(int kantitatea) { this.kantitatea = kantitatea; }
    public int getKantitatea() { return kantitatea; }
    public float getPrezioa() { return prezioa; }
    public void setPrezioa(float prezioa) { this.prezioa = prezioa; }
    public void setMenu(boolean menu) { this.menu = menu; }
    public boolean getMenu() { return this.menu; }
}

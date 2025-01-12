package com.example.javafx;

public class Langilea {
    private int id;
    private String izena;
    private String email;
    private String pasahitza;
    private String lanPostua;

    public Langilea() {}
    public Langilea(int id, String izena, String email, String lanPostua, String pasahitza) {
        this.id = id;
        this.izena = izena;
        this.email = email;
        this.lanPostua = lanPostua;
        this.pasahitza = pasahitza;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getIzena() {
        return izena;
    }
    public void setIzena(String izena) {
        this.izena = izena;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPasahitza() {
        return pasahitza;
    }
    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }
    public String getLanPostua() {
        return lanPostua;
    }
    public void setLanPostua(String lanPostua) {
        this.lanPostua = lanPostua;
    }
}

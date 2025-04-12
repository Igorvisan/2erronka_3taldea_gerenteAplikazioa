package com.example.javafx;

import java.sql.Date;

public class Langilea {
    private int id;
    private String dni;
    private String izena;
    private String abizena;
    private String email;
    private String pasahitza;
    private String telefonoa;
    private String lanPostua;
    private boolean txatBaimena;
    private Date updateData;
    private String updateBy;

    public Langilea() {}
    public Langilea(int id, String dni, String izena, String abizena, String email, String lanPostua, String pasahitza,
                    String telefonoa, boolean txatBaimena, Date updateData, String updateBy) {
        this.id = id;
        this.dni = dni;  // Ahora está en la posición correcta
        this.izena = izena;  // Ahora está en la posición correcta
        this.abizena = abizena;
        this.email = email;
        this.lanPostua = lanPostua;
        this.pasahitza = pasahitza;
        this.telefonoa = telefonoa;
        this.txatBaimena = txatBaimena;
        this.updateData = updateData;
        this.updateBy = updateBy;
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
    public boolean isTxatBaimena() {
        return txatBaimena;
    }
    public void setTxatBaimena(boolean txatBaimena) {
        this.txatBaimena = txatBaimena;
    }
    public Date getUpdateData() { return updateData; }
    public void setUpdateData(Date updateData) { this.updateData = updateData; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public String getTelefonoa() { return telefonoa; }
    public void setTelefonoa(String telefonoa) { this.telefonoa = telefonoa; }
    public String getAbizena() { return abizena; }
    public void setAbizena(String abizena) { this.abizena = abizena; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

}

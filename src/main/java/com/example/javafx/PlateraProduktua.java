package com.example.javafx;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlateraProduktua {
    private final SimpleStringProperty produktua;
    private final SimpleIntegerProperty kantitatea;

    public PlateraProduktua(String produktua, int kantitatea) {
        this.produktua = new SimpleStringProperty(produktua);
        this.kantitatea = new SimpleIntegerProperty(kantitatea);
    }

    public String getProduktua() {
        return produktua.get();
    }
    public void setProduktua(String produktua) {
        this.produktua.set(produktua);
    }

    public int getKantitatea() {
        return kantitatea.get();
    }
    public void setKantitatea(int kantitatea) {
        this.kantitatea.set(kantitatea);
    }
}

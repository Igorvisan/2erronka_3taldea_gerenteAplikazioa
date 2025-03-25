package com.example.javafx;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductoSeleccionado {
    private final SimpleStringProperty izenaProduktua;
    private final SimpleIntegerProperty produktuKantitatea;

    public ProductoSeleccionado(String izenaProduktua, int produktuKantitatea) {
        this.izenaProduktua = new SimpleStringProperty(izenaProduktua);
        this.produktuKantitatea = new SimpleIntegerProperty(produktuKantitatea);
    }

    public String getIzenaProduktua() {  return izenaProduktua.get(); }
    public void setIzenaProduktua(String izenaProduktua) { this.izenaProduktua.set(izenaProduktua); }
    public int getProduktuKantitatea() {  return produktuKantitatea.get(); }
    public void setProduktuKantitatea(int produktuKantitatea) { this.produktuKantitatea.set(produktuKantitatea); }

}

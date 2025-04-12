package com.example.javafx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LangileaGehituControllerTest {

    @Test
    void langileaGehitu_datuGuztiekin_NewWorkerItzultzenDu(){
        String txatBaimenaSeleccionado = "Bai";
        Boolean txatBaimena = false;
        String izena = "Miquel";
        String email = "mike@gmail.com";
        String pasahitza = "1234";
        String lanPostua = "Camarero";
        String dni = "00000000Z";
        String telefonoa = "612098334";

        assertEquals(2,LangileaGehituController.anadirTrabajador(txatBaimenaSeleccionado, txatBaimena, izena,
                email, pasahitza, lanPostua, dni, telefonoa), "Deberia de haberse añadido el trabajador");

    }

    @Test
    void langileaGehitu_datuakFaltaDira_missingDataItzultzenDu(){
        String txatBaimenaSeleccionado = "Bai";
        Boolean txatBaimena = false;
        String izena = "Miquel";
        String email = "mike@gmail.com";
        String pasahitza = "1234";
        String lanPostua = "Camarero";
        String dni = null;
        String telefonoa = null;

        assertEquals(1,LangileaGehituController.anadirTrabajador(txatBaimenaSeleccionado, txatBaimena, izena,
                email, pasahitza, lanPostua, dni, telefonoa), "Deberia de devolver missing data");
    }

}
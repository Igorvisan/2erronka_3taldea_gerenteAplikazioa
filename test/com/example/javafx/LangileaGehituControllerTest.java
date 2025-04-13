package com.example.javafx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LangileaGehituControllerTest {

    @Test
    void langileaGehitu_datuGuztiekin_NewWorkerItzultzenDu(){
        String txatBaimenaSeleccionado = "Bai";
        Boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
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
        Boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
        String izena = null;
        String email = null;
        String pasahitza = null;
        String lanPostua = null;
        String dni = null;
        String telefonoa = null;

        assertEquals(1,LangileaGehituController.anadirTrabajador(txatBaimenaSeleccionado, txatBaimena, izena,
                email, pasahitza, lanPostua, dni, telefonoa), "Deberia de devolver missing data");
    }
    @Test
    void langileaGehitu_izenaFaltaDa_missingDataItzultzenDu(){
        String txatBaimenaSeleccionado = "Bai";
        Boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
        String izena = null;
        String email = "K";
        String pasahitza = "1234";
        String lanPostua = "Gerentea";
        String dni = "33333333D";
        String telefonoa = "602098764";

        assertEquals(1,LangileaGehituController.anadirTrabajador(txatBaimenaSeleccionado, txatBaimena, izena,
                email, pasahitza, lanPostua, dni, telefonoa), "Deberia de devolver missing data");
    }

    @Test
    void langileaGehitu_emailFaltaDa_missingDataItzultzenDu(){
        String txatBaimenaSeleccionado = "EZ";
        Boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
        String izena = "K";
        String email = null;
        String pasahitza = "1234";
        String lanPostua = "Gerentea";
        String dni = "33333333D";
        String telefonoa = "602098764";

        assertEquals(1,LangileaGehituController.anadirTrabajador(txatBaimenaSeleccionado, txatBaimena, izena,
                email, pasahitza, lanPostua, dni, telefonoa), "Deberia de devolver missing data");
    }

    @Test
    void langileaGehitu_pasahitzaFaltaDa_missingDataItzultzenDu(){
        String txatBaimenaSeleccionado = "EZ";
        Boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
        String izena = "K";
        String email = "K";
        String pasahitza = null;
        String lanPostua = "Gerentea";
        String dni = "33333333D";
        String telefonoa = "602098764";

        assertEquals(1,LangileaGehituController.anadirTrabajador(txatBaimenaSeleccionado, txatBaimena, izena,
                email, pasahitza, lanPostua, dni, telefonoa), "Deberia de devolver missing data");
    }

    @Test
    void langileaGehitu_lanPostuaFaltaDa_missingDataItzultzenDu(){
        String txatBaimenaSeleccionado = "EZ";
        Boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
        String izena = "K";
        String email = "K";
        String pasahitza = "1234";
        String lanPostua = null;
        String dni = "33333333D";
        String telefonoa = "602098764";

        assertEquals(1,LangileaGehituController.anadirTrabajador(txatBaimenaSeleccionado, txatBaimena, izena,
                email, pasahitza, lanPostua, dni, telefonoa), "Deberia de devolver missing data");
    }

    @Test
    void langileaGehitu_dniFaltaDa_missingDataItzultzenDu(){
        String txatBaimenaSeleccionado = "EZ";
        Boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
        String izena = "K";
        String email = "K";
        String pasahitza = "1234";
        String lanPostua = "Gerentea";
        String dni = null;
        String telefonoa = "602098764";

        assertEquals(1,LangileaGehituController.anadirTrabajador(txatBaimenaSeleccionado, txatBaimena, izena,
                email, pasahitza, lanPostua, dni, telefonoa), "Deberia de devolver missing data");
    }

    @Test
    void langileaGehitu_telefonoaFaltaDa_missingDataItzultzenDu(){
        String txatBaimenaSeleccionado = "EZ";
        Boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
        String izena = "K";
        String email = "K";
        String pasahitza = "1234";
        String lanPostua = "Gerentea";
        String dni = "33333333G";
        String telefonoa = null;

        assertEquals(1,LangileaGehituController.anadirTrabajador(txatBaimenaSeleccionado, txatBaimena, izena,
                email, pasahitza, lanPostua, dni, telefonoa), "Deberia de devolver missing data");
    }

}
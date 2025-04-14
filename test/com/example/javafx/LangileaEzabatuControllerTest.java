package com.example.javafx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LangileaEzabatuControllerTest {

    private LangileaEzabatuController controller;

    @BeforeEach
    void setUp() {
        controller = new LangileaEzabatuController();
    }

    @Test
    void langileaEzabatu_baiBotoiaEman_returnsLANGILE_EZABATUTA(){
        boolean baiEzabatu = true;
        int langileId = 22; //Trabajador existente en la db
        if(baiEzabatu){
           LangileaEzabatuController controller = new LangileaEzabatuController();
           assertEquals(controller.LANGILE_EZABATUTA, controller.langielaEzabutu(langileId), "Deberia de devolver langileEzabatua");
        }
    }

    @Test
    void langileaEzabatu_baiBotoiaEman_returnsLANGILE_EZ_EZABATUTA(){
        boolean baiEzabatuta = false;
        int langileId = 0; // trabajador inexistente en la db
        if(baiEzabatuta){
            LangileaEzabatuController controller = new LangileaEzabatuController();
            assertEquals(controller.LANGILE_EZ_EZABATUTA, controller.langielaEzabutu(langileId), "No deberia de poder borrar trabajador");
        }
    }

    @Test
    void langileaEzabatu_ezBotoiaEman_returnsLANGILE_EZ_EZABATUTA(){
        int langileId = 0; // trabajador inexistente en la db

        LangileaEzabatuController controller = new LangileaEzabatuController();
        assertEquals(controller.LANGILE_EZ_EZABATUTA, controller.langielaEzabutu(langileId), "No deberia de poder borrar trabajador");
    }

}
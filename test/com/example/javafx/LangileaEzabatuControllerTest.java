package com.example.javafx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LangileaEzabatuControllerTest {

    @Test
    void langileaEzabatu_baiBotoiaEman_returnsLANGILE_EZABATUTA(){
        boolean baiEzabatu = true;
        int langileId = 17; //Trabajador existente en la db
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

}
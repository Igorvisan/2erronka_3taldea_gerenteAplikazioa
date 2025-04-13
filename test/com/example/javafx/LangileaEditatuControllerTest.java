package com.example.javafx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class LangileaEditatuControllerTest {
    @Test
    void langileaEditatu_datuGuztiekin_returnsTrue() throws IOException {
        int id = 19; //Id existente en la db
        String izena = "Kevin";
        String abizena = "Mayers";
        String dni = "56730087G";
        String email = "Martinez@example.org";
        String pasahitza = "123456";
        String telefonoa = "615876110";
        String lanPostua = "Camarero";
        String txatBaimenaSeleccionado = "Ez";
        boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
        String updateBy = "Jonan";
        Date updateData = Date.valueOf(LocalDate.now());

        LangileaEditatuController controller = new LangileaEditatuController();

        assertEquals(controller.LANGILE_AlDATUA, controller.langileaEditatu(id, dni, izena, abizena, email, lanPostua, pasahitza,
                telefonoa, txatBaimena, updateData, updateBy), "El trabajo deberia haberse editado sin problemas");
    }

    @Test
    void langileaEditatu_idInexistente_returnsFalse() throws IOException {
        // ID que no existe en la base de datos
        int id = -1;
        String izena = "Test";
        String abizena = "Usuario";
        String dni = "12345678Z";
        String email = "test@example.com";
        String pasahitza = "password";
        String telefonoa = "600000000";
        String lanPostua = "Camarero";
        String txatBaimenaSeleccionado = "Ez";
        boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
        String updateBy = "Test";
        Date updateData = Date.valueOf(LocalDate.now());

        LangileaEditatuController controller = new LangileaEditatuController();

        assertEquals(controller.LANGILE_EZ_ALDATUA, controller.langileaEditatu(id, dni, izena, abizena, email,
                        lanPostua, pasahitza, telefonoa, txatBaimena, updateData, updateBy),
                "El trabajador con ID inexistente no debería editarse");
    }

    @Test
    void langileaEditatu_txatBaimenaTrue_returnsTrue() throws IOException {
        // Prueba específica para asegurar que el valor de txatBaimena se guarda correctamente cuando es true
        int id = 19;
        String izena = "Kevin";
        String abizena = "Mayers";
        String dni = "56730087G";
        String email = "Martinez@example.org";
        String pasahitza = "123456";
        String telefonoa = "615876110";
        String lanPostua = "Camarero";
        boolean txatBaimena = true; // Valor explícito true
        String updateBy = "Jonan";
        Date updateData = Date.valueOf(LocalDate.now());

        LangileaEditatuController controller = new LangileaEditatuController();

        assertEquals(controller.LANGILE_AlDATUA, controller.langileaEditatu(id, dni, izena, abizena, email,
                        lanPostua, pasahitza, telefonoa, txatBaimena, updateData, updateBy),
                "El trabajador debería editarse con txatBaimena=true");

        // Verificar que el valor de txatBaimena se ha guardado correctamente
        Langilea langileaActualizado = LangileaDbKudeaketa.langileaLortuIzenaBidez(izena);
        assertNotNull(langileaActualizado, "El trabajador actualizado debería existir");
        assertTrue(langileaActualizado.isTxatBaimena(), "txatBaimena debería ser true");
    }

    @Test
    void langileaEditatu_camposVacios_returnsError() throws IOException {
        // Prueba con campos obligatorios vacíos
        int id = 19;
        String izena = null; // Vacío
        String abizena = null;
        String dni = "56730087G";
        String email = "Martinez@example.org";
        String pasahitza = "123456";
        String telefonoa = "615876110";
        String lanPostua = "Camarero";
        String txatBaimenaSeleccionado = "Ez";
        boolean txatBaimena = txatBaimenaSeleccionado.equals("Bai") ? true : false;
        String updateBy = "Jonan";
        Date updateData = Date.valueOf(LocalDate.now());

        LangileaEditatuController controller = new LangileaEditatuController();

        assertEquals(controller.LANGILE_EZ_ALDATUA, controller.langileaEditatu(id, dni, izena, abizena, email,
                        lanPostua, pasahitza, telefonoa, txatBaimena, updateData, updateBy),
                "No debería permitir campos obligatorios vacíos");
    }
}
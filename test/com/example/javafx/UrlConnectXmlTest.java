package com.example.javafx;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class UrlConnectXmlTest {

    private String originalUrl;

    @BeforeEach
    void setup() {
        // Guardar la URL original
        originalUrl = UrlConnectXml.GLOBAL_VARIABLE;
    }

    @AfterEach
    void tearDown() {
        // Restaurar la URL original después de cada prueba
        UrlConnectXml.GLOBAL_VARIABLE = originalUrl;
    }

    @Test
    void getDocumentXml_validUrl_retursValidUrl(){
        UrlConnectXml urlConnectXml = new UrlConnectXml();

        Document xml = urlConnectXml.getXmlDocument();

        assertNotNull(xml, "El documento no deberia ser null");
    }

    @Test
    void getXmlDocument2_invalidUrl_returnsNull() {
        // Guardamos el valor anterior
        String oldGlobalVariable = UrlConnectXml.GLOBAL_VARIABLE;

        // Establecemos una URL inválida
        UrlConnectXml.GLOBAL_VARIABLE = "https://url-invalida.xml";

        UrlConnectXml urlConnectXml = new UrlConnectXml();

        Document xml = null;
        Exception thrownException = null;

        xml = urlConnectXml.getXmlDocument();

        // Comprobamos que el documento es nulo
        assertNull(xml, "El documento XML debería ser nulo con la URL inválida");

        // Restauramos el valor original
        UrlConnectXml.GLOBAL_VARIABLE = oldGlobalVariable;
    }

    @Test
    void getXmlDocument_emptyUrl_returnsNull() {
        // URL vacía
        UrlConnectXml.GLOBAL_VARIABLE = "";

        UrlConnectXml urlConnectXml = new UrlConnectXml();

        Document xml = urlConnectXml.getXmlDocument();

        assertNull(xml, "El documento XML debería ser nulo con URL vacía");
    }

    @Test
    void getXmlDocument_nonXmlContent_returnsNull() {
        // URL que no devuelve contenido XML
        UrlConnectXml.GLOBAL_VARIABLE = "https://www.google.com";

        UrlConnectXml urlConnectXml = new UrlConnectXml();

        Document xml = urlConnectXml.getXmlDocument();

        assertNull(xml, "El documento XML debería ser nulo con contenido que no es XML");
    }

}
package com.example.javafx;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.junit.jupiter.api.Assertions.*;

class UrlConnectXmlTest {
    @Test
    void testGetDocumentXml(){
        UrlConnectXml urlConnectXml = new UrlConnectXml();

        Document xml = urlConnectXml.getXmlDocument();

        assertNotNull(xml, "El documento no deberia ser null");
    }

    @Test
    void testgetXmlDocument2_retursInvalidURL(){
        UrlConnectXml.GLOBAL_VARIABLE = "https://url-invalida.xml";

        UrlConnectXml urlConnectXml = new UrlConnectXml();

        Document xml = urlConnectXml.getXmlDocument();

        assertNull(xml, "El documento XML deberia de ser nulo con la URL invalida");
    }
}
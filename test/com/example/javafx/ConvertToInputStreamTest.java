package com.example.javafx;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ConvertToInputStreamTest {
    @Test
    void convertToInputStreamTest1_newInputStream() throws Exception {
        DocumentBuilderFactory fakeDocument = DocumentBuilderFactory.newInstance();
        DocumentBuilder fakeDocumentBuilder = fakeDocument.newDocumentBuilder();
        Document mockedDocument = fakeDocumentBuilder.newDocument();

        Element rootElement = mockedDocument.createElement("elementoRaizFalso");
        mockedDocument.appendChild(rootElement);
        InputStream mockInputStream = ConvertToInputStream.convertXmlDocument(mockedDocument);

        assertNotNull(mockInputStream, "El stream debe tener el InputStream");
    }

    @Test
    void convertToInputStreamTest2_WithNullDocument() throws Exception {
        InputStream mockInputStream = ConvertToInputStream.convertXmlDocument(null);
         assertNull(mockInputStream, "Deberia de ser null el InputStream");
    }
}
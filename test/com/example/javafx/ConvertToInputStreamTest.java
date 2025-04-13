package com.example.javafx;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void convertToInputStreamTest3_WithEmptyValidDocument() throws Exception {
        // Crear un documento XML vacío pero válido (solo tiene elemento raíz)
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document emptyDocument = builder.newDocument();

        // Añadir un elemento raíz vacío
        Element rootElement = emptyDocument.createElement("raizVacia");
        emptyDocument.appendChild(rootElement);

        // Convertir a InputStream
        InputStream resultStream = ConvertToInputStream.convertXmlDocument(emptyDocument);

        // Verificar que se ha creado el InputStream correctamente
        assertNotNull(resultStream, "El InputStream no debería ser null para un documento XML vacío válido");

        // Verificar que el contenido del InputStream es un XML válido
        DocumentBuilder parserBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document parsedDocument = parserBuilder.parse(resultStream);

        // Comprobar que tiene un elemento raíz con el nombre correcto
        assertNotNull(parsedDocument.getDocumentElement(), "El documento debería tener un elemento raíz");
        assertEquals("raizVacia", parsedDocument.getDocumentElement().getTagName(),
                "El nombre del elemento raíz debería ser 'raizVacia'");

        // Verificar que no tiene hijos (está vacío)
        assertFalse(parsedDocument.getDocumentElement().hasChildNodes(),
                "El elemento raíz no debería tener nodos hijos");
    }
}
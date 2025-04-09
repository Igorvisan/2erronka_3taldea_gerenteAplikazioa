package com.example.javafx;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import static org.junit.jupiter.api.Assertions.*;

class XPathTransformerTest {
    //Con este text verificaremoos si la clase XPathTransformer funciona como deberia de funcionar
    @Test
    void XpathTransformerTest1_returnsNewDoc() throws Exception {
        //Creamos el nuevo documento XML
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document mockDocument = dBuilder.newDocument();

        //Creamos un nodo root para el XML
        Element rootElement = mockDocument.createElement("prediccion");
        mockDocument.appendChild(rootElement);
        //Añadimos mas datos dentro del nuevo documento
        Element dia = mockDocument.createElement("dia");
        Element Temperatura = mockDocument.createElement("temperatura");
        Temperatura.setTextContent("25");
        rootElement.appendChild(dia);
        dia.appendChild(Temperatura);

        //Creamos un XPath falso
        String mockXpath = "//temperatura";

        //Ejecutamos el metodo de la clase que nos devolvera el documento nuevo con el XPath aplicado
        Document mockXMLDocument = XPathTransformer.applyXPath(mockDocument, mockXpath);
        assertNotNull(mockXMLDocument, "El nuevo document no puede ser nulo");
        NodeList nodos = mockXMLDocument.getElementsByTagName("temperatura");
        assertEquals(1, nodos.getLength(), "Deberia de haber un nodo temperatura");
        assertEquals("25", nodos.item(0).getTextContent(), "Dentro del nodo temperatura debe de haber 25");
    }
    @Test
    void XpathTransformerTest2_NoMatchingNodes() throws Exception {
        //Creamos el nuevo documento XML
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document falseDocument = dBuilder.newDocument();

        //Creamos un nuveo elemento raiz para el documento
        Element rootElement = falseDocument.createElement("prediccion");
        falseDocument.appendChild(rootElement);

        //Crearemos mas nodo para hacerlo mas realista
        Element dia = falseDocument.createElement("dia");
        Element Temperatura = falseDocument.createElement("temperatura");
        Temperatura.setTextContent("25");
        rootElement.appendChild(dia);
        dia.appendChild(Temperatura);
        //Usaremos un XPath que no coincida con nigun nodo
        String mockXpath = "//nodo-no-coincidente";

        Document mockXMLDocument = XPathTransformer.applyXPath(falseDocument, mockXpath);
        assertNotNull(mockXMLDocument, "El documento no es nulo pero el nodo no coincidente");
        // Verificar que el documento tiene un elemento raíz llamado 'tiempo'
        Element root = mockXMLDocument.getDocumentElement();
        assertEquals("tiempo", root.getTagName(), "El elemento raíz debería llamarse 'tiempo'");

        // Verificar que el elemento raíz no tiene nodos hijos
        assertEquals(0, root.getChildNodes().getLength(), "El elemento raíz no debería tener nodos hijos");
    }

    @Test
    void XpathTransformerTest3_WithNullDocument() throws Exception {
        String mockXpath = "//temperatura";

        Document mockXMLDocument = XPathTransformer.applyXPath(null, mockXpath);

        assertNull(mockXMLDocument, "El documento resultante debe de ser nulo");
    }

    @Test
    void XpathTransformerTest4_InvaliXPath() throws Exception {
        //Construiremos un nuevo documento XML
        DocumentBuilderFactory falseBuildFactury = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = falseBuildFactury.newDocumentBuilder();
        Document falseDocument = dBuilder.newDocument();

        Element rootElement = falseDocument.createElement("prediccion");
        falseDocument.appendChild(rootElement);
        Element dia = falseDocument.createElement("dia");
        Element Temperatura = falseDocument.createElement("temperatura");
        Temperatura.setTextContent("25");
        rootElement.appendChild(dia);
        dia.appendChild(Temperatura);

        String mockXpath = "///temperatura";
        Document mockXMLDocument = XPathTransformer.applyXPath(falseDocument, mockXpath);

        assertNull(mockXMLDocument, "El nuevo document no puede ser nulo");
    }
}
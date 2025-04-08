package com.example.javafx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class XPathTransformer {
    public static Document applyXPath(Document xmlDocument, String XPath) {
        try{
            //Creamos el nuevo documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document newDoc = dBuilder.newDocument();

            //Creamos el elemento raiz
            Element rootElement = newDoc.createElement("tiempo");
            newDoc.appendChild(rootElement);

            //Creamos el XPath
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();

            XPathExpression expr = xpath.compile(XPath);
            NodeList nodes = (NodeList) expr.evaluate(xmlDocument, XPathConstants.NODESET);

            // Añadir los nodos al nuevo documento
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                Node newNode = newDoc.importNode(node, true); // Importar el nodo al nuevo documento
                rootElement.appendChild(newNode);
            }
            System.out.println("Archivo XML creado exitosamente");
            //Devulvemos el documento
            return newDoc;
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}

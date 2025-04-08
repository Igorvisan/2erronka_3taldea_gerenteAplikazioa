package com.example.javafx;

import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ConvertToInputStream {
    public static InputStream convertXmlDocument(Document xmlDocument) {
        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDocument);
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(source, result);

            return new ByteArrayInputStream(outputStream.toByteArray());
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}

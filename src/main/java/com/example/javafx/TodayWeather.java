package com.example.javafx;

import javafx.scene.control.Alert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TodayWeather {
    public static void mostrarTodayWeather(Document XMLDocument) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate today = LocalDate.now();

        String todayDate = today.format(formatter);
        String mesnajePrediciion = null;

        if(XMLDocument == null) {
            String titulua = "Errorea";
            String mezuLuzea = "Ez dago documenturik";
            showAlert(titulua, mezuLuzea);
        }

        try{
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();

            String XpathExpresion = "//dia[@fecha='" + todayDate + "']";

            Node diaNode = (Node) xpath.evaluate(XpathExpresion, XMLDocument, XPathConstants.NODE);

            if(diaNode != null) {
                // Intentar diferentes períodos para el estado del cielo
                String[] periodos = {"00-24", "00-12", "12-24", "00-06", "06-12", "12-18", "18-24"};
                String resultado = null;

                for (String periodo : periodos) {
                    String xpathEstadoCielo = "estado_cielo[@periodo='" + periodo + "']/@descripcion";
                    resultado = (String) xpath.evaluate(xpathEstadoCielo, diaNode, XPathConstants.STRING);
                    if (resultado != null && !resultado.isEmpty()) {
                        break;
                    }
                }

                if (resultado == null || resultado.isEmpty()) {
                    resultado = (String) xpath.evaluate("estado_cielo[1]/@descripcion", diaNode, XPathConstants.STRING);
                }

                if(resultado != null && !resultado.isEmpty()){
                    if(resultado.toLowerCase().contains("lluvia")){
                        String titulua = "Errorea";
                        String mezuLuzea = "!ALERTA! Gaur euria egingo du, beraz ez dago terrazarik";
                        showAlert(titulua, mezuLuzea);
                    } else {
                        String titulua = "Alerta";
                        String mezuLuzea = "!BIKAIN! Gaur ez du euririk egingo, terraza jarri";
                        showAlert(titulua, mezuLuzea);
                    }
                    System.out.println(mesnajePrediciion);
                } else {
                    String titulua = "Errorea";
                    String mezuLuzea = "No se ha encontrado información sobre el estado del cielo";
                    showAlert(titulua, mezuLuzea);
                }
            } else {
                String titulua = "Errorea";
                String mezuLuzea = "No se encontró información para la fecha: " + todayDate;;
                showAlert(titulua, mezuLuzea);
            }
        } catch (XPathExpressionException e) {
            String titulua = "Errorea";
            String mezuLuzea = "Error al procesar la expresión XPath: " + e.getMessage();
            showAlert(titulua, mezuLuzea);
        }

    }

    private static void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

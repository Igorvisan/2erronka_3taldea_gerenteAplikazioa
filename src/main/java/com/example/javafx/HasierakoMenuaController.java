package com.example.javafx;

import com.google.api.services.drive.Drive;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.commons.net.ftp.FTPClient;
import org.w3c.dom.Document;

import javax.swing.event.DocumentEvent;
import java.io.IOException;
import java.io.InputStream;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;

public class HasierakoMenuaController extends BaseController {
    @FXML
    private Label erabiltzailea;

    private Stage usingStage;

    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }
    public String getErabiltzailea() {
        return erabiltzailea.getText();
    }
    @FXML
    protected void onLangileakKudeatuBotoiaClick() throws IOException {
        String erab = erabiltzailea.getText();

        FXMLLoader langileMenua = new FXMLLoader(App.class.getResource("langileMenua.fxml"));
        Scene scene = new Scene(langileMenua.load());
        LangileMenuaController lmc = langileMenua.getController();
        Stage usingStage = this.getUsingStage();
        lmc.setErabiltzailea(erab);
        lmc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Langile Menua");
        usingStage.show();
    }

    public void onMahaiakKudeatuBotoiaClick(ActionEvent actionEvent) throws IOException {
        String erab = erabiltzailea.getText();

        FXMLLoader mahaiaMenua = new FXMLLoader(App.class.getResource("mahaiaMenua.fxml"));
        Scene scene = new Scene(mahaiaMenua.load());
        MahaiaMenuaController mmc = mahaiaMenua.getController();
        Stage usingStage = this.getUsingStage();
        mmc.setErabiltzailea(erab);
        mmc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Langile Menua");
        usingStage.show();
    }

    public void onPlaterakKudeatuBotoiaClick(ActionEvent actionEvent) throws IOException {
        String erab = erabiltzailea.getText();

        FXMLLoader plateraMenua = new FXMLLoader(App.class.getResource("plateraMenua.fxml"));
        Scene scene = new Scene(plateraMenua.load());
        PlateraMenuaController plmc = plateraMenua.getController();
        Stage usingStage = this.getUsingStage();
        plmc.setErabiltzailea(erab);
        plmc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Platera Menua");
        usingStage.show();
    }

    public void onErreserbakKudeatuBotoiaClick(ActionEvent actionEvent) throws IOException {
        String erab = erabiltzailea.getText();

        FXMLLoader erreserbaMenua = new FXMLLoader(App.class.getResource("erreserbaMenua.fxml"));
        Scene scene = new Scene(erreserbaMenua.load());
        ErreserbaMenuaController emc = erreserbaMenua.getController();
        Stage usingStage = this.getUsingStage();
        emc.setErabiltzailea(erab);
        emc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Erreserba Menua");
        usingStage.show();
    }

    public void onTxataBotoiaClick(ActionEvent actionEvent) throws IOException {
        String erab = erabiltzailea.getText();

        // Obtener el Langilea a partir del nombre (erabiltzailea)
        Langilea langilea = LangileaDbKudeaketa.langileaLortuIzenaBidez(erab);

        if (langilea != null && langilea.isTxatBaimena()) {
            // Si tiene permiso para el chat
            FXMLLoader txata = new FXMLLoader(getClass().getResource("txata.fxml"));
            Scene scene = new Scene(txata.load());
            TxatController tc = txata.getController();  // Obtener el controlador
            Stage usingStage = this.getUsingStage();
            tc.setErabiltzailea(erab);
            tc.setUsingStage(usingStage);
            usingStage.setScene(scene);
            usingStage.setTitle("Erreserba Menua");
            usingStage.show();
        } else {
            // Si no tiene permiso para el chat
            String izenaError = "Errorea";
            String mezuLuzeaError = "Ez duzu txat-erako baimenik.";
            mezuaPantailaratu(izenaError, mezuLuzeaError, Alert.AlertType.ERROR);
        }
    }


    public void onSaioaItxiBotoiaClick(ActionEvent actionEvent) throws IOException {
        String erab = erabiltzailea.getText();

        FXMLLoader saioaHasi = new FXMLLoader(App.class.getResource("saioaHasi.fxml"));
        Scene scene = new Scene(saioaHasi.load());
        SaioaHasiController shc = saioaHasi.getController();
        Stage usingStage = this.getUsingStage();
        shc.setErabiltzailea(erab);
        shc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Erreserba Menua");
        usingStage.show();
    }

    public void onEguraldiClickButton(ActionEvent actionEvent) throws IOException {
        UrlConnectXml connectXml = new UrlConnectXml();
        Document xmlDocument = connectXml.getXmlDocument();

        if(xmlDocument != null) {
            String XPath = "//prediccion/dia/estado_cielo | //prediccion/dia/viento | //prediccion/dia/temperatura | //prediccion/dia/sens_termica | //prediccion/dia/humedad_relativa";
            Document newXmlDocument = XPathTransformer.applyXPath(xmlDocument, XPath);
            if(newXmlDocument != null) {
                System.out.println("SE HA APLICADO CORRECTAMENTE EL XPATH");
                //AQUI CONTRUIREMOS EL INPUTSTREAM DEL ARCHIVO XML
                InputStream inputStreamXML = ConvertToInputStream.convertXmlDocument(newXmlDocument);

                if(inputStreamXML != null) {
                        //Configuracion FTP
                        String servidor = "192.168.115.154";
                        String user = "ikasleak";
                        String contrasena = "ikasleak";
                        String carpetaRemota = "/Filezilla";
                        String nombreArchivo = "tiempoErronka2.xml";
                        int puerto = 21;
                        // Subir a FTP
                        FtpUploader ftpUploadXml = new FtpUploader();
                        boolean exito = ftpUploadXml.subirArchivo(servidor, puerto, user, contrasena, carpetaRemota, nombreArchivo, inputStreamXML);

                        if(exito) {
                            System.out.println("Se ha guardado el archivo exitosamente");
                        }else{
                            System.out.println("No se ha guardado el archivo como debería");
                        }
                }else{
                    System.out.println("No se ha podido hacer bien el InputStreamXML");
                }
            }else{
                System.out.println("No se ha podido aplicar correctamente el XPATH");
            }
        }else{
            System.out.println("No se ha encontrado ningun documento XML");
        }
    }
}

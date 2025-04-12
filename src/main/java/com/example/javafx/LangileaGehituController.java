package com.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;

public class LangileaGehituController extends BaseController {
    @FXML
    private Label erabiltzailea;
    @FXML
    private TextField izenaField;

    @FXML
    private TextField dniField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telefonoField;

    @FXML
    private ComboBox lanPostuaComboBox;

    @FXML
    private PasswordField pasahitzaField;
    @FXML
    private ComboBox txatBaimenaComboBox;

    public static final int MISSING_DATA = 1;
    public static final int NEW_WORKER = 2;

    @FXML
    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }

    public void onAtzeaBotoiaClick(MouseEvent mouseEvent) throws IOException {
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

    public void onGehituBotoiaClick(ActionEvent actionEvent) throws IOException {
        String izena = izenaField.getText();
        String dni = dniField.getText();
        String email = emailField.getText();
        String telefonoa = telefonoField.getText();
        String pasahitza = pasahitzaField.getText();
        String lanPostua = lanPostuaComboBox.getValue().toString();

        // Verificar la selección del ComboBox para "txat_baimena"
        String txatBaimenaSeleccionado = (String) txatBaimenaComboBox.getSelectionModel().getSelectedItem();
        Boolean txatBaimena = false; // Valor predeterminado (false)

        anadirTrabajador(txatBaimenaSeleccionado, txatBaimena, izena, email, pasahitza, lanPostua, dni, telefonoa);

        // Limpiar los campos después de añadir
        izenaField.clear();
        emailField.clear();
        pasahitzaField.clear();
        lanPostuaComboBox.setValue(null);

        // Redirigir al menú de trabajadores
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

    public static int anadirTrabajador(String txatBaimenaSeleccionado, Boolean txatBaimena, String izena, String email, String pasahitza, String lanPostua, String dni, String telefonoa) {
        // Si la selección es "Bai", asignar true
        if ("Bai".equals(txatBaimenaSeleccionado)) {
            txatBaimena = true;
        }else{
            txatBaimena = false;
        }

        // Verificar si los campos están vacíos
        // Validar campos nulos o vacíos
        if (izena == null || izena.isEmpty() ||
                email == null || email.isEmpty() ||
                pasahitza == null || pasahitza.isEmpty() ||
                lanPostua == null || lanPostua.isEmpty() ||
                dni == null || dni.isEmpty() || // Verificar null y vacío
                telefonoa == null || telefonoa.isEmpty()) { // Verificar null y vacío
            System.out.println("Faltan datos por introducir");
            return MISSING_DATA;
        }

        // Crear el nuevo trabajador
        Langilea langileBerria = new Langilea();
        langileBerria.setIzena(izena);
        langileBerria.setDni(dni);
        langileBerria.setEmail(email);
        langileBerria.setTelefonoa(telefonoa);
        langileBerria.setPasahitza(pasahitza);
        langileBerria.setLanPostua(lanPostua);
        langileBerria.setTxatBaimena(txatBaimena); // Establecer el valor de txat_baimena
        // Llamada a la base de datos para agregar el trabajador
        LangileaDbKudeaketa.langileaGehitu(langileBerria);

        return NEW_WORKER;
    }

}

package com.example.javafx;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LangileaEditatuController extends BaseController {

    @FXML
    private Label erabiltzailea;

    @FXML
    private TableView<Langilea> langileTaula;

    @FXML
    private TableColumn<Langilea, String> izenaColumn;

    @FXML
    private TableColumn<Langilea, String> emailColumn;

    @FXML
    private TableColumn<Langilea, String> lanPostuaColumn;
    @FXML
    private TableColumn<Langilea, String> pasahitzaColumn;

    @FXML
    private TextField izenaField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField lanPostuaField;

    @FXML
    private TextField pasahitzaField;

    private Langilea aukeratutakoa;

    @FXML
    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }

    public void onEditatuBotoiaClick(ActionEvent actionEvent) throws IOException {
        String izena = izenaField.getText();
        String email = emailField.getText();
        String lanPostua = lanPostuaField.getText();
        String pasahitza = pasahitzaField.getText();


        int id = langileTaula.getSelectionModel().getSelectedItem().getId();


        Langilea langileEditatua = new Langilea(id, izena, email, lanPostua, pasahitza);


        boolean editatuta = LangileaDbKudeaketa.editatuLangilea(langileEditatua);


        if (editatuta) {
            // Alerta de éxito utilizando la clase FuntzioLaguntzaileak
            FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Zuzen editatu da",
                    "Langilearen datuak editatu dira.",
                    Alert.AlertType.INFORMATION
            );

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
        } else {
            // Alerta de error utilizando la clase FuntzioLaguntzaileak
            FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Errorea editatzean",
                    "Errore bat egon da. Berriro saiatu mesedez.",
                    Alert.AlertType.ERROR
            );
        }
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

    @FXML
    public void initialize() {
        ObservableList<Langilea> langileak = LangileaDbKudeaketa.getAllLangileak();

        if (langileak != null) {
            langileTaula.setItems(langileak);
        } else {
            System.err.println("Langilerik ez dago");
        }

        izenaColumn.setCellValueFactory(new PropertyValueFactory<>("izena"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        lanPostuaColumn.setCellValueFactory(new PropertyValueFactory<>("lanPostua"));
        pasahitzaColumn.setCellValueFactory(new PropertyValueFactory<>("Pasahitza"));

        langileTaula.setPrefWidth(300);
        langileTaula.setPrefHeight(150);

        langileTaula.setOnMouseClicked(this::onTableRowClick);
    }

    private void onTableRowClick(MouseEvent event) {
        aukeratutakoa = langileTaula.getSelectionModel().getSelectedItem();

        if (aukeratutakoa != null) {
            izenaField.setText(aukeratutakoa.getIzena());
            emailField.setText(aukeratutakoa.getEmail());
            lanPostuaField.setText(aukeratutakoa.getLanPostua());
            pasahitzaField.setText(aukeratutakoa.getPasahitza());
        } else {
            System.out.println("No se seleccionó ningún elemento en la tabla.");
        }
    }
}

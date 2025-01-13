package com.example.javafx;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class ErreserbaMenuaController extends BaseController {
    @FXML
    private Label erabiltzailea;

    @FXML
    private TableView<Erreserba> erreserbaTaula;

    @FXML
    private TableColumn<Erreserba, String> izenaColumn;
    @FXML
    private TableColumn<Erreserba, Date> dataColumn;
    @FXML
    private TableColumn<Erreserba, Integer> pertsonaColumn;
    @FXML
    private TableColumn<Erreserba, Integer> mahaiaColumn;

    @FXML
    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }

    public void initialize() {

        ObservableList<Erreserba> erreserbak = ErreserbaDbKudeaketa.getAllErreserbak();


        erreserbaTaula.setItems(erreserbak);


        izenaColumn.setCellValueFactory(new PropertyValueFactory<>("erreserbaIzena"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("erreserbaDate"));
        pertsonaColumn.setCellValueFactory(new PropertyValueFactory<>("pertsonaKopurua"));
        mahaiaColumn.setCellValueFactory(new PropertyValueFactory<>("mahiaId"));

        izenaColumn.setPrefWidth(100);
        dataColumn.setPrefWidth(100);
        pertsonaColumn.setPrefWidth(100);
        mahaiaColumn.setPrefWidth(100);

        erreserbaTaula.setPrefWidth(300);
        erreserbaTaula.setPrefHeight(150);

    }

    public void onAtzeaBotoiaClick(MouseEvent mouseEvent) throws IOException {
        String erab = erabiltzailea.getText();

        FXMLLoader hasierakoMenua = new FXMLLoader(App.class.getResource("hasieraMenua.fxml"));
        Scene scene = new Scene(hasierakoMenua.load());
        HasierakoMenuaController hmc = hasierakoMenua.getController();
        Stage usingStage = this.getUsingStage();
        hmc.setErabiltzailea(erab);
        hmc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Hasierako Menua");
        usingStage.show();
    }

    public void onErreserbaGehituBotoiaClick(ActionEvent actionEvent) {
    }

    public void onErreserbaEzabatuBotoiaClick(ActionEvent actionEvent) {
    }

    public void onErreserbaEditatuBotoiaClick(ActionEvent actionEvent) {
    }
}

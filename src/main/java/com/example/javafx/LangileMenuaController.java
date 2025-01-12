package com.example.javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LangileMenuaController extends BaseController {
    @FXML
    private Label erabiltzailea;

    @FXML
    private TableView<Langilea> tableView;

    @FXML
    private TableColumn<Langilea, String> izenaColumn;
    @FXML
    private TableColumn<Langilea, String> emailColumn;
    @FXML
    private TableColumn<Langilea, String> lanPostuaColumn;

    @FXML
    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }

    public void initialize() {

        ObservableList<Langilea> langileak = LangileaDbKudeaketa.getAllLangileak();


        tableView.setItems(langileak);


        izenaColumn.setCellValueFactory(new PropertyValueFactory<>("izena"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        lanPostuaColumn.setCellValueFactory(new PropertyValueFactory<>("lanPostua"));

        izenaColumn.setPrefWidth(100);
        emailColumn.setPrefWidth(100);
        lanPostuaColumn.setPrefWidth(100);

        tableView.setPrefWidth(300);
        tableView.setPrefHeight(150);

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
}

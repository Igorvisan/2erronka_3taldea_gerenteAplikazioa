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

public class MahaiaMenuaController extends BaseController {
    @FXML
    private Label erabiltzailea;
    @FXML
    private TableView<Mahaia> mahaiaTaula;
    @FXML
    private TableColumn<Mahaia, Integer> idColumn;
    @FXML
    private TableColumn<Mahaia, Integer> komentsalColumn;
    @FXML
    private TableColumn<Mahaia, Boolean> libreColumn;


    @FXML
    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
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

    public void initialize() {

        ObservableList<Mahaia> mahaiak = MahaiaDbKudeaketa.getAllMahaiak();


        mahaiaTaula.setItems(mahaiak);


        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        komentsalColumn.setCellValueFactory(new PropertyValueFactory<>("gehienezkoKopurua"));
        libreColumn.setCellValueFactory(new PropertyValueFactory<>("Libre"));

        idColumn.setPrefWidth(100);
        komentsalColumn.setPrefWidth(100);
        libreColumn.setPrefWidth(100);

        mahaiaTaula.setPrefWidth(300);
        mahaiaTaula.setPrefHeight(150);

    }

    public void onMahaiaGehituBotoiaClick(ActionEvent actionEvent) {
    }

    public void onMahaiaEzabatuBotoiaClick(ActionEvent actionEvent) {
    }

    public void onMahaiaEditatuBotoiaClick(ActionEvent actionEvent) {
    }
}

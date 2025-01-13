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

        if (mahaiak != null && !mahaiak.isEmpty()) {
            System.out.println("Mesas cargadas: ");
            for (Mahaia mahaia : mahaiak) {
                System.out.println("ID: " + mahaia.getId() + ", Comensales: " + mahaia.getGehienezkoKopurua());
            }
            mahaiaTaula.setItems(mahaiak);
        } else {
            System.out.println("No hay mesas disponibles para mostrar.");
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        komentsalColumn.setCellValueFactory(new PropertyValueFactory<>("gehienezkoKopurua"));

        idColumn.setPrefWidth(100);
        komentsalColumn.setPrefWidth(100);

        mahaiaTaula.setPrefWidth(300);
        mahaiaTaula.setPrefHeight(150);
    }


    public void onMahaiaGehituBotoiaClick(ActionEvent actionEvent) throws IOException {
        String erab = erabiltzailea.getText();

        FXMLLoader mahaiaGehitu = new FXMLLoader(App.class.getResource("mahaiaGehitu.fxml"));
        Scene scene = new Scene(mahaiaGehitu.load());
        MahaiaGehituController mgc = mahaiaGehitu.getController();
        Stage usingStage = this.getUsingStage();
        mgc.setErabiltzailea(erab);
        mgc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Mahaia gehitzeko galdetegia");
        usingStage.show();
    }

    public void onMahaiaEzabatuBotoiaClick(ActionEvent actionEvent) {
    }

    public void onMahaiaEditatuBotoiaClick(ActionEvent actionEvent) throws IOException {
        String erab = erabiltzailea.getText();

        FXMLLoader mahaiaEditatu = new FXMLLoader(App.class.getResource("mahaiaEditatu.fxml"));
        Scene scene = new Scene(mahaiaEditatu.load());
        MahaiaEditatuController mec = mahaiaEditatu.getController();
        Stage usingStage = this.getUsingStage();
        mec.setErabiltzailea(erab);
        mec.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Mahaia editatzeko galdetegia");
        usingStage.show();
    }
}

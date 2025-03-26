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

public class PlateraMenuaController extends BaseController {
    @FXML
    private Label erabiltzailea;
    @FXML
    private TableView<Platera> plateraTaula;
    @FXML
    private TableColumn<Platera, String> izena;
    @FXML
    private TableColumn<Platera, String> deskribapena;
    @FXML
    private TableColumn<Platera, String> kategoria;
    @FXML
    private TableColumn<Platera, Integer> kantitatea;
    @FXML
    private TableColumn<Platera, Float> prezioa;
    @FXML
    private TableColumn<Platera, Boolean> menu;

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
        ObservableList<Platera> platerak = PlateraDbKudeaketa.getAllPlaterak();
        if(platerak != null && !platerak.isEmpty()) {
            plateraTaula.setItems(platerak);
        }else{
            System.out.println("No hay platerak que mostrar");
        }
        izena.setCellValueFactory(new PropertyValueFactory<>("izena"));
        deskribapena.setCellValueFactory(new PropertyValueFactory<>("deskribapena"));
        kategoria.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        kantitatea.setCellValueFactory(new PropertyValueFactory<>("kantitatea"));
        prezioa.setCellValueFactory(new PropertyValueFactory<>("prezioa"));
        menu.setCellValueFactory(new PropertyValueFactory<>("menu"));
    }

    public void onPlateraGehituBotoiaClick(ActionEvent actionEvent) throws IOException {
        String erab = erabiltzailea.getText();

        FXMLLoader plateraGehitu = new FXMLLoader(App.class.getResource("plateraGehitu.fxml"));
        Scene scene = new Scene(plateraGehitu.load());
        PlateraGehituController pgc = plateraGehitu.getController();
        Stage usingStage = this.getUsingStage();
        pgc.setErabiltzailea(erab);
        pgc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Platera gehitzeko galdetegia");
        usingStage.show();
    }

    public void onPlateraEditatuBotoiaClick(ActionEvent actionEvent) throws IOException {
        String erab = erabiltzailea.getText();
        FXMLLoader plateraEditatu = new FXMLLoader(App.class.getResource("plateraEditatu.fxml"));
        Scene scene = new Scene(plateraEditatu.load());
        PlateraEditatuController pec = plateraEditatu.getController();
        Stage usingStage = this.getUsingStage();
        pec.setErabiltzailea(erab);
        pec.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Platera editatu");
        usingStage.show();
    }

    public void onPlateraEzabatuBotoiaClick(ActionEvent actionEvent) throws IOException {
        String erab = erabiltzailea.getText();
        FXMLLoader plateraEzabatu = new FXMLLoader(App.class.getResource("plateraEzabatu.fxml"));
        Scene scene = new Scene(plateraEzabatu.load());
        PlateraEzabatuController pec = plateraEzabatu.getController();
        Stage usingStage = this.getUsingStage();
        pec.setErabiltzailea(erab);
        pec.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Platera ezabatu");
        usingStage.show();

    }

}

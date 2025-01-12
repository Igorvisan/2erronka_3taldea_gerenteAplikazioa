package com.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

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
}

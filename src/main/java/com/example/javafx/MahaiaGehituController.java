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

public class MahaiaGehituController extends BaseController {
    @FXML
    private Label erabiltzailea;
    @FXML
    private TextField zenbakiaField;

    @FXML
    private TextField komentsalField;

    @FXML
    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }

    public void onAtzeaBotoiaClick(MouseEvent mouseEvent) throws IOException {
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

    public void onGehituBotoiaClick(ActionEvent actionEvent) throws IOException {
        String zenbakia = zenbakiaField.getText();
        String komentsal = komentsalField.getText();

        // Verificar si los campos están vacíos
        if (zenbakia.isEmpty() || komentsal.isEmpty()) {
            String izenaError = "Errorea";
            String mezuLuzeaError = "Datu guztiak sartu behar dituzu.";
            mezuaPantailaratu(izenaError, mezuLuzeaError, Alert.AlertType.ERROR);
            return;
        }

        // Crear el nuevo trabajador
        Mahaia mahaiBerria = new Mahaia();
        mahaiBerria.setId(Integer.parseInt(zenbakia));
        mahaiBerria.setGehienezkoKopurua(Integer.parseInt(komentsal));

        MahaiaDbKudeaketa.mahaiaGehitu(mahaiBerria);

        // Limpiar los campos después de añadir
        zenbakiaField.clear();
        komentsalField.clear();

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

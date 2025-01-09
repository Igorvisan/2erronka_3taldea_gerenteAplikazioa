package com.example.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HasierakoMenuaController extends BaseController {
    @FXML
    private Label erabiltzailea;

    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }
    public String getErabiltzailea() {
        return erabiltzailea.getText();
    }
}

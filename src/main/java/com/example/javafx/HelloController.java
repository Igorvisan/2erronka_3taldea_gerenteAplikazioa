package com.example.javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class HelloController extends BaseController {
    @FXML
    private Label welcomeText;
    @FXML
    private Label secondText;

    @FXML
    protected void onAyudaButtonClick() throws IOException {

        //Nik sortutakoa
//        JavaHelpDemo javaHelpDemo = new JavaHelpDemo();
//        javaHelpDemo.abrirAyuda();

        //Jaitsitakoa
        JavaHelpExecuter javaHelpExecuter = new JavaHelpExecuter();
        javaHelpExecuter.ponLaAyuda();
    }

}

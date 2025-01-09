package com.example.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader saioaHasi = new FXMLLoader(App.class.getResource("saioaHasi.fxml"));
        Scene scene = new Scene(saioaHasi.load(), 320, 240);

        stage.setTitle("Saioa Hasi");
        stage.setMaximized(true);
        SaioaHasiController shc = saioaHasi.getController();
        stage.setWidth(javafx.stage.Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(javafx.stage.Screen.getPrimary().getVisualBounds().getHeight());


        shc.setUsingStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
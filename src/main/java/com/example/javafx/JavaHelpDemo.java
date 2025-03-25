package com.example.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.web.WebView;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import java.net.URL;

public class JavaHelpDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button helpButton = new Button("Abrir Ayuda");

        helpButton.setOnAction(event -> abrirAyudaGrande());

        StackPane root = new StackPane(helpButton);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("JavaHelp con JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void abrirAyuda() {
        Stage helpStage = new Stage();
        WebView webView = new WebView();

        // Cargar archivo HTML de ayuda
        URL helpURL = JavaHelpDemo.class.getResource("/help/html/main.html");
        if (helpURL != null) {
            webView.getEngine().load(helpURL.toString());
        } else {
            System.out.println(" No se encontró el archivo de ayuda HTML.");
        }

        StackPane helpRoot = new StackPane(webView);
        Scene helpScene = new Scene(helpRoot, 600, 400);

        helpStage.setTitle("Ayuda");
        helpStage.setScene(helpScene);
        helpStage.show();
    }

    public void abrirAyudaGrande() {
        try {
            ClassLoader cl = JavaHelpDemo.class.getClassLoader();

            URL hsURL = JavaHelpDemo.class.getResource("/help/help_set.hs");

            if (hsURL == null) {
                System.out.println("No se encontró el archivo HelpSet.");
                return;
            }

            // Crear el HelpSet y HelpBroker
            HelpSet helpSet = new HelpSet(cl, hsURL);
            HelpBroker helpBroker = helpSet.createHelpBroker();


            // Crear ventana de ayuda con WebView (JavaFX)
            Stage helpStage = new Stage();
            WebView webView = new WebView();
            webView.getEngine().load(hsURL.toString()); // Cargar la página de ayuda

            StackPane helpRoot = new StackPane(webView);
            Scene helpScene = new Scene(helpRoot, 600, 400);

            helpStage.setTitle("Ayuda");
            helpStage.setScene(helpScene);
            helpStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


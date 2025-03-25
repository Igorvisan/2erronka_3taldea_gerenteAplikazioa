package com.example.javafx;

import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.JHelp;
import javax.swing.*;
import javax.swing.SwingUtilities;

/**
 * Ejemplo sencillo de uso de JavaHelp. Crea dos ventanas con un men� y les pone
 * la ayuda.
 *
 * @author Chuidiang
 *
 */
public class JavaHelpExecuter {
    /** Ventana secundaria */
    private JDialog secundaria;

    /** Ventana principal */
    private JFrame principal;

    /** Item de men� para la ayuda */
    private JMenuItem itemAyuda;

    /** Boton que despliega la ventana secundaria */
    private JButton botonMuetraSecundaria;

    /**
     * Crea una instanacia de esta clase.
     *
     * @param args
     */
    public static void main(String[] args) {
        new JavaHelpExecuter();
    }

    /**
     * Crea las ventanas, pone la ayuda y visualiza la ventana principal.
     */
    public JavaHelpExecuter() {
    }


    /**
     * Hace que el item del menu y la pulsacion de F1 visualicen la ayuda.
     */
    public void ponLaAyuda() {
        try {
            // Carga el fichero de ayuda
            URL hsURL = JavaHelpDemo.class.getResource("/help/help_set.hs");

            // Crea el HelpSet y el HelpBroker
            HelpSet helpset = new HelpSet(getClass().getClassLoader(), hsURL);
            HelpBroker hb = helpset.createHelpBroker();

            // Crear SwingNode para incrustar JHelp en JavaFX
            SwingNode swingNode = new SwingNode();
            SwingUtilities.invokeLater(() -> {
                JHelp jHelp = new JHelp(helpset);
                swingNode.setContent(jHelp);
            });

            // Crear ventana JavaFX con el SwingNode
            Stage helpStage = new Stage();
            StackPane root = new StackPane(swingNode);
            Scene scene = new Scene(root, 800, 600);

            helpStage.setTitle("Ayuda");
            helpStage.setScene(scene);
            helpStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

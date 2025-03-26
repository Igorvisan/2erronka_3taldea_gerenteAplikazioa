package com.example.javafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;


public class PlateraEzabatuController extends BaseController {
    @FXML
    private Label erabiltzailea;

    @FXML
    private TableView<Platera> platerenTaula;
    @FXML
    private TableColumn<Platera, String> izenaColumn;
    @FXML
    private TableColumn<Platera, String> deskribapenaColumn;
    @FXML
    private TableColumn<Platera, String> kategoriaColumn;
    @FXML
    private TableColumn<Platera, Integer> kantitateaColumn;
    @FXML
    private TableColumn<Platera, Float> prezioaColumn;
    @FXML
    private TableColumn<Platera, Boolean> menuColumn;

    private Langilea usuarioLogeado;

    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }

    public void onAtzeaBotoiaClick(MouseEvent mouseEvent) throws IOException {
        String erab = erabiltzailea.getText();

        FXMLLoader plateraMenua = new FXMLLoader(App.class.getResource("plateraMenua.fxml"));
        Scene scene = new Scene(plateraMenua.load());
        PlateraMenuaController pmc = plateraMenua.getController();
        Stage usingStage = this.getUsingStage();
        pmc.setErabiltzailea(erab);
        pmc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Platera Menua");
        usingStage.show();
    }

    public void initialize() {
        ObservableList<Platera> platerak = PlateraDbKudeaketa.getAllPlaterak();
        if(platerak != null && !platerak.isEmpty()) {
            platerenTaula.setItems(platerak);
        }else{
            System.out.println("No hay platerak que mostrar");
        }
        izenaColumn.setCellValueFactory(new PropertyValueFactory<>("izena"));
        deskribapenaColumn.setCellValueFactory(new PropertyValueFactory<>("deskribapena"));
        kategoriaColumn.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        kantitateaColumn.setCellValueFactory(new PropertyValueFactory<>("kantitatea"));
        prezioaColumn.setCellValueFactory(new PropertyValueFactory<>("prezioa"));
        menuColumn.setCellValueFactory(new PropertyValueFactory<>("menu"));
    }

    @FXML
    public void onEzabatuPlateraClick() {
        // 1. Obtener el plato seleccionado de la tabla
        Platera platoSeleccionado = platerenTaula.getSelectionModel().getSelectedItem();
        if (platoSeleccionado == null) {
            mezuaPantailaratu("Error", "Platera bat aukeratu behar da", Alert.AlertType.WARNING);
            return;
        }

        // 2. Obtener el ID del usuario logueado (Langilea)
        if (erabiltzailea == null) {
            mezuaPantailaratu("Error", "Ez dago langile logueatuta", Alert.AlertType.ERROR);
            return;
        }
        String deletedBy = erabiltzailea.getText(); // Asegúrate de que este ID es el correcto

        // 3. Llamar al método de borrado (borrado lógico)
        PlateraDbKudeaketa.deletePlatera(platoSeleccionado.getId(), deletedBy);

        // 4. Refrescar la tabla (mostrando solo los platos que no tengan deletedAt)
        ObservableList<Platera> platerak = PlateraDbKudeaketa.getAllPlaterak();
        platerenTaula.setItems(platerak);
    }


}

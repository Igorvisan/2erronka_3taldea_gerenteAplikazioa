package com.example.javafx;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class ErreserbaEditatuController extends BaseController {

    @FXML
    private Label erabiltzailea;

    @FXML
    private TableView<Erreserba> erreserbenTaula;

    @FXML
    private TableColumn<Erreserba, String> izenaColumn;

    @FXML
    private TableColumn<Erreserba, String> dataColumn;

    @FXML
    private TableColumn<Erreserba, String> pertsonaColumn;

    @FXML
    private TableColumn<Erreserba, String> mahaiaColumn;

    @FXML
    private TextField izenaField;

    @FXML
    private DatePicker dataField;

    @FXML
    private TextField pertsonaField;

    @FXML
    private TextField mahaiaField;

    private Erreserba aukeratutakoa;

    @FXML
    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }

    public void onAtzeaBotoiaClick(MouseEvent mouseEvent) throws IOException {
        String erab = erabiltzailea.getText();
        FXMLLoader erreserbaMenua = new FXMLLoader(App.class.getResource("erreserbaMenua.fxml"));
        Scene scene = new Scene(erreserbaMenua.load());
        ErreserbaMenuaController emc = erreserbaMenua.getController();
        Stage usingStage = this.getUsingStage();
        emc.setErabiltzailea(erab);
        emc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Erreserba Menua");
        usingStage.show();
    }

    public void onEditatuBotoiaClick(ActionEvent actionEvent) throws IOException {
        String izena = izenaField.getText();
        String dataStr = String.valueOf(dataField.getValue());
        String pertsonaStr = pertsonaField.getText();
        String mahaiaStr = mahaiaField.getText();

        int pertsona, mahaia;
        try {
            pertsona = Integer.parseInt(pertsonaStr);
            mahaia = Integer.parseInt(mahaiaStr);
        } catch (NumberFormatException e) {
            FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Errorea",
                    "Pertsona kopurua eta mahaia zenbakiak izan behar dira.",
                    Alert.AlertType.ERROR
            );
            return;
        }

        LocalDate localDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            localDate = LocalDate.parse(dataStr, formatter);
        } catch (DateTimeParseException e) {
            FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Errorea",
                    "Data formatu egokia ez du.",
                    Alert.AlertType.ERROR
            );
            return;
        }

        java.sql.Date data = java.sql.Date.valueOf(localDate);
        int id = erreserbenTaula.getSelectionModel().getSelectedItem().getId();

        boolean kantzelatuta = false;
        Date updateData = new Date();
        String updatedBy = erabiltzailea.getText();

        Erreserba erreserbaEditatua = new Erreserba(id, izena, data, pertsona, mahaia, kantzelatuta, updateData, updatedBy);

        boolean editatuta = ErreserbaDbKudeaketa.editatuErreserba(erreserbaEditatua);

        if (editatuta) {
            FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Zuzen editatu da",
                    "Erreserbaren datuak editatu dira.",
                    Alert.AlertType.INFORMATION
            );
            onAtzeaBotoiaClick(null);
        } else {
            FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Errorea editatzean",
                    "Errore bat egon da. Berriro saiatu mesedez.",
                    Alert.AlertType.ERROR
            );
        }
    }

    @FXML
    public void initialize() {
        ObservableList<Erreserba> erreserbak = ErreserbaDbKudeaketa.getAllErreserbak();

        erreserbenTaula.setItems(erreserbak);

        izenaColumn.setCellValueFactory(new PropertyValueFactory<>("erreserbaIzena"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("erreserbaDate"));
        pertsonaColumn.setCellValueFactory(new PropertyValueFactory<>("pertsonaKopurua"));
        mahaiaColumn.setCellValueFactory(new PropertyValueFactory<>("mahaiZenbakia"));

        erreserbenTaula.setOnMouseClicked(this::onTableRowClick);
    }

    private void onTableRowClick(MouseEvent event) {
        aukeratutakoa = erreserbenTaula.getSelectionModel().getSelectedItem();

        if (aukeratutakoa != null) {
            izenaField.setText(aukeratutakoa.getErreserbaIzena());

            // Convertir java.sql.Date a LocalDate y establecerlo en el DatePicker
            LocalDate fechaLocal = aukeratutakoa.getErreserbaDate().toLocalDate();
            dataField.setValue(fechaLocal);

            pertsonaField.setText(String.valueOf(aukeratutakoa.getPertsonaKopurua()));
            mahaiaField.setText(String.valueOf(aukeratutakoa.getMahaiZenbakia()));
        }
    }
}

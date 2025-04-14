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
import java.sql.Date;
import java.time.LocalDate;

public class LangileaEditatuController extends BaseController {
    @FXML
    private Label erabiltzailea;
    @FXML
    private TableView<Langilea> langileTaula;
    @FXML
    private TableColumn<Langilea, String> izenaColumn, emailColumn, lanPostuaColumn, pasahitzaColumn;
    @FXML
    private TableColumn<Langilea, Boolean> txatBaimenaColumn;
    @FXML
    private TextField izenaField, emailField, pasahitzaField, abizenaField, dniField, telefonoaField;
    @FXML
    private ComboBox<String> lanPostuaComboBox, txatBaimenaComboBox;

    private Langilea aukeratutakoa;

    @FXML
    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }

    public static final int LANGILE_AlDATUA = 1;
    public static final int LANGILE_EZ_ALDATUA = 2;

    @FXML
    public void initialize() {
        ObservableList<Langilea> langileak = LangileaDbKudeaketa.getAllLangileak();
        langileTaula.setItems(langileak);

        izenaColumn.setCellValueFactory(new PropertyValueFactory<>("izena"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        lanPostuaColumn.setCellValueFactory(new PropertyValueFactory<>("lanPostua"));
        pasahitzaColumn.setCellValueFactory(new PropertyValueFactory<>("pasahitza"));

        // Asignar evento al hacer clic en una fila de la tabla
        langileTaula.setOnMouseClicked(this::onTableRowClick);
    }

    @FXML
    private void onTableRowClick(MouseEvent event) {
        // CORRECCIÓN: Asignar el valor a la variable de clase, no crear una nueva variable local
        this.aukeratutakoa = langileTaula.getSelectionModel().getSelectedItem();

        if (this.aukeratutakoa != null) {
            izenaField.setText(this.aukeratutakoa.getIzena());
            abizenaField.setText(this.aukeratutakoa.getAbizena());
            dniField.setText(this.aukeratutakoa.getDni());
            emailField.setText(this.aukeratutakoa.getEmail());
            lanPostuaComboBox.setValue(this.aukeratutakoa.getLanPostua());
            pasahitzaField.setText(this.aukeratutakoa.getPasahitza());
            telefonoaField.setText(this.aukeratutakoa.getTelefonoa());
            txatBaimenaComboBox.setValue(this.aukeratutakoa.isTxatBaimena() ? "Bai" : "Ez");
        }
    }

    public void onEditatuBotoiaClick(ActionEvent actionEvent) throws IOException {
        // CORRECCIÓN: Verificar si hay una fila seleccionada y actualizar aukeratutakoa si es necesario
        if (langileTaula.getSelectionModel().getSelectedItem() == null) {
            /*FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Ez dago hautatutako langilerik",
                    "Mesedez, hautatu langile bat editatzeko.",
                    Alert.AlertType.WARNING
            );*/
            return;
        }

        // Asegurarse de que aukeratutakoa está actualizado con la selección actual
        if (this.aukeratutakoa == null) {
            this.aukeratutakoa = langileTaula.getSelectionModel().getSelectedItem();
        }

        int id = this.aukeratutakoa.getId();
        String izena = izenaField.getText();
        String abizena = abizenaField.getText();
        String dni = dniField.getText();
        String email = emailField.getText();
        String pasahitza = pasahitzaField.getText();
        String telefonoa = telefonoaField.getText();
        String lanPostua = lanPostuaComboBox.getValue();
        String txatBaimenaSeleccionado = txatBaimenaComboBox.getSelectionModel().getSelectedItem();
        boolean txatBaimena = "Bai".equals(txatBaimenaSeleccionado);
        String updateBy = erabiltzailea.getText();
        Date updateData = Date.valueOf(LocalDate.now());

        // CORRECCIÓN: Ajustar el orden de los parámetros según el constructor de Langilea
        langileaEditatu(id, dni, izena, abizena, email, lanPostua, pasahitza, telefonoa, txatBaimena, updateData, updateBy);
    }

    public int langileaEditatu(int id, String dni, String izena, String abizena, String email, String lanPostua, String pasahitza, String telefonoa, boolean txatBaimena, Date updateData, String updateBy) throws IOException {

        if(izena == null || izena.isEmpty() || abizena == null || abizena.isEmpty()
        || dni == null || dni.isEmpty() || email == null || email.isEmpty()){
            /*FuntzioLaguntzaileak.mezuaPantailaratu("Errorea", "Izena, abizena eta dni minimo", Alert.AlertType.ERROR);*/
            System.out.println("El debe de haber un nombre en la casilla para que se guarde perfectamente los datos");
            return LANGILE_EZ_ALDATUA;
        }
        Langilea langileEditatua = new Langilea(id, dni, izena, abizena, email, lanPostua, pasahitza, telefonoa, txatBaimena, updateData, updateBy);

        boolean editatuta = LangileaDbKudeaketa.editatuLangilea(langileEditatua);

        if (editatuta) {
            /*FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Zuzen editatu da",
                    "Langilearen datuak editatu dira.",
                    Alert.AlertType.INFORMATION
            );
            onAtzeaBotoiaClick(null);*/
            System.out.println("Se han cambiado los datos del trabajador con exito");
            return LANGILE_AlDATUA;
        } else {
            /*FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Errorea editatzean",
                    "Errore bat egon da. Berriro saiatu mesedez.",
                    Alert.AlertType.ERROR
            );*/
            System.out.println("No se han guardado los datos");
            return LANGILE_EZ_ALDATUA;
        }
    }

    public void onAtzeaBotoiaClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader langileMenua = new FXMLLoader(App.class.getResource("langileMenua.fxml"));
        Scene scene = new Scene(langileMenua.load());
        LangileMenuaController lmc = langileMenua.getController();
        Stage usingStage = this.getUsingStage();
        lmc.setErabiltzailea(erabiltzailea.getText());
        lmc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Langile Menua");
        usingStage.show();
    }
}
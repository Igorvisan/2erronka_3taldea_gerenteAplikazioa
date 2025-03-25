package com.example.javafx;

import com.mysql.cj.xdevapi.Table;
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

public class MahaiaEditatuController extends BaseController {
    @FXML
    private Label erabiltzailea;
    @FXML
    private TableView<Mahaia> mahaienTaula;

    @FXML
    private TableColumn<Mahaia, Integer> izenaColumn;

    @FXML
    private TableColumn<Mahaia, Integer> komentsalColumn;
    @FXML
    private TableColumn<Mahaia, Boolean> habilitado;

    @FXML
    private TableColumn<Mahaia, Boolean> libreColumn;


    @FXML
    private TextField izenaField;

    @FXML
    private TextField komentsalField;
    @FXML
    private ComboBox<String> habilitadoSelection;

    private Mahaia aukeratutakoa;

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
        usingStage.setTitle("Mahaien Menua");
        usingStage.show();
    }
    public void onEditatuBotoiaClick(ActionEvent actionEvent) throws IOException {
        // Obtener los valores de los campos de edición
        Integer komentsalKop = Integer.parseInt(komentsalField.getText());
        Integer mahaiZenbakia = Integer.parseInt(izenaField.getText());

        Boolean habilitadoValue = "Bai".equals(habilitadoSelection.getValue());

        // Verificar que se haya seleccionado un elemento de la tabla antes de proceder
        if (mahaienTaula.getSelectionModel().getSelectedItem() == null) {
            // Si no se ha seleccionado ningún elemento en la tabla, muestra una alerta
            FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Ez dago hautatutako mahairik",
                    "Mesedez, hautatu mahai bat editatzeko.",
                    Alert.AlertType.WARNING
            );
            return; // Salir de la función si no se seleccionó nada
        }

        // Obtener el ID del mahai seleccionado
        int zenbakia = mahaienTaula.getSelectionModel().getSelectedItem().getId();

        // Crear un objeto Mahaia con los nuevos datos
        Mahaia mahaiaEditatua = new Mahaia(zenbakia, mahaiZenbakia, komentsalKop, habilitadoValue);

        // Llamar a la base de datos para editar los datos
        boolean editatuta = MahaiaDbKudeaketa.editatuMahaia(mahaiaEditatua);

        if (editatuta) {
            // Alerta de éxito utilizando la clase FuntzioLaguntzaileak
            FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Zuzen editatu da",
                    "Mahaiaren datuak editatu dira.",
                    Alert.AlertType.INFORMATION
            );

            String erab = erabiltzailea.getText();

            FXMLLoader mahaiaMenua = new FXMLLoader(App.class.getResource("mahaiaMenua.fxml"));
            Scene scene = new Scene(mahaiaMenua.load());
            MahaiaMenuaController mmc = mahaiaMenua.getController();
            Stage usingStage = this.getUsingStage();
            mmc.setErabiltzailea(erab);
            mmc.setUsingStage(usingStage);
            usingStage.setScene(scene);
            usingStage.setTitle("Mahaien Menua");
            usingStage.show();
        } else {
            // Alerta de error utilizando la clase FuntzioLaguntzaileak
            FuntzioLaguntzaileak.mezuaPantailaratu(
                    "Errorea editatzean",
                    "Errore bat egon da. Berriro saiatu mesedez.",
                    Alert.AlertType.ERROR
            );
        }
    }

    public void initialize() {
        ObservableList<Mahaia> mahaiak = MahaiaDbKudeaketa.getAllMahaiak();

        habilitadoSelection.getItems().addAll("Bai", "ez");

        if (mahaiak != null && !mahaiak.isEmpty()) {
            mahaienTaula.setItems(mahaiak);
        } else {
            System.err.println("Mahairik ez dago");
        }

        izenaColumn.setCellValueFactory(new PropertyValueFactory<>("mahaiZenbakia"));
        komentsalColumn.setCellValueFactory(new PropertyValueFactory<>("komentsalKopurua"));
        habilitado.setCellValueFactory(new PropertyValueFactory<>("habilitado"));

        habilitado.setCellFactory(column -> new TableCell<Mahaia, Boolean>(){
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if(empty || item == null) {
                    setText(null);
                }else{
                    setText(item ? "Bai" : "ez");
                }
            }
        });

        mahaienTaula.setPrefWidth(300);
        mahaienTaula.setPrefHeight(150);

        mahaienTaula.setOnMouseClicked(this::onTableRowClick);

        // Imprimir datos para ver si se cargan correctamente
        mahaienTaula.getItems().forEach(mahaia -> {
            System.out.println("Mahaia: " + mahaia.getId() + ", " + mahaia.getKomentsalKopurua());
        });
    }



    private void onTableRowClick(MouseEvent event) {
        Mahaia aukeratutakoa = mahaienTaula.getSelectionModel().getSelectedItem();

        if (aukeratutakoa != null) {
            // Imprimir los datos seleccionados
            System.out.println("Datos seleccionados: " +
                    "ID=" + aukeratutakoa.getId() +
                    ", Gehienezko Kopurua=" + aukeratutakoa.getKomentsalKopurua() +
                    ", Habilitado=" + aukeratutakoa.isHabilitado()
            );

            // Establecer los valores en los campos de texto
            izenaField.setText(String.valueOf(aukeratutakoa.getMahaiZenbakia()));
            komentsalField.setText(String.valueOf(aukeratutakoa.getKomentsalKopurua()));

            // Establecer el valor del ComboBox según el valor booleano
            habilitadoSelection.setValue(aukeratutakoa.isHabilitado() ? "Bai" : "Ez");
        } else {
            System.out.println("No se seleccionó ningún elemento en la tabla.");
        }
    }
}

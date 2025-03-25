package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;

public class PlateraGehituController extends BaseController implements Initializable {

    @FXML
    private Label erabiltzailea;

    @FXML
    private TextField izenaField;
    @FXML
    private TextField deskribapenaField;
    @FXML
    private ComboBox kategoriaComboBox;
    @FXML
    private TextField KantitateaField;
    @FXML
    private TextField prezioaField;
    @FXML
    private ComboBox menuComboBox;
    @FXML
    private ComboBox<Produktua> produktuakComboBox;
    @FXML
    private TextField produktuKantitatea;
    @FXML
    private TableView<ProductoSeleccionado> aukeratutakoProduktuak;
    @FXML
    private TableColumn<ProductoSeleccionado, String> produktuZutabea;
    @FXML
    private TableColumn<ProductoSeleccionado, Integer> kantitateZutabea;

    private final ObservableList<ProductoSeleccionado> listaProduktuak = FXCollections.observableArrayList();

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

    public void onGehituBotoiaClick(ActionEvent actionEvent) throws IOException {
        String izena = izenaField.getText();
        String deskribapena = deskribapenaField.getText();
        String kategoria = kategoriaComboBox.getValue().toString();
        int kantitatea = Integer.parseInt(KantitateaField.getText());
        float prezioa = Float.parseFloat(prezioaField.getText());
        //Obtenemos el valor
        String menuSeleccion = menuComboBox.getSelectionModel().getSelectedItem().toString();
        Boolean menuanDago = false;

        if("Bai".equals(menuSeleccion)) {
            menuanDago = true;
        }
        //Verificamos si los fields estan vacios
        if(izena.isEmpty() || deskribapena.isEmpty() || kategoria.isEmpty() || prezioa <= 0) {
            String izenaError = "Errorea";
            String mezuLuzeaError = "Datu guztiak sartu behar dituzu.";
            mezuaPantailaratu(izenaError, mezuLuzeaError, Alert.AlertType.ERROR);
            return;
        }
        //Crear nuevo trabajador
        Platera plateraBerria = new Platera();
        plateraBerria.setIzena(izena);
        plateraBerria.setDeskribapena(deskribapena);
        plateraBerria.setKategoria(kategoria);
        plateraBerria.setKantitatea(kantitatea);
        plateraBerria.setPrezioa(prezioa);
        plateraBerria.setMenu(menuanDago);

        int idPlatera = PlateraDbKudeaketa.plateraGehitu(plateraBerria);

        if(idPlatera > 0){
            for(ProductoSeleccionado ps : listaProduktuak){
                int idProduktua = PlateraDbKudeaketa.getProduktuIzenaBtName(ps.getIzenaProduktua());
                if(idProduktua > 0){
                    PlateraDbKudeaketa.insterPlateraProduktuak(idPlatera, idProduktua, ps.getProduktuKantitatea());
                }
            }
        }
        izenaField.clear();
        deskribapenaField.clear();
        prezioaField.clear();
        KantitateaField.clear();
        menuComboBox.getSelectionModel().clearSelection();

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Cargar productos en el ComboBox
        produktuakComboBox.setItems(PlateraDbKudeaketa.getAllProduktuak());
        // Personalizar cómo se muestra cada elemento en el ComboBox
        produktuakComboBox.setCellFactory(param -> new ListCell<Produktua>() {
            @Override
            protected void updateItem(Produktua item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getIzena());
                }
            }
        });

        // Configurar el convertidor de texto pa mostrar el nombre del producto
        produktuakComboBox.setConverter(new StringConverter<Produktua>() {
            @Override
            public String toString(Produktua produktua) {
                return produktua == null ? null : produktua.getIzena();
            }

            @Override
            public Produktua fromString(String string) {
                return produktuakComboBox.getItems().stream()
                        .filter(produktua -> produktua.getIzena().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        produktuZutabea.setCellValueFactory(new PropertyValueFactory<>("izenaProduktua"));
        kantitateZutabea.setCellValueFactory(new PropertyValueFactory<>("produktuKantitatea"));
        aukeratutakoProduktuak.setItems(listaProduktuak);
    }

    public void onGehituProduktuaClick(ActionEvent event){
        Produktua produkt = produktuakComboBox.getValue();
        if(produkt == null) {
            String izenaError = "Errorea";
            String mezuLuzeaError = "Ez duzu autato produkturik";
            mezuaPantailaratu(izenaError, mezuLuzeaError, Alert.AlertType.ERROR);
            return;
        }
        int kantitatea;
        try{
            kantitatea = Integer.parseInt(produktuKantitatea.getText());
        }catch(NumberFormatException e){
            String izenaError = "Errorea";
            String mezuLuzea = "Ez duzu idatzi produktu kantitatea";
            mezuaPantailaratu(izenaError, mezuLuzea, Alert.AlertType.ERROR);
            return;
        }
        ProductoSeleccionado produktuBerriaAukeratuta = new ProductoSeleccionado(produkt.getIzena(), kantitatea);
        listaProduktuak.add(produktuBerriaAukeratuta);

        // Limpiar el ComboBox y el TextField
        produktuakComboBox.getSelectionModel().clearSelection();
        produktuKantitatea.clear();
    }

    // Método para obtener el producto seleccionado
    public Produktua getSelectedProduktu() {
        return produktuakComboBox.getValue();
    }

}

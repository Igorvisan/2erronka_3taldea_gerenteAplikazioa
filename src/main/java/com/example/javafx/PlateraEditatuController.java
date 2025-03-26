package com.example.javafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;

public class PlateraEditatuController extends BaseController implements Initializable {
    @FXML
    private Label erabiltzailea;
    @FXML
    private ComboBox<Produktua> produktuakComboBox;
    @FXML
    private TextField KantitateaField;
    @FXML
    private ComboBox<Platera> plateraComboBox;
    @FXML
    private TableView<PlateraProduktua> aukeratutakoProduktuak;
    @FXML
    private TableColumn<PlateraProduktua, String> produktuZutabea;
    @FXML
    private TableColumn<PlateraProduktua, Integer> kantitateZutabea;
    @FXML
    private TextField plateraField;
    @FXML
    private TextField deskribapenaField;
    @FXML
    private ComboBox<String> kategoriaComboBox;
    @FXML
    private TextField prezioaField;
    @FXML
    private ComboBox<String> menuComboBox;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Cargar la lista de platos
        ObservableList<Platera> platerak = PlateraDbKudeaketa.getAllPlaterak();
        plateraComboBox.setItems(platerak);

        // 2. Personalizar cómo se muestra cada Platera en el ComboBox
        plateraComboBox.setCellFactory(param -> new ListCell<Platera>() {
            @Override
            protected void updateItem(Platera item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Mostrar solo el nombre del plato
                    setText(item.getIzena());
                }
            }
        });

        // 3. Configurar convertidor de texto para que el ComboBox sepa
        //    qué texto mostrar cuando un Platera está seleccionado
        plateraComboBox.setConverter(new StringConverter<Platera>() {
            @Override
            public String toString(Platera platera) {
                return (platera == null) ? null : platera.getIzena();
            }

            @Override
            public Platera fromString(String string) {
                // Si el usuario escribe manualmente un nombre, aquí
                // podrías buscar el Platera con ese nombre, si quieres.
                // Para la mayoría de casos, simplemente retornamos null
                // o hacemos una búsqueda en la lista:
                return plateraComboBox.getItems().stream()
                        .filter(p -> p.getIzena().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        // Listener para rellenar los campos con los datos del plato seleccionado
        plateraComboBox.valueProperty().addListener((obs, oldPlato, platera) -> {
            if (platera != null) {
                plateraField.setText(platera.getIzena());
                deskribapenaField.setText(platera.getDeskribapena());
                // Suponiendo que la categoría en la clase Platera es un String que coincide con los valores del ComboBox
                kategoriaComboBox.setValue(platera.getKategoria());
                prezioaField.setText(String.valueOf(platera.getPrezioa()));
                // Si en Platera tienes un metodo getMenu() que retorna un boolean:
                menuComboBox.setValue(platera.getMenu() ? "Bai" : "Ez");
            }
        });
        produktuZutabea.setCellValueFactory(new PropertyValueFactory<>("produktua"));
        kantitateZutabea.setCellValueFactory(new PropertyValueFactory<>("kantitatea"));

        produktuakComboBox.setItems(PlateraDbKudeaketa.getAllProduktuak());

        produktuakComboBox.setCellFactory(param -> new ListCell<Produktua>() {
            @Override
            protected void updateItem(Produktua item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                }else{
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

        aukeratutakoProduktuak.setOnMouseClicked(event -> {
            PlateraProduktua seleccion = aukeratutakoProduktuak.getSelectionModel().getSelectedItem();
            if (seleccion != null) {
                // Obtén el nombre del producto (String) de la fila seleccionada.
                String productName = seleccion.getProduktua();
                // Buscar el objeto Produktua en la lista del ComboBox cuyo nombre coincida.
                Produktua producto = produktuakComboBox.getItems().stream()
                        .filter(p -> p.getIzena().equals(productName))
                        .findFirst()
                        .orElse(null);
                if (producto != null) {
                    produktuakComboBox.setValue(producto);
                }
                // Poner la cantidad en el TextField.
                KantitateaField.setText(String.valueOf(seleccion.getKantitatea()));
            }
        });
    }

    @FXML
    public void onPropietateakBilatuClick() {
        // 1. Obtener el plato seleccionado
        Platera seleccionado = plateraComboBox.getValue();
        if (seleccionado == null) {
            // Muestra algún mensaje de error si quieres
            System.out.println("No se ha seleccionado ningún plato");
            return;
        }

        // 2. Con su ID, buscar los productos y cantidades
        int plateraId = seleccionado.getId();  // <- Por eso es importante tener el 'id' en Platera
        ObservableList<PlateraProduktua> productosAsignados = PlateraDbKudeaketa.getProduktuakByPlateraId(plateraId);

        // 3. Cargar esos datos en el TableView
        aukeratutakoProduktuak.setItems(productosAsignados);
    }

    @FXML
    public void onEguneratuPlateraClick() throws IOException {
        // 1) Obtener plato seleccionado
        String erabiltzaileText = erabiltzailea.getText();
        Platera plato = plateraComboBox.getValue();
        if (plato == null) {
            mezuaPantailaratu("Error", "Selecciona un plato", Alert.AlertType.WARNING);
            return;
        }
        int plateraId = plato.getId();

        // 2) Leer los nuevos datos del plato desde los controles de edición
        // (Se asume que tienes estos controles: plateraField, deskribapenaField, kategoriaComboBox, prezioaField y menuComboBox)
        String newName = plateraField.getText();
        String newDesc = deskribapenaField.getText();
        String newCategory = kategoriaComboBox.getValue();
        float newPrice;
        try {
            newPrice = Float.parseFloat(prezioaField.getText());
        } catch (NumberFormatException e) {
            mezuaPantailaratu("Error", "Prezioa inválido", Alert.AlertType.WARNING);
            return;
        }
        boolean newMenu = "Bai".equals(menuComboBox.getValue());
        // Si también deseas actualizar la cantidad del plato (campo kantitatea en platera)
        int newPlaterQuantity;
        try {
            newPlaterQuantity = Integer.parseInt(plato.getKantitatea() + ""); // O si tienes un control dedicado, cámbialo
        } catch (NumberFormatException e) {
            newPlaterQuantity = plato.getKantitatea(); // O asigna un valor por defecto
        }

        // Actualiza el objeto plato con los nuevos datos
        plato.setIzena(newName);
        plato.setDeskribapena(newDesc);
        plato.setKategoria(newCategory);
        plato.setPrezioa(newPrice);
        plato.setMenu(newMenu);
        plato.setKantitatea(newPlaterQuantity); // si corresponde

        // 3) Llamar al método updatePlatera para actualizar la tabla platera
        PlateraDbKudeaketa.updatePlatera(plato, erabiltzaileText);

        // 2) Obtener producto viejo (de la tabla)
        PlateraProduktua seleccion = aukeratutakoProduktuak.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mezuaPantailaratu("Error", "Selecciona un producto en la tabla", Alert.AlertType.WARNING);
            return;
        }
        int oldProductId = PlateraDbKudeaketa.getProduktuIzenaBtName(seleccion.getProduktua());

        // 3) Obtener producto nuevo (del ComboBox)
        Produktua newProduct = produktuakComboBox.getValue();
        if (newProduct == null) {
            mezuaPantailaratu("Error", "Selecciona un nuevo producto", Alert.AlertType.WARNING);
            return;
        }
        // Comprobar el ID del nuevo producto
        System.out.println("Nuevo producto ID: " + newProduct.getId());
        int newProductId = newProduct.getId();

        // 4) Obtener la cantidad nueva
        int nuevaCantidad;
        try {
            nuevaCantidad = Integer.parseInt(KantitateaField.getText());
        } catch (NumberFormatException e) {
            mezuaPantailaratu("Error", "Cantidad inválida", Alert.AlertType.WARNING);
            return;
        }

        // 5) Llamar al método de actualización
        PlateraDbKudeaketa.updatePlateraProduktua(plateraId, oldProductId, newProductId, nuevaCantidad);

        // 6) Refrescar la tabla
        ObservableList<PlateraProduktua> productosAsignados = PlateraDbKudeaketa.getProduktuakByPlateraId(plateraId);
        aukeratutakoProduktuak.setItems(productosAsignados);

        menuComboBox.getSelectionModel().clearSelection();
        plateraField.clear();
        deskribapenaField.clear();
        kategoriaComboBox.getSelectionModel().clearSelection();
        prezioaField.clear();

        String erab = erabiltzailea.getText();

        FXMLLoader platera = new FXMLLoader(App.class.getResource("plateraMenua.fxml"));
        Scene scene = new Scene(platera.load());
        PlateraMenuaController pmc = platera.getController();
        Stage usingStage = this.getUsingStage();
        pmc.setErabiltzailea(erabiltzaileText);
        pmc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Platera Menua");
        usingStage.show();

    }

}

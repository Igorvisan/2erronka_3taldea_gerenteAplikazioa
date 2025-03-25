package com.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.javafx.FuntzioLaguntzaileak.mezuaPantailaratu;

public class ErreserbaGehituController extends BaseController {
    @FXML
    private Label erabiltzailea;
    @FXML
    private TextField izenaField;

    @FXML
    private DatePicker dateErreserbaData;

    @FXML
    private TextField pertsonaField;

    @FXML
    private TextField mahaiaField;

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

    public void onGehituBotoiaClick(ActionEvent actionEvent) {
        String erreserbaIzena = izenaField.getText();
        String dataStr = String.valueOf(dateErreserbaData.getValue());
        String pertsonaStr = pertsonaField.getText();
        String mahaiaStr = mahaiaField.getText();

        if (erreserbaIzena.isEmpty() || dataStr.isEmpty() || pertsonaStr.isEmpty() || mahaiaStr.isEmpty()) {
            mezuaPantailaratu("Errorea", "Datu guztiak sartu behar dituzu.", Alert.AlertType.ERROR);
            return;
        }

        int pertsonaKopurua;
        try {
            pertsonaKopurua = Integer.parseInt(pertsonaStr);
            if (pertsonaKopurua <= 0) {
                mezuaPantailaratu("Errorea", "Pertsona kopurua ezin da 0 edo negatiboa izan.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            mezuaPantailaratu("Errorea", "Pertsona kopurua zenbaki bat izan behar da.", Alert.AlertType.ERROR);
            return;
        }

        int mahaiaId;
        try {
            mahaiaId = Integer.parseInt(mahaiaStr);
        } catch (NumberFormatException e) {
            mezuaPantailaratu("Errorea", "Mahaia ID bat izan behar da.", Alert.AlertType.ERROR);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(dataStr, formatter);
        } catch (Exception e) {
            mezuaPantailaratu("Errorea", "Data ez da egokia. Erabiltzaileak 'yyyy-MM-dd' formatuan sartu behar du.", Alert.AlertType.ERROR);
            return;
        }

        Erreserba erreserbaBerria = new Erreserba();
        erreserbaBerria.setErreserbaIzena(erreserbaIzena);
        erreserbaBerria.setErreserbaDate(Date.valueOf(localDate));
        erreserbaBerria.setPertsonaKopurua(pertsonaKopurua);
        erreserbaBerria.setMahaiZenbakia(mahaiaId);

        boolean ondo = ErreserbaDbKudeaketa.erreserbaGehitu(erreserbaBerria);

        if (ondo) {
            izenaField.clear();
            pertsonaField.clear();
            mahaiaField.clear();
            String erab = erabiltzailea.getText();
            FXMLLoader erreserbaMenua = new FXMLLoader(App.class.getResource("erreserbaMenua.fxml"));
            try {
                Scene scene = new Scene(erreserbaMenua.load());
                ErreserbaMenuaController emc = erreserbaMenua.getController();
                Stage usingStage = this.getUsingStage();
                emc.setErabiltzailea(erab);
                emc.setUsingStage(usingStage);
                usingStage.setScene(scene);
                usingStage.setTitle("Erreserba Menua");
                usingStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                mezuaPantailaratu("Errorea", "Ez da posible menuan sartzea.", Alert.AlertType.ERROR);
            }
        } else {
            mezuaPantailaratu("Errorea", "Ez da erreserba gehitu.", Alert.AlertType.ERROR);
        }
    }
}

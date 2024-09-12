package org.example.kolos_2023_odczytpliku_zapispliku_java_fx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

// Kontroler dla interfejsu użytkownika
public class ServerAppController {

    @FXML
    private Slider radiusSlider;  // Suwak do wyboru promienia filtra
    @FXML
    private Label radiusLabel;  // Etykieta wyświetlająca wartość promienia

    // Inicjalizacja suwaka i etykiety
    @FXML
    public void initialize() {
        radiusSlider.setMin(1);  // Minimalna wartość suwaka
        radiusSlider.setMax(15);  // Maksymalna wartość suwaka
        radiusSlider.setValue(3);  // Początkowa wartość suwaka
        radiusSlider.setBlockIncrement(1);  // Wartość zmiany suwaka przy kliknięciu
        radiusSlider.setShowTickMarks(true);  // Pokazuje oznaczenia na suwaku
        radiusSlider.setShowTickLabels(true);  // Pokazuje etykiety na suwaku
        radiusSlider.setMajorTickUnit(2);  // Duże oznaczenia co 2 jednostki
        radiusSlider.setMinorTickCount(1);  // Małe oznaczenia co jednostkę

        // Ustawienie etykiety z aktualną wartością suwaka
        radiusLabel.setText("Promień: " + (int) radiusSlider.getValue());

        // Aktualizacja etykiety, gdy wartość suwaka się zmienia
        radiusSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                radiusLabel.setText("Promień: " + newVal.intValue()));
    }

    // Metody dostępu do suwaka i etykiety
    public Slider getRadiusSlider() {
        return radiusSlider;
    }

    public Label getRadiusLabel() {
        return radiusLabel;
    }
}
package org.example.kolokwium_2022.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AppController {

    @FXML
    private TextField filterField;

    @FXML
    private ListView<String> wordList;

    @FXML
    private Label wordCountLabel;

    @FXML
    protected void addWord(String word) {
        //Dodanie słowa do ListViev
        wordList.getItems().add(word);

        // Aktualizacja liczby słów
        wordCountLabel.setText(String.valueOf(wordList.getItems().size()));
    }
}
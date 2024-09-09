package org.example.kolokwium_2022.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class AppController {

    @FXML
    private TextField filterField; // Pole tekstowe, w którym użytkownik wpisuje filtr

    @FXML
    private ListView<String> wordList; // Lista słów otrzymanych od serwera

    @FXML
    private Label wordCountLabel; // Etykieta do wyświetlania liczby otrzymanych słów

    // Formatter do zamiany obiektu LocalTime na tekst w formacie HH:mm:ss
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Lista przechowująca wszystkie otrzymane słowa wraz z czasem ich odbioru
    private List<String> recievedWords = new ArrayList<>();

    @FXML
    protected void addWord(String word) {
        // Pobieranie aktualnego czasu w formacie HH:mm:ss
        String currentTime = LocalTime.now().format(formatter);

        // Dodanie słowa do ListView (wcześniejszy pomysł zakomentowany)
//        wordList.getItems().add(currentTime + " " + word);

        // Tworzenie wpisu, który zawiera czas oraz słowo
        String wordWithTime = currentTime + " " + word;
        // Dodanie wpisu do listy otrzymanych słów
        recievedWords.add(wordWithTime);

        // Aktualizacja wyświetlanej listy na podstawie aktualnego filtra
        filterWords(filterField.getText());

        // Aktualizacja liczby wszystkich otrzymanych słów (nieograniczonej filtrem)
//        wordCountLabel.setText(String.valueOf(wordList.getItems().size())); V1 pokazuje ilość słów ograniczoną filtrowaniem
        wordCountLabel.setText(String.valueOf(recievedWords.size())); // Liczba wszystkich słów, niezależnie od filtra
    }

    // Metoda inicjalizująca kontroler, wywoływana automatycznie po załadowaniu FXML
    @FXML
    public void initialize() {
        // Dodanie słuchacza do pola tekstowego, który reaguje na zmiany tekstu
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Wywołanie funkcji filtrowania za każdym razem, gdy użytkownik zmieni tekst
            filterWords(newValue);
        });
    }

    // Metoda do filtrowania słów na podstawie tekstu wprowadzonego w pole filtracji
    private void filterWords(String filter) {
        List<String> filteredWords;

        // Jeśli pole filtra jest puste, wyświetl wszystkie słowa
        if (filter == null || filter.isEmpty()) {
            filteredWords = new ArrayList<>(recievedWords); // Kopiowanie listy
        } else {
            // Filtruj słowa, które zaczynają się od tekstu wprowadzonego w filtr
            filteredWords = recievedWords.stream()
                    .filter(word -> word.split(" ", 2)[1].startsWith(filter)) // Filtruj tylko na podstawie słowa, pomijając czas
                    .collect(Collectors.toList());
        }

        // Sortowanie słów alfabetycznie według ich treści (pomijając czas)
        Collections.sort(filteredWords, Comparator.comparing(word -> word.split(" ", 2)[1]));

        // Aktualizacja listy wyświetlanej w GUI
        wordList.getItems().setAll(filteredWords);

        // Zakomentowana wersja z uwzględnieniem polskich znaków:
//        Collections.sort(filteredWords, Comparator.comparing(word -> normalize(word.split(" ", 2)[1])));
//
//        private String normalize(String input) {
//            // Zamiana polskich znaków na odpowiedniki bez znaków diakrytycznych
//            return input.replaceAll("[ąćęłńóśźż]", "a").replaceAll("[ĄĆĘŁŃÓŚŹŻ]", "A");
//        }
    }
}
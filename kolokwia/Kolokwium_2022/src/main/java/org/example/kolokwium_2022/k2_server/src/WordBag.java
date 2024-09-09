package org.example.kolokwium_2022.k2_server.src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordBag {
    // Lista przechowująca wszystkie słowa
    private List<String> words = new ArrayList<>();
    // Generator liczb losowych do wybierania słów
    private Random rand = new Random();

    // Metoda do wczytywania słów z pliku do listy
    public void populate() {
        // Ścieżka do pliku z listą słów
        String path = "src/main/java/org/example/kolokwium_2022/k2_server/slowa.txt";
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            // Wczytanie wszystkich linii z pliku do listy
            words = stream.collect(Collectors.toList());
        } catch (IOException e) {
            // Obsługa błędów podczas wczytywania pliku
            e.printStackTrace();
        }
    }

    // Metoda do losowego wybierania słowa z listy
    public String get() {
        return words.get(rand.nextInt(words.size())); // Zwracanie losowego słowa
    }
}
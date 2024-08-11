package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ICDCodeTabularOptimizedForTime implements ICDCodeTabular {
    // Mapa przechowująca kody ICD-10 jako klucze i opisy jako wartości
    private Map<String, String> diseases = new HashMap<>();

    // Konstruktor, który inicjalizuje mapę z danymi z pliku
    public ICDCodeTabularOptimizedForTime(String path) {
        try (Stream<String> lines = Files.lines(Path.of(path))) {  // Otwieramy strumień linii z pliku
            diseases = lines.skip(87)  // Pomijamy pierwsze 87 linii (np. nagłówki)
                    .map(String::trim)  // Usuwamy nadmiarowe białe znaki z każdej linii
                    .filter(s -> s.matches("[A-Z]\\d\\d.*"))  // Filtrujemy linie, które zaczynają się od kodu ICD-10 (np. A01.0)
                    .map(s -> s.split(" ", 2))  // Dzielimy linię na dwie części: kod i opis
                    .collect(Collectors.toMap(
                            sp -> sp[0],  // Klucz mapy: kod ICD-10
                            sp -> sp[1].trim(),  // Wartość mapy: opis, usuwamy nadmiarowe białe znaki
                            (existingValue, newValue) -> existingValue));  // W przypadku konfliktu kluczy (jeśli występują duplikaty), zachowujemy istniejącą wartość
        } catch (IOException e) {  // Obsługujemy wyjątek w przypadku problemów z plikiem
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription(String code) throws IndexOutOfBoundsException {
        // Zamiast używania tradycyjnego if, używamy Optional do pobrania opisu z mapy
        String value = Optional.ofNullable(diseases.get(code))  // Pobieramy wartość z mapy lub zwracamy Optional.empty()
                .orElseThrow(() -> new IndexOutOfBoundsException("BAD CODE!"));  // Jeśli wartość jest null, rzucamy wyjątek
        return value;
    }
}

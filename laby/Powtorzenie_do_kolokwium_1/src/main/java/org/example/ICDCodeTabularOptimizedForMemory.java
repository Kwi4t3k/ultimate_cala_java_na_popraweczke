package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ICDCodeTabularOptimizedForMemory implements ICDCodeTabular {
    private String path;  // Ścieżka do pliku z kodami ICD-10

    // Konstruktor, który inicjalizuje ścieżkę do pliku
    public ICDCodeTabularOptimizedForMemory(String path) {
        this.path = path;
    }

    @Override
    public String getDescription(String code) throws IndexOutOfBoundsException {
        try (Stream<String> lines = Files.lines(Path.of(path))) {  // Otwieramy strumień linii z pliku
            return lines.skip(87)  // Pomijamy pierwsze 87 linii (np. nagłówki)
                    .map(String::trim)  // Usuwamy nadmiarowe białe znaki z każdej linii
                    .filter(s -> s.matches("[A-Z]\\d\\d.*"))  // Filtrujemy linie, które zaczynają się od kodu ICD-10 (np. A01.0)
                    .map(s -> s.split(" ", 2))  // Dzielimy linię na dwie części: kod i opis
                    .filter(parts -> parts[0].equals(code))  // Wybieramy tylko te linie, gdzie kod odpowiada podanemu kodowi
                    .findFirst()  // Pobieramy pierwszy (i powinien być jedyny) wynik
                    .map(parts -> parts[1].trim())  // Jeśli znaleziono, zwracamy opis (usuwamy nadmiarowe białe znaki)
                    .orElseThrow(() -> new IndexOutOfBoundsException("Bad CODE !!"));  // Jeśli nie znaleziono kodu, rzucamy wyjątek

        } catch (IOException e) {  // Obsługujemy wyjątek w przypadku problemów z plikiem
            throw new RuntimeException(e);
        }
    }
}

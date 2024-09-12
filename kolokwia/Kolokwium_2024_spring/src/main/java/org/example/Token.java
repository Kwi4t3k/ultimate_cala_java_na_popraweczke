package org.example;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Token {
    private int id; // Identyfikator tokena, unikalny dla każdego obiektu
    private LocalDateTime timeOfCreation; // Czas utworzenia tokena
    private static int actualValueOfToken = 0; // Statyczna zmienna przechowująca aktualną wartość dla nowego tokena
    private static List<Token> tokens = new ArrayList<Token>(); // Lista przechowująca wszystkie utworzone tokeny, która się aktualizuje sama

    // Konstruktor klasy Token, który przypisuje unikalny identyfikator oraz ustawia czas utworzenia
    public Token() {
        // pierwsza wartość id = 0 | inkrementacja będzie po przypisaniu zmiennej id
        id = actualValueOfToken++; // Inkrementacja zmiennej tokena, aby każdy token miał unikalne ID
        timeOfCreation = LocalDateTime.now(); // Ustawienie aktualnego czasu utworzenia tokena
        tokens.add(this); // Dodanie tokena do listy tokenów
    }

    // Getter zwracający ID tokena
    public int getId() {
        return id; // Zwracamy identyfikator tokena
    }

    // Getter zwracający czas utworzenia tokena
    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation; // Zwracamy czas utworzenia tokena
    }

    // Zwraca listę wszystkich utworzonych tokenów
    public static List<Token> getTokens() {
        return tokens; // Zwracamy listę wszystkich tokenów
    }

    // KROK 2 - Sprawdzenie, czy token jest aktywny, tj. czy został utworzony mniej niż 5 minut temu
    public boolean isTokenActive() {
        LocalDateTime now = LocalDateTime.now(); // Pobranie aktualnego czasu
        long elapsedMinutes = ChronoUnit.MINUTES.between(timeOfCreation, now); // Obliczenie różnicy minut między utworzeniem tokena a aktualnym czasem

        return elapsedMinutes < 5; // Zwracamy true, jeśli token został utworzony mniej niż 5 minut temu
    }

    // KROK 8 - Usuwanie tokena z listy tokenów na podstawie ID
    public static void removeToken(int id) {
        // Iterujemy po liście tokenów
        for (Token token : tokens) {
            // Jeśli znajdziemy token o danym ID, usuwamy go
            if (token.getId() == id) {
                tokens.remove(token); // Usunięcie tokena z listy
            }
        }
    }

    // Klasa DTO (Data Transfer Object) do przekazywania tokenów z informacją o ich stanie aktywności
    static class TokenDTO {
        // jest tworzony na podstawie zwykłego tokena | obiekt transportujący dane do tokenów | łatwiej zarządzać isActive | DTO - data transfer objector

        public int id; // Identyfikator tokena
        public LocalDateTime timeOfCreation; // Czas utworzenia tokena
        public boolean isActive; // Informacja, czy token jest aktywny

        // Konstruktor, który tworzy DTO na podstawie obiektu Token
        public TokenDTO(Token token) {
            this.id = token.id; // Przypisanie ID tokena
            this.timeOfCreation = token.timeOfCreation; // Przypisanie czasu utworzenia tokena
            this.isActive = token.isTokenActive(); // Sprawdzenie i przypisanie, czy token jest aktywny
        }
    }
}
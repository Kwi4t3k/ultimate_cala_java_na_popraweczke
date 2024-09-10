package org.example;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Token {
    private int id;
    private LocalDateTime timeOfCreation;
    private static int actualValueOfToken = 0;
    private static List<Token> tokens = new ArrayList<Token>(); // Lista do przechowywania wszystkich tokenów, która się aktualizuje sama

    public Token() {
        id = actualValueOfToken++; // pierwsza wartość id = 0 | inkrementacja będzie po przypisaniu zmiennej id
        timeOfCreation = LocalDateTime.now();
        tokens.add(this);
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    public static List<Token> getTokens() { // Zwraca listę wszystkich tokenów
        return tokens;
    }

    //    KROK 2
    public boolean isTokenActive() {
        LocalDateTime now = LocalDateTime.now();
        long elapsedMinutes = ChronoUnit.MINUTES.between(timeOfCreation, now);

        return elapsedMinutes < 5;
    }

    static class TokenDTO { // jest tworzony na podstawie zwykłego tokena | obiekt transportujący dane do tokenów | łatwiej zarządzać isActive | DTO - data transfer objector
        public int id;
        public LocalDateTime timeOfCreation;
        public boolean isActive; // przekazywanie czy token jest aktywny

        public TokenDTO(Token token) {
            this.id = token.id;
            this.timeOfCreation = token.timeOfCreation;
            this.isActive = token.isTokenActive();
        }
    }
}
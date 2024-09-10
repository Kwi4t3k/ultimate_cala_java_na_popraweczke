package org.example;

import java.time.LocalDateTime;

public class Token {
    public int id;
    public LocalDateTime timeOfCreation;
    private static int actualValueOfToken = 0;

    public Token() {
        id = actualValueOfToken++; // pierwsza wartość id = 0 | inkrementacja będzie po przypisaniu zmiennej id
        timeOfCreation = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }
}
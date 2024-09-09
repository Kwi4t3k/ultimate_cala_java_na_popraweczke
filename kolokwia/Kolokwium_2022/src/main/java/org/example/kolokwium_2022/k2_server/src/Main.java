package org.example.kolokwium_2022.k2_server.src;

public class Main {
    public static void main(String[] args) {
        // Tworzenie obiektu worka słów (WordBag)
        WordBag wordBag = new WordBag();
        // Zapełnienie worka słów z pliku
        wordBag.populate();
        // Tworzenie serwera nasłuchującego na porcie 5000 i przekazanie worka słów
        Server server = new Server(5000, wordBag);
        // Uruchomienie serwera
        server.start();
        // Rozpoczęcie wysyłania słów co 5 sekund
        server.startSending();
    }
}
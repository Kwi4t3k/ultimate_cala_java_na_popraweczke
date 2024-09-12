package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Adnotacja informująca, że jest to główna klasa uruchamiająca aplikację Spring Boot
public class ServerApp {
    public static void main(String[] args) {
        // KROK 6 - Ustawienie obrazu na podstawie pikseli z bazy danych
        ImageRGB.getInstance().setImageBasedOnPixels(); // Pobranie instancji ImageRGB i ustawienie pikseli obrazu

        // KROK 7 - Uruchomienie serwera nasłuchującego na porcie 5000
        Server server = new Server(5000); // Tworzenie nowej instancji serwera
        server.start(); // Uruchomienie serwera w osobnym wątku

        SpringApplication.run(ServerApp.class, args); // Uruchomienie aplikacji Spring Boot
    }
}
package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Starting client...");
            // Tworzymy instancję klienta, podając adres serwera (localhost) i port (5000).
            Client client = new Client("localhost", 5000);
            System.out.println("Connected to server.");

            // Uruchamiamy klienta, co rozpoczyna wysyłanie i odbieranie wiadomości.
            client.start();
            System.out.println("Client started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

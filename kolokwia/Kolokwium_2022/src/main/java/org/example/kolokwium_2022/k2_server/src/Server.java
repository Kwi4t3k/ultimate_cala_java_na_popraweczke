package org.example.kolokwium_2022.k2_server.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server extends Thread {
    private ServerSocket serverSocket; // Główne gniazdo serwera
    private List<ClientThread> clients = new ArrayList<>(); // Lista klientów (połączeń)
    private WordBag wordBag; // Obiekt zarządzający słowami

    // Konstruktor serwera, przyjmujący port i worek słów
    public Server(int port, WordBag wordBag) {
        this.wordBag = wordBag;
        try {
            // Inicjalizacja gniazda serwera
            System.out.println("Serwer uruchomiony. Nasłuchiwanie...");
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda do wysyłania słowa do wszystkich klientów co 5 sekund
    public void startSending() {
        Timer timer = new Timer();
        // Zaplanowanie zadania do wysyłania słów co 5 sekund
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                broadcast(wordBag.get()); // Pobranie losowego słowa i wysłanie do klientów
            }
        }, 0, 5000); // Początkowe opóźnienie = 0 ms, co 5000 ms (5 sekund)
    }

    public void run() {
        // Stała pętla nasłuchująca połączeń od klientów
        while (true) {
            Socket clientSocket;
            try {
                // Oczekiwanie na połączenie z klientem
                clientSocket = serverSocket.accept();
                System.out.println("Połączono z klientem");
                // Tworzenie nowego wątku dla klienta
                ClientThread thread = new ClientThread(clientSocket, this);
                clients.add(thread); // Dodanie klienta do listy
                thread.start(); // Uruchomienie wątku klienta
            } catch (IOException e) {
                e.printStackTrace(); // Obsługa wyjątków
            }
        }
    }

    // Usuwanie klienta z listy po rozłączeniu
    public void removeClient(ClientThread client) {
        clients.remove(client);
        System.out.println("removed");
    }

    // Wysyłanie wiadomości do wszystkich podłączonych klientów
    public void broadcast(String message) {
        for (var client : clients)
            client.send(message); // Wysyłanie wiadomości do każdego klienta
    }
}
package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server_zad1_2 {
    // Lista do przechowywania wszystkich połączeń z klientami
    private List<ClientHandler> clients;
    private ServerSocket serverSocket;

    // Konstruktor serwera, inicjalizuje listę klientów i tworzy gniazdo serwera nasłuchujące na porcie 5000
    public Server_zad1_2() throws IOException {
        clients = new ArrayList<>();
        serverSocket = new ServerSocket(5000);
    }

    // Metoda uruchamiająca serwer, która czeka na nowe połączenia i dodaje klientów do listy
    public void listen() {
        System.out.println("Server started");

        while (true) {
            try {
                // Oczekiwanie na połączenie od klienta
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                // Tworzenie nowego obiektu ClientHandler dla nowego klienta
                ClientHandler clientHandler = new ClientHandler(socket, this);

                // Dodanie klienta do listy klientów
                clients.add(clientHandler);

                // Uruchomienie wątku obsługującego klienta
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Metoda do rozsyłania wiadomości do wszystkich klientów z wyjątkiem tego, który wysłał wiadomość
    public synchronized void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    // Metoda usuwająca klienta z listy, gdy zakończy on połączenie
    public synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    // Metoda główna uruchamiająca serwer
    public static void main(String[] args) {
        try {
            Server_zad1_2 server = new Server_zad1_2();
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
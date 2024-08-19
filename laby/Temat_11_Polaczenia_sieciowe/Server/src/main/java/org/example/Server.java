package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    // Zbiór do przechowywania wszystkich połączeń z klientami
    private Set<ClientHandler> clients = new HashSet<>();
    private ServerSocket serverSocket; // Gniazdo serwera

    // Konstruktor serwera, inicjalizuje listę klientów i tworzy gniazdo serwera nasłuchujące na porcie 5000
    public Server() throws IOException {
        serverSocket = new ServerSocket(5000); // Tworzenie gniazda serwera na porcie 5000
    }

    // Metoda uruchamiająca serwer, która czeka na nowe połączenia i dodaje klientów do listy
    public void listen() {
        System.out.println("Server started"); // Informacja o uruchomieniu serwera

        while (true) {
            try {
                // Oczekiwanie na połączenie od klienta
                Socket socket = serverSocket.accept();
                System.out.println("Client connected"); // Informacja o połączeniu klienta

                // Tworzenie nowego obiektu ClientHandler dla nowego klienta
                ClientHandler clientHandler = new ClientHandler(socket, this);

                // Dodanie klienta do zbioru klientów
                clients.add(clientHandler);

                // Uruchomienie wątku obsługującego klienta
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace(); // Obsługa błędów wejścia/wyjścia
            }
        }
    }

    // Metoda do rozsyłania wiadomości do wszystkich klientów z wyjątkiem tego, który wysłał wiadomość
    public synchronized void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) { // Pominięcie klienta-nadawcy
                client.sendMessage(message);
            }
        }
        System.out.println(message); // Wyświetlenie wiadomości na serwerze
    }

    // Metoda do rozsyłania wiadomości do wszystkich klientów
    public synchronized void broadcastToAll(String message){
        for (ClientHandler client : clients){
            client.sendMessage(message); // Wysłanie wiadomości do wszystkich klientów
        }
        System.out.println(message); // Wyświetlenie wiadomości na serwerze
    }

    // Metoda usuwająca klienta z listy, gdy zakończy on połączenie
    public synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler); // Usunięcie klienta ze zbioru
        broadcastToAll(clientHandler.getUserName() + " has left the chat."); // Powiadomienie pozostałych klientów
    }

    // Metoda zwracająca listę aktualnie zalogowanych użytkowników
    public synchronized String getOnlineUsers(){
        StringBuilder usersOnline = new StringBuilder();
        for (ClientHandler client : clients){
            usersOnline.append(client.getUserName()).append(",\n"); // Dodanie każdego użytkownika do listy
        }

        if (usersOnline.length() > 0) {
            usersOnline.setLength(usersOnline.length() - 2); // Usunięcie ostatniego przecinka i nowej linii
        }

        return usersOnline.toString(); // Zwrócenie listy użytkowników jako ciąg znaków
    }

    // Metoda zwracająca klienta na podstawie jego loginu
    public synchronized ClientHandler getClientByUserName(String userName){
        for (ClientHandler client : clients) {
            if (client.getUserName().equals(userName)){ // Sprawdzenie, czy login się zgadza
                return client; // Zwrócenie znalezionego klienta
            }
        }
        return null; // Zwrócenie null, jeśli klient nie został znaleziony
    }

    // Metoda główna uruchamiająca serwer
    public static void main(String[] args) {
        try {
            Server server = new Server(); // Tworzenie instancji serwera
            server.listen(); // Uruchomienie serwera
        } catch (IOException e) {
            e.printStackTrace(); // Obsługa błędów wejścia/wyjścia
        }
    }
}
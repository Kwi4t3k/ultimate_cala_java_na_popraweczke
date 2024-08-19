package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    // Lista do przechowywania wszystkich połączeń z klientami
    private Set<ClientHandler> clients = new HashSet<>();
    private ServerSocket serverSocket;

    // Konstruktor serwera, inicjalizuje listę klientów i tworzy gniazdo serwera nasłuchujące na porcie 5000
    public Server() throws IOException {
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
        System.out.println(message);
    }

    public synchronized void broadcastToAll(String message){
        for (ClientHandler client : clients){
            client.sendMessage(message);
        }
        System.out.println(message);
    }

    // Metoda usuwająca klienta z listy, gdy zakończy on połączenie
    public synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastToAll(clientHandler.getUserName() + " has left the chat.");
    }

    public synchronized String getOnlineUsers(){
        StringBuilder usersOnline = new StringBuilder();
        for (ClientHandler client : clients){
            usersOnline.append(client.getUserName()).append(",\n");
        }

        if (usersOnline.length() > 0) {
            usersOnline.setLength(usersOnline.length() - 2);
        }

        return usersOnline.toString();
    }

    public synchronized ClientHandler getClientByUserName(String userName){
        for (ClientHandler client : clients) {
            if (client.getUserName().equals(userName)){
                return client;
            }
        }
        return null;
    }

    // Metoda główna uruchamiająca serwer
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
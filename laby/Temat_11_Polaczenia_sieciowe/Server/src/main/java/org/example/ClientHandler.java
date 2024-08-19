package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

// Klasa obsługująca pojedynczego klienta
public class ClientHandler implements Runnable {
    private Socket socket;           // Socket do komunikacji z klientem
    private Server server;           // Referencja do obiektu Server, aby móc wysyłać wiadomości do innych klientów
    private PrintWriter out;         // Strumień wyjściowy do wysyłania wiadomości do klienta
    private BufferedReader reader;   // Strumień wejściowy do odbierania wiadomości od klienta
    private String userName;         // Login użytkownika

    // Konstruktor przyjmujący socket klienta i referencję do serwera
    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            // Ustawienie strumienia wejściowego do odbierania wiadomości od klienta
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            // Ustawienie strumienia wyjściowego do wysyłania wiadomości do klienta
            OutputStream output = socket.getOutputStream();
            out = new PrintWriter(output, true);
        } catch (IOException e) {
            e.printStackTrace(); // Obsługa błędów wejścia/wyjścia, np. gdy nie można utworzyć strumieni
        }
    }

    @Override
    public void run() {
        try {
            // Wysłanie wiadomości do klienta z pytaniem o jego login
            sendMessage("What's your name?");

            // Odczytanie loginu od klienta
            userName = reader.readLine();

            // Powiadomienie wszystkich klientów, że nowy użytkownik dołączył do czatu
            server.broadcastToAll(userName + " has joined the chat.");

            String message;

            // Pętla do odczytywania wiadomości od klienta
            while ((message = reader.readLine()) != null) {
                // Sprawdzenie, czy użytkownik chce zobaczyć listę dostępnych użytkowników
                if (message.equals("/online")){
                    sendMessage("Currently online: " + server.getOnlineUsers());
                } else if (message.startsWith("/w")) {
                    // Sprawdzenie, czy użytkownik chce wysłać prywatną wiadomość
                    whisper(message);
                } else {
                    // Przekazanie wiadomości do serwera, aby rozesłał ją do innych klientów
                    server.broadcast(userName + ": " + message, this);
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); // Obsługa błędów wejścia/wyjścia
        } finally {
            // Usunięcie klienta z listy klientów serwera po zakończeniu połączenia
            server.removeClient(this);

            try {
                // Zamknięcie połączenia z klientem
                socket.close();
            } catch (IOException e) {
                e.printStackTrace(); // Obsługa błędów wejścia/wyjścia
            }
        }
    }

    // Metoda do wysyłania wiadomości do klienta
    public void sendMessage(String message) {
        out.println(message); // Wysłanie wiadomości przez strumień wyjściowy
    }

    // Metoda zwracająca login użytkownika
    public String getUserName() {
        return userName;
    }

    // Metoda obsługująca wysyłanie prywatnych wiadomości
    private void whisper(String message){
        // Podział wiadomości na trzy części: "/w", nazwa odbiorcy, treść wiadomości
        String[] parts = message.split(" ", 3);

        // Sprawdzenie, czy wiadomość została podzielona na co najmniej trzy części
        if (parts.length >= 3){
            String recipientName = parts[1]; // Nazwa odbiorcy wiadomości
            String privateMessage = parts[2]; // Treść wiadomości

            // Znalezienie klienta o podanej nazwie
            ClientHandler recipient = server.getClientByUserName(recipientName);

            // Sprawdzenie, czy odbiorca jest zalogowany
            if (recipient != null){
                // Wysłanie prywatnej wiadomości do odbiorcy
                recipient.sendMessage("[Private to " + recipientName + " ]" + privateMessage);
                // Wysłanie kopii wiadomości do nadawcy
                sendMessage("[Private to " + recipientName + "] " + privateMessage);
            } else {
                // Jeśli odbiorca nie jest zalogowany, informujemy o tym nadawcę
                sendMessage("User '" + recipientName + "' is not online.");
            }
        } else {
            // Jeśli format wiadomości jest niepoprawny, informujemy nadawcę
            sendMessage("Invalid command format. Use: /w recipient message");
        }
    }
}
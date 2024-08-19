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
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Pierwsza wiadomość od klienta to jego login
            sendMessage("What's your name?");
            userName = reader.readLine();
            server.broadcastToAll(userName + " has joined the chat.");

            String message;

            // Pętla, która odbiera wiadomości od klienta
            while ((message = reader.readLine()) != null) {
                if (message.equals("/online")){
                    sendMessage("Currently online: " + server.getOnlineUsers());
                } else if (message.startsWith("/w")) {
                    whisper(message);
                } else {
                    // Przekazanie wiadomości do serwera w celu rozesłania do innych klientów
                    server.broadcast(userName + ": " + message, this);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Usunięcie klienta z listy po zakończeniu połączenia
            server.removeClient(this);

            try {
                // Zamknięcie połączenia z klientem
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Metoda do wysyłania wiadomości do klienta
    public void sendMessage(String message) {
        out.println(message);
    }

    // Metoda zwracająca login użytkownika
    public String getUserName() {
        return userName;
    }

    private void whisper(String message){
        String[] parts = message.split(" ", 3);

        if (parts.length >= 3){
            String recipientName = parts[1];
            String privateMessage = parts[2];

            ClientHandler recipient = server.getClientByUserName(recipientName);

            if (recipient != null){
                recipient.sendMessage("[Private to " + recipientName + " ]" + privateMessage);
                sendMessage("[Private to " + recipientName + "] " + privateMessage);
            } else {
                sendMessage("User '" + recipientName + "' is not online.");
            }
        } else {
            sendMessage("Invalid command format. Use: /w recipient message");
        }
    }
}

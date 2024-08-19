package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

// Klasa ReceivedMessagesHandler odpowiada za odbieranie wiadomości od serwera w osobnym wątku.
public class RecivedMessageHandler implements Runnable {
    // Gniazdo połączenia z serwerem
    private Socket socket;

    // BufferedReader używany do odczytywania wiadomości od serwera
    private BufferedReader in;

    // Konstruktor, który inicjalizuje socket i BufferedReader
    public RecivedMessageHandler(Socket socket, BufferedReader in) {
        this.socket = socket;
        this.in = in;
    }

    @Override
    public void run() {
        String message;
        try {
            // Pętla odczytująca wiadomości od serwera
            while ((message = in.readLine()) != null) {
                // Wyświetlamy otrzymaną wiadomość w konsoli.
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Gdy połączenie zostanie zamknięte, zamykamy również socket.
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
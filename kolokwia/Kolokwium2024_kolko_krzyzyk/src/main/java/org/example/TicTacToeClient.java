package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TicTacToeClient {

    private String serverAddress;
    private int serverPort;

    public TicTacToeClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void startClient() {
        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Połączono z serwerem. Rozpocznij grę.");

            // Wątek odbierający komunikaty od serwera
            Thread receiverThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println("Serwer: " + message);
                        // Zakończenie gry, jeśli serwer wysyła informację o zakończeniu
                        if (message.equals("Game over") || message.equals("You win") || message.equals("You lose")) {
                            System.out.println("Gra zakończona. Wcisnij Enter, aby zamknąć...");
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiverThread.start();

            // Wysyłanie ruchów do serwera
            while (true) {
                System.out.println("Podaj współrzędne [x y]: ");
                String input = scanner.nextLine();

                // Jeśli ruch został wykonany, wyślij do serwera
                if (!input.trim().isEmpty()) {
                    writer.println(input);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda pomocnicza, aby ułatwić uruchomienie klienta bezpośrednio
    public static void startClient(String serverAddress, int serverPort) {
        TicTacToeClient client = new TicTacToeClient(serverAddress, serverPort);
        client.startClient();
    }
}
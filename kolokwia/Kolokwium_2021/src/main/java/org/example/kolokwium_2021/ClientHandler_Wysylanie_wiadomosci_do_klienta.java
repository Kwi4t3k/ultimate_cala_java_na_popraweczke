package org.example.kolokwium_2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket clientSocket; // Socket klienta
    private appCanvas canvas; // Płótno, na którym będą rysowane linie
    private String currentColor; // Aktualny kolor rysowania

    // Strumień do odczytu danych od klienta i do wysyłania odpowiedzi
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket, appCanvas appCanvas) {
        this.clientSocket = clientSocket;
        this.canvas = appCanvas;
        this.currentColor = "000000"; // Domyślny kolor: czarny (hex: 000000)
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Inicjalizacja strumienia wejściowego
            out = new PrintWriter(clientSocket.getOutputStream(), true); // Inicjalizacja strumienia wyjściowego (auto-flush włączony)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals("END")) { // Jeśli wiadomość to "END", kończymy połączenie
                    System.out.println("Zakończenie transmisji od klienta.");
                    sendResponse("Transmission ended."); // Wysyłanie wiadomości zwrotnej
                    break; // Przerywamy pętlę i zamykamy połączenie
                }

                // Jeśli wiadomość to kolor w formacie HEX
                if (line.length() == 6 && line.matches("[0-9A-Fa-f]+")) {
                    System.out.println("Otrzymano HEX: " + line);
                    currentColor = line; // Zapisanie nowego koloru
                    sendResponse("Color updated to " + line); // Wysyłanie wiadomości zwrotnej

                } else {
                    // Zakładamy, że wiadomość to współrzędne
                    System.out.println("Otrzymano współrzędne: " + line);

                    String[] parts = line.split(" "); // Dzielenie wiadomości na współrzędne
                    if (parts.length == 4) { // Sprawdzamy, czy mamy 4 wartości
                        double x1 = Double.parseDouble(parts[0]);
                        double y1 = Double.parseDouble(parts[1]);
                        double x2 = Double.parseDouble(parts[2]);
                        double y2 = Double.parseDouble(parts[3]);

                        canvas.drawLine(x1, y1, x2, y2, currentColor); // Rysowanie linii na płótnie
                        sendResponse("Line drawn from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")"); // Wysyłanie wiadomości zwrotnej
                    } else {
                        sendResponse("Invalid coordinate format: " + line); // Wysyłanie wiadomości o błędzie
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Obsługa wyjątków związanych z I/O
        } finally {
            try {
                clientSocket.close(); // Zamknięcie socketu klienta
                System.out.println("Rozłączono z klientem");
            } catch (IOException e) {
                e.printStackTrace(); // Obsługa wyjątków podczas zamykania socketu
            }
        }
    }

    // Metoda do wysyłania wiadomości zwrotnej do klienta
    private void sendResponse(String message) {
        if (out != null) {
            out.println(message); // Wysyłanie wiadomości
        }
    }
}
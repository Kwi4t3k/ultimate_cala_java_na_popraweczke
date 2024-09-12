package org.example.kolokwium_2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket clientSocket; // Socket klienta
    private appCanvas canvas; // Płótno, na którym będą rysowane linie
    private String currentColor; // Aktualny kolor rysowania

    public ClientHandler(Socket clientSocket, appCanvas appCanvas) {
        this.clientSocket = clientSocket;
        this.canvas = appCanvas;
        this.currentColor = "000000"; // Domyślny kolor: czarny (hex: 000000)
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Strumień do odczytu danych od klienta
            String line;

            while ((line = in.readLine()) != null) {
                // Przetwarzanie danych od klienta
                if (line.equals("END")) { // Jeśli klient wysłał wiadomość "END"
                    System.out.println("Zakończenie transmisji od klienta.");
                    break; // Przerywamy pętlę i zamykamy połączenie
                }

                // przykładowa wiadomość do zmiany koloru - 00FF00

         // lub if (line.length() == 6 && line.matches("[0-9A-Fa-f]+"))
                if (line.matches("^[0-9A-Fa-f]{6}$")) { // Sprawdzamy, czy wiadomość to kolor w formacie HEX
                    System.out.println("Otrzymano HEX: " + line);
                    currentColor = line; // Zapisanie nowego koloru

                // przykładowa wiadomość do ustawienia współrzędnych -  10 20 10 20

                // lub zrobić sam else jeśli zakładamy poprawny format wiadomości
                } else if (line.matches("^[+-]?\\d*\\.?\\d+\\s[+-]?\\d*\\.?\\d+\\s[+-]?\\d*\\.?\\d+\\s[+-]?\\d*\\.?\\d+$")) { // Sprawdzamy, czy wiadomość to współrzędne
                    System.out.println("Otrzymano współrzędne: " + line);

                    String[] parts = line.split(" "); // Dzielenie wiadomości na współrzędne
                    double x1 = Double.parseDouble(parts[0]);
                    double y1 = Double.parseDouble(parts[1]);
                    double x2 = Double.parseDouble(parts[2]);
                    double y2 = Double.parseDouble(parts[3]);

                    canvas.drawLine(x1, y1, x2, y2, currentColor); // Rysowanie linii na płótnie

                } else {
                    System.out.println("Zły format wiadomości: " + line); // Obsługa błędnego formatu wiadomości
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
}
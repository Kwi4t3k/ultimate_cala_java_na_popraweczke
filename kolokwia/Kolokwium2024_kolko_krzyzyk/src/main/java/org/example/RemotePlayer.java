package org.example; // Deklaracja pakietu

import java.io.BufferedReader; // Importowanie klasy do odczytu danych wejściowych
import java.io.IOException; // Importowanie klasy do obsługi wyjątków wejścia/wyjścia
import java.io.InputStreamReader; // Importowanie klasy do odczytu strumienia danych
import java.io.PrintWriter; // Importowanie klasy do zapisu danych do strumienia wyjściowego
import java.net.Socket; // Importowanie klasy Socket do obsługi połączeń sieciowych

public class RemotePlayer extends Player { // Klasa RemotePlayer, dziedziczy po klasie Player
    private Socket socket; // Gniazdo sieciowe dla gracza
    private BufferedReader reader; // Strumień do odczytu danych wejściowych od gracza
    private PrintWriter writer; // Strumień do zapisu danych wyjściowych do gracza

    public RemotePlayer(Symbol symbol, Board board, Socket socket) { // Konstruktor klasy RemotePlayer
        super(symbol, board); // Wywołanie konstruktora klasy nadrzędnej Player
        this.socket = socket; // Inicjalizacja gniazda sieciowego
        try {
            // Inicjalizacja strumieni wejścia i wyjścia dla komunikacji z klientem
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Tworzenie strumienia do odczytu danych od gracza
            this.writer = new PrintWriter(socket.getOutputStream(), true); // Tworzenie strumienia do wysyłania danych do gracza
        } catch (IOException e) { // Obsługa wyjątku w przypadku błędu
            e.printStackTrace(); // Wydrukowanie szczegółów błędu
        }
    }

    @Override
    public void playTurn() { // Nadpisanie metody playTurn z klasy Player
        try {
            // Odczyt współrzędnych od gracza
            String xInput = reader.readLine(); // Odczyt współrzędnej x od gracza
            String yInput = reader.readLine(); // Odczyt współrzędnej y od gracza

            if (xInput == null || yInput == null) { // Sprawdzenie, czy współrzędne nie są null
                inform("Error reading input."); // Informowanie o błędzie odczytu
                return; // Zakończenie tury
            }

            int x = Integer.parseInt(xInput); // Parsowanie współrzędnej x do liczby całkowitej
            int y = Integer.parseInt(yInput); // Parsowanie współrzędnej y do liczby całkowitej

            // Sprawdzenie, czy ruch jest prawidłowy
            if (x >= 0 && x < 3 && y >= 0 && y < 3 && board.board[x][y] == Symbol.PUSTE_POLE) { // Sprawdzenie, czy pole jest wolne
                board.setSymbol(symbol, x, y); // Ustawienie symbolu na planszy
                writer.println("OK"); // Potwierdzenie prawidłowego ruchu
            } else {
                writer.println("Invalid move"); // Powiadomienie o błędnym ruchu
            }
        } catch (IOException e) { // Obsługa wyjątku
            e.printStackTrace(); // Wydrukowanie szczegółów błędu
        }
    }

    @Override
    public void inform(String message) { // Nadpisanie metody inform
        writer.println(message); // Wysłanie wiadomości do gracza
    }
}
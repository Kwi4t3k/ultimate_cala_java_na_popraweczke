package org.example; // Deklaracja pakietu, w którym znajduje się klasa

import java.io.IOException; // Importowanie klasy do obsługi wyjątków wejścia/wyjścia
import java.net.ServerSocket; // Importowanie klasy do tworzenia gniazda serwera
import java.net.Socket; // Importowanie klasy do tworzenia połączeń sieciowych

public class RemoteBoard extends Board { // Deklaracja klasy RemoteBoard, która dziedziczy po klasie Board
    private ServerSocket serverSocket; // Pole przechowujące gniazdo serwera

    // Konstruktor z portem serwera
    public RemoteBoard(int port) { // Konstruktor, który przyjmuje numer portu
        try {
            // Tworzenie serwera nasłuchującego na określonym porcie
            this.serverSocket = new ServerSocket(port); // Tworzenie gniazda serwera na danym porcie
            System.out.println("Server started. Waiting for players to connect..."); // Informacja o starcie serwera
        } catch (IOException e) { // Obsługa wyjątku w przypadku błędu tworzenia serwera
            e.printStackTrace(); // Wydrukowanie szczegółów błędu
        }
    }

    @Override
    public void start() { // Nadpisanie metody start z klasy Board
        while (true) { // Pętla nieskończona - serwer działa, dopóki nie zostanie zamknięty
            try {
                // Oczekiwanie na połączenie dwóch graczy
                System.out.println("Waiting for Player 1..."); // Informacja o oczekiwaniu na gracza 1
                Socket player1Socket = serverSocket.accept(); // Akceptowanie połączenia od gracza 1
                System.out.println("Player 1 connected."); // Informacja o połączeniu gracza 1

                System.out.println("Waiting for Player 2..."); // Informacja o oczekiwaniu na gracza 2
                Socket player2Socket = serverSocket.accept(); // Akceptowanie połączenia od gracza 2
                System.out.println("Player 2 connected."); // Informacja o połączeniu gracza 2

                // Utworzenie graczy zdalnych
                RemotePlayer player1 = new RemotePlayer(Symbol.KOLKO, this, player1Socket); // Tworzenie zdalnego gracza 1 z symbolem KOLKO
                RemotePlayer player2 = new RemotePlayer(Symbol.KRZYZYK, this, player2Socket); // Tworzenie zdalnego gracza 2 z symbolem KRZYZYK

                // Rozpoczęcie gry
                playGame(player1, player2); // Uruchomienie gry między dwoma graczami

                // Zamknięcie połączeń po zakończeniu gry
                player1Socket.close(); // Zamknięcie połączenia gracza 1
                player2Socket.close(); // Zamknięcie połączenia gracza 2

            } catch (IOException e) { // Obsługa wyjątku w przypadku błędu wejścia/wyjścia
                e.printStackTrace(); // Wydrukowanie szczegółów błędu
            }
        }
    }

    @Override
    public void playGame(Player player1, Player player2) { // Nadpisanie metody playGame z klasy Board
        boolean gameOver = false; // Flaga oznaczająca koniec gry, początkowo ustawiona na false
        Player currentPlayer = player1; // Zmienna przechowująca obecnego gracza, rozpoczyna gracz 1

        while (!gameOver) { // Pętla działa dopóki gra się nie zakończy
            // Przewodzenie tury obecnego gracza
            currentPlayer.playTurn(); // Wywołanie metody tury dla obecnego gracza
            // Sprawdzanie, czy gra się zakończyła
            gameOver = checkGameOver(); // Sprawdzenie, czy gra dobiegła końca
            // Przełączanie na następnego gracza, jeśli gra nie jest zakończona
            if (!gameOver) { // Jeśli gra się nie zakończyła
                currentPlayer = (currentPlayer == player1) ? player2 : player1; // Zmiana gracza na kolejnego
            }
        }

        // Informowanie graczy o wyniku
        String result = determineResult(); // Ustalanie wyniku gry
        player1.inform(result); // Informowanie gracza 1 o wyniku
        player2.inform(result); // Informowanie gracza 2 o wyniku
    }
}
package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RemoteBoard extends Board {
    private ServerSocket serverSocket;

    // Konstruktor z portem serwera
    public RemoteBoard(int port) {
        try {
            // Tworzenie serwera nasłuchującego na określonym porcie
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server started. Waiting for players to connect...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        while (true) {
            try {
                // Oczekiwanie na połączenie dwóch graczy
                System.out.println("Waiting for Player 1...");
                Socket player1Socket = serverSocket.accept();
                System.out.println("Player 1 connected.");

                System.out.println("Waiting for Player 2...");
                Socket player2Socket = serverSocket.accept();
                System.out.println("Player 2 connected.");

                // Utworzenie graczy zdalnych
                RemotePlayer player1 = new RemotePlayer(Symbol.KOLKO, this, player1Socket);
                RemotePlayer player2 = new RemotePlayer(Symbol.KRZYZYK, this, player2Socket);

                // Rozpoczęcie gry
                playGame(player1, player2);

                // Zamknięcie połączeń po zakończeniu gry
                player1Socket.close();
                player2Socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void playGame(Player player1, Player player2) {
        boolean gameOver = false;
        Player currentPlayer = player1;

        while (!gameOver) {
            // Przewodzenie tury obecnego gracza
            currentPlayer.playTurn();
            // Sprawdzanie, czy gra się zakończyła
            gameOver = checkGameOver();
            // Przełączanie na następnego gracza, jeśli gra nie jest zakończona
            if (!gameOver) {
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
            }
        }

        // Informowanie graczy o wyniku
        String result = determineResult();
        player1.inform(result);
        player2.inform(result);
    }
}
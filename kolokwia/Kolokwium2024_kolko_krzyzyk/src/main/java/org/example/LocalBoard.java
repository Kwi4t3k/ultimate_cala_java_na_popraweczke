package org.example;

public class LocalBoard extends Board {
    public Symbol[][] getBoard() {
        return board;
    }

    public void setBoard(Symbol[][] board){
        this.board = board;
    }

    // Implementacja metody playGame, zarządzająca rozgrywką między dwoma graczami
    @Override
    public void playGame(Player player1, Player player2) {
        boolean gameOver = false;       // Flaga sprawdzająca, czy gra się zakończyła
        Player currentPlayer = player1; // Zaczynamy od gracza 1

        while (!gameOver) {
            currentPlayer.playTurn();   // Aktualny gracz wykonuje ruch

            gameOver = checkGameOver(); // Sprawdzenie, czy gra się zakończyła
            if (!gameOver) {
                // Zmiana aktualnego gracza na drugiego gracza
                if (currentPlayer == player1) {
                    currentPlayer = player2; // Zmiana na gracza 2
                } else {
                    currentPlayer = player1; // Zmiana na gracza 1
                }
            }
        }

        // Po zakończeniu gry, wyświetlamy wynik
        String result = determineResult();
        player1.inform(result); // Poinformowanie gracza 1 o wyniku
        player2.inform(result); // Poinformowanie gracza 2 o wyniku
    }

    @Override
    public void start() {
        // Utworzenie dwóch graczy: jeden gra kółkiem, drugi krzyżykiem
        Player player1 = new HumanPlayer(Symbol.KOLKO, this);
        Player player2 = new HumanPlayer(Symbol.KRZYZYK, this);
        playGame(player1, player2); // Rozpoczęcie rozgrywki
    }
}
package org.example;

public class LocalBoard extends Board {
    private String gameResult = "GRA_TRWA";
    @Override
    public void playGame(Player player1, Player player2) {
        boolean gameOver = false;
        Player currentPlayer = player1;

        while (!gameOver) {
            currentPlayer.playTurn();
            updateGameResult();

            gameOver = checkGameOver();

            if (!gameOver) {
                // Zmień aktualnego gracza
                if (currentPlayer == player1) {
                    currentPlayer = player2; // Zmiana na player2
                    player1.inform("tura gracza2");
                    player2.inform("tura gracza2");
                } else {
                    currentPlayer = player1; // Zmiana na player1
                    player1.inform("tura gracza1");
                    player2.inform("tura gracza1");
                }
            }

            String result = getGameResult();
            player1.inform(result);
            player2.inform(result);
        }
    }

    // Metoda sprawdzająca, czy gra się zakończyła
    private boolean checkGameOver() {
        return gameResult.equals("KOLKO_WYGRANA") ||
                gameResult.equals("KRZYZYK_WYGRANA") ||
                gameResult.equals("REMIS");
    }

    // Metoda sprawdzająca wynik gry
    private void updateGameResult() {
        if (checkWin(Symbol.KOLKO)) {
            gameResult = "KOLKO_WYGRANA";
        } else if (checkWin(Symbol.KRZYZYK)) {
            gameResult = "KRZYZYK_WYGRANA";
        } else if (isBoardFull()) {
            gameResult = "REMIS";
        } else {
            gameResult = "GRA_TRAJE";
        }
    }

    // Metoda zwracająca wynik gry
    public String getGameResult() {
        return gameResult;
    }

    // Metoda sprawdzająca, czy dany symbol wygrał
    private boolean checkWin(Symbol symbol) {
        // Sprawdź wiersze, kolumny i przekątne
        return (checkRows(symbol) || checkColumns(symbol) || checkDiagonals(symbol));
    }

    // Metoda sprawdzająca wiersze
    private boolean checkRows(Symbol symbol) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true;
            }
        }
        return false;
    }

    // Metoda sprawdzająca kolumny
    private boolean checkColumns(Symbol symbol) {
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                return true;
            }
        }
        return false;
    }

    // Metoda sprawdzająca przekątne
    private boolean checkDiagonals(Symbol symbol) {
        return (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
    }

    // Metoda sprawdzająca, czy plansza jest pełna
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Symbol.PUSTE_POLE) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void start() {
        // Metoda start rozpoczynająca rozgrywkę
    }
}
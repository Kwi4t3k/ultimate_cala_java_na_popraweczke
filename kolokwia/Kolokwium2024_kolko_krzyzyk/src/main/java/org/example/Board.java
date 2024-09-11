package org.example;

public abstract class Board {
    protected Symbol[][] board;

    // Konstruktor inicjalizujący tablicę 3x3 pustymi polami
    public Board() {
        board = new Symbol[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = Symbol.PUSTE_POLE;
            }
        }
    }

    // Setter do ustawiania Symbolu na planszy
    public void setSymbol(Symbol symbol, int x, int y) {
        if (x >= 0 && x < 3 && y >= 0 && y < 3) {
            board[x][y] = symbol;
        }
    }

    // Abstrakcyjna metoda start, która zostanie zaimplementowana w klasach pochodnych
    public abstract void start();

    // Abstrakcyjna metoda playGame do rozgrywki pomiędzy dwoma graczami
    public abstract void playGame(Player player1, Player player2);
}
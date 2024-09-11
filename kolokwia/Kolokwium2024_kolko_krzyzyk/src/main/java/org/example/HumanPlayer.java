package org.example;

import java.util.Scanner;

public class HumanPlayer extends Player {
    public HumanPlayer(Symbol symbol, Board board) {
        super(symbol, board);
    }

    // Metoda wyświetlająca aktualny stan planszy
    private void showBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(board.board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Metoda oczekująca na wprowadzenie współrzędnych symbolu
    private void listenInput() {
        Scanner scanner = new Scanner(System.in);
        int x, y;

        System.out.println("Podaj współrzędne [x, y]: ");
        x = scanner.nextInt();
        y = scanner.nextInt();

        // Sprawdzamy, czy współrzędne są w zakresie i pole jest puste
        if (x >= 0 && x < 3 && y >= 0 && y < 3 && board.board[x][y] == Symbol.PUSTE_POLE) {
            board.setSymbol(symbol, x, y);
        } else {
            inform("Nieprawidłowy ruch! Spróbuj ponownie.");
        }
    }

    @Override
    public void inform(String message) {
        System.out.println("Informacja: " + message);
    }

    @Override
    public void playTurn() {
        showBoard();
        listenInput();
        inform("Ruch zakończony");
    }
}

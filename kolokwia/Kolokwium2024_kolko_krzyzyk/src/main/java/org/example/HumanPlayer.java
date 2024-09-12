package org.example;

import java.util.Scanner;

// Klasa HumanPlayer dziedzicząca po Player, reprezentująca gracza sterowanego przez człowieka
public class HumanPlayer extends Player {
    public HumanPlayer(Symbol symbol, Board board) {
        super(symbol, board);
    }

    // Metoda wyświetlająca aktualny stan planszy
    private void showBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board.board[i][j] + " "); // Wyświetlenie symbolu na planszy
            }
            System.out.println(); // Nowa linia po każdym wierszu
        }
    }

    // Metoda oczekująca na wprowadzenie współrzędnych symbolu
    private void listenInput() {
        Scanner scanner = new Scanner(System.in);
        int x, y;

        System.out.println("Podaj współrzędne [x, y]: ");
        x = scanner.nextInt(); // Wczytanie współrzędnej x
        y = scanner.nextInt(); // Wczytanie współrzędnej y

        // Sprawdzamy, czy współrzędne są w zakresie i pole jest puste
        if (x >= 0 && x < 3 && y >= 0 && y < 3 && board.board[x][y] == Symbol.PUSTE_POLE) {
            board.setSymbol(symbol, x, y); // Ustawienie symbolu na planszy
        } else {
            inform("Nieprawidłowy ruch! Spróbuj ponownie."); // Informowanie o błędnym ruchu
        }
    }

    @Override
    public void inform(String message) {
        System.out.println("Informacja: " + message); // Wyświetlenie informacji do gracza
    }

    @Override
    public void playTurn() {
        showBoard(); // Wyświetlenie planszy
        listenInput(); // Oczekiwanie na wejście od użytkownika
        inform("Ruch zakończony"); // Informowanie o zakończeniu tury
    }
}
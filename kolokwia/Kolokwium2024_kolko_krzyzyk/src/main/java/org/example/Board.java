package org.example;

public abstract class Board {
    protected Symbol[][] board; // Tablica 3x3 do przechowywania stanu planszy

    // Konstruktor inicjalizujący planszę z pustymi polami
    public Board() {
        board = new Symbol[3][3]; // Inicjalizacja tablicy 3x3
        // Ustawienie wszystkich pól na wartość PUSTE_POLE
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = Symbol.PUSTE_POLE;
            }
        }
    }

    // Setter do ustawienia symbolu na określonym miejscu planszy
    public void setSymbol(Symbol symbol, int x, int y) {
        // Sprawdzenie, czy współrzędne są w granicach planszy
        if (x >= 0 && x < 3 && y >= 0 && y < 3) {
            board[x][y] = symbol; // Ustawienie symbolu na planszy
        }
    }

    // Sprawdzenie, czy gra się zakończyła (wygrana lub pełna plansza)
    public boolean checkGameOver() {
        // Sprawdzenie wierszy i kolumn
        for (int i = 0; i < 3; i++) {
            // Sprawdzenie wierszy
            if (board[i][0] != Symbol.PUSTE_POLE && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
            // Sprawdzenie kolumn
            if (board[0][i] != Symbol.PUSTE_POLE && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return true;
            }
        }
        // Sprawdzenie przekątnych
        if (board[0][0] != Symbol.PUSTE_POLE && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }
        if (board[0][2] != Symbol.PUSTE_POLE && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }
        // Sprawdzenie, czy plansza jest pełna
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Symbol.PUSTE_POLE) {
                    return false;
                }
            }
        }
        return true; // Plansza pełna - gra zakończona remisem
    }

    // Ustalanie wyniku gry
    public String determineResult() {
        // Sprawdzenie wierszy, kolumn i przekątnych
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != Symbol.PUSTE_POLE && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0] + " wygrał!";
            }
            if (board[0][i] != Symbol.PUSTE_POLE && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i] + " wygrał!";
            }
        }
        // Sprawdzenie przekątnych
        if (board[0][0] != Symbol.PUSTE_POLE && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0] + " wygrał!";
        }
        if (board[0][2] != Symbol.PUSTE_POLE && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2] + " wygrał!";
        }
        // Sprawdzenie, czy plansza jest pełna
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Symbol.PUSTE_POLE) {
                    return "Gra trwa"; // Gra jeszcze trwa
                }
            }
        }
        return "Remis!"; // Jeśli żadna z opcji nie zadziałała, mamy remis
    }

    // Abstrakcyjna metoda start - zostanie zaimplementowana w klasach pochodnych
    public abstract void start();

    // Abstrakcyjna metoda playGame do rozgrywki pomiędzy dwoma graczami
    public abstract void playGame(Player player1, Player player2);
}
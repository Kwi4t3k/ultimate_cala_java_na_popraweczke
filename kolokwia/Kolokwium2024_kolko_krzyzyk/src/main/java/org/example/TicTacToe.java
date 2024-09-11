package org.example;

public class TicTacToe {
    public static void main(String[] args) {
        if (args[0].equals("hotseats")) {
            LocalBoard localBoard = new LocalBoard();
            Player player1 = new HumanPlayer(Symbol.KOLKO, localBoard);
            Player player2 = new HumanPlayer(Symbol.KRZYZYK, localBoard);

            localBoard.playGame(player1, player2);
        } else {
            System.out.println("Nieznany tryb gry");
        }
    }
}

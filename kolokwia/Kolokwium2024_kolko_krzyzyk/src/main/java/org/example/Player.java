package org.example;

public abstract class Player {
    protected Symbol symbol; // Symbol przypisany do gracza
    protected Board board;   // Referencja do planszy, na której gracz rozgrywa grę

    // Konstruktor przypisujący graczowi symbol i planszę
    public Player(Symbol symbol, Board board) {
        this.symbol = symbol;
        this.board = board;
    }

    // Abstrakcyjna metoda playTurn - gracz wykonuje swój ruch
    public abstract void playTurn();

    // Abstrakcyjna metoda inform - przekazywanie informacji graczowi
    public abstract void inform(String message);
}
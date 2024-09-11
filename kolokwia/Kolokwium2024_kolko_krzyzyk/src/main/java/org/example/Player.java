package org.example;

public abstract class Player {
    protected Symbol symbol; // Symbol gracza
    protected Board board;   // Referencja na planszę

    public Player(Symbol symbol, Board board) {
        this.symbol = symbol;
        this.board = board;
    }

    // Abstrakcyjna metoda playTurn do zagrania tury
    public abstract void playTurn();

    // Abstrakcyjna metoda inform do przekazywania informacji graczowi
    public abstract void inform(String message);
}
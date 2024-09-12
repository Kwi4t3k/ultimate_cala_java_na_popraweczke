package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RemotePlayer extends Player {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public RemotePlayer(Symbol symbol, Board board, Socket socket) {
        super(symbol, board);
        this.socket = socket;
        try {
            // Inicjalizacja strumieni wejścia i wyjścia dla komunikacji z klientem
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playTurn() {
        try {
            // Odczyt współrzędnych od gracza
            String xInput = reader.readLine();
            String yInput = reader.readLine();

            if (xInput == null || yInput == null) {
                inform("Error reading input.");
                return;
            }

            int x = Integer.parseInt(xInput);
            int y = Integer.parseInt(yInput);

            // Sprawdzenie, czy ruch jest prawidłowy
            if (x >= 0 && x < 3 && y >= 0 && y < 3 && board.board[x][y] == Symbol.PUSTE_POLE) {
                board.setSymbol(symbol, x, y);
                writer.println("OK"); // Potwierdzenie prawidłowego ruchu
            } else {
                writer.println("Invalid move"); // Powiadomienie o błędnym ruchu
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inform(String message) {
        writer.println(message);
    }
}
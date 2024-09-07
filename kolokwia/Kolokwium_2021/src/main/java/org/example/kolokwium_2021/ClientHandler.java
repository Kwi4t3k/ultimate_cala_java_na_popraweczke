package org.example.kolokwium_2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private appCanvas canvas;

    public ClientHandler(Socket clientSocket, appCanvas appCanvas) {
        this.clientSocket = clientSocket;
        this.canvas = appCanvas;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;

            while ((line = in.readLine()) != null) {
                // Przetwarzanie danych od klienta

                if (line.matches("^[0-9A-Fa-f]{6}$")){
                    System.out.println("Otrzymano HEX: " + line);
                    canvas.setColor(line);

                } else if (line.matches("^[+-]?\\d*\\.?\\d+\\s[+-]?\\d*\\.?\\d+\\s[+-]?\\d*\\.?\\d+\\s[+-]?\\d*\\.?\\d+$")) {

                    System.out.println("Otrzymano współrzędne: " + line);

                    String[] parts = line.split(" ");
                    double x1 = Double.parseDouble(parts[0]);
                    double y1 = Double.parseDouble(parts[1]);
                    double x2 = Double.parseDouble(parts[2]);
                    double y2 = Double.parseDouble(parts[3]);

                    canvas.drawLine(x1, y1, x2, y2);

                } else {
                    System.out.println("Zły format wiadomości");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
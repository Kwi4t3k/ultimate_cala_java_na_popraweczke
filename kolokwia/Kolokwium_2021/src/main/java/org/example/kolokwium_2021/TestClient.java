package org.example.kolokwium_2021;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4000); // Tworzenie połączenia z serwerem
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {

            // Wysyłanie koloru w formacie HEX
            out.println("FF0000");

            // Wysyłanie współrzędnych
            out.println("100 100 200 200");

            // Informowanie serwera o zakończeniu transmisji danych
            out.println("END");

            // Utrzymanie połączenia przez 5 sekund, aby dać czas serwerowi
            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace(); // Obsługa wyjątków
        }
    }
}
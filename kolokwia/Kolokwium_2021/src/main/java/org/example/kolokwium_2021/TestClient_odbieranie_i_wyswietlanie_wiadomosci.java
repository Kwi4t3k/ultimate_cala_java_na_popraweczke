package org.example.kolokwium_2021;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4000);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Wysyłanie koloru w formacie HEX
            out.println("FF0000");

            // Odbieranie odpowiedzi od serwera
            System.out.println("Server response: " + in.readLine());

            // Wysyłanie współrzędnych
            out.println("100 100 200 200");

            // Odbieranie odpowiedzi od serwera
            System.out.println("Server response: " + in.readLine());

            // Informowanie serwera o zakończeniu transmisji danych
            out.println("END");

            // Odbieranie odpowiedzi od serwera
            System.out.println("Server response: " + in.readLine());

            // Utrzymanie połączenia przez 5 sekund, aby dać czas serwerowi
            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

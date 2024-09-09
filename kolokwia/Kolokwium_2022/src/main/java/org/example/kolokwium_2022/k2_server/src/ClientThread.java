package org.example.kolokwium_2022.k2_server.src;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    // Gniazdo połączeniowe klienta
    private Socket socket;
    // Writer do wysyłania danych do klienta
    private PrintWriter writer;
    // Referencja do obiektu serwera
    private Server server;

    // Konstruktor, który przyjmuje gniazdo klienta i obiekt serwera
    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            // Strumień do odbierania danych od klienta
            InputStream input = socket.getInputStream();
            // Strumień do wysyłania danych do klienta
            OutputStream output = socket.getOutputStream();
            // Buforowany reader do czytania danych od klienta
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            // Writer do wysyłania danych do klienta
            writer = new PrintWriter(output, true);

            // Oczekiwanie na wiadomości od klienta (nieskończona pętla)
            while (reader.readLine() != null) {
            }

            // Gdy połączenie zostanie zamknięte, usuń klienta z serwera
            System.out.println("closed");
            server.removeClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda do wysyłania wiadomości do klienta
    public void send(String message) {
        writer.println(message); // Wysyłanie wiadomości do klienta
    }
}
//package org.example;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//// Klasa obsługująca pojedynczego klienta
//public class ClientHandler implements Runnable {
//    private Socket socket;           // Socket do komunikacji z klientem
//    private Server server;           // Referencja do obiektu Server, aby móc wysyłać wiadomości do innych klientów
//    private PrintWriter out;         // Strumień wyjściowy do wysyłania wiadomości do klienta
//
//    // Konstruktor przyjmujący socket klienta i referencję do serwera
//    public ClientHandler(Socket socket, Server server) {
//        this.socket = socket;
//        this.server = server;
//
//        try {
//            // Ustawiamy strumień wejściowy, aby móc odbierać wiadomości od klienta.
//            InputStream input = socket.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//
//            // Ustawiamy strumień wyjściowy, aby móc wysyłać wiadomości do klienta.
//            OutputStream output = socket.getOutputStream();
//            out = new PrintWriter(output, true);
//
//            // Pętla, która odbiera wiadomości od klienta. Dopóki klient przesyła wiadomości, będą one odbierane.
//            String message;
//            while ((message = reader.readLine()) != null) {
//                System.out.println("Received: " + message);
//
//                // Po otrzymaniu wiadomości od klienta, przekazujemy ją do serwera, aby została rozesłana do innych klientów.
//                server.broadcast(message, this);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // W przypadku zamknięcia połączenia, usuwamy klienta z listy klientów serwera i zamykamy socket
//            try {
//                socket.close();
//                server.removeClient(this);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Metoda do wysyłania wiadomości do klienta
//    public void sendMessage(String message) {
//        out.println(message);
//    }
//
//    @Override
//    public void run() {
//        // Kod obsługi klienta został wykonany w konstruktorze, więc tu nie jest potrzebny dodatkowy kod.
//    }
//}
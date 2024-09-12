package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean isClientConnected = false;

    public Server (int port) {
        try {
            this.serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Połączono klienta");

                // Jeśli klient jest już połączony, odrzucamy nowe połączenia
                if (isClientConnected) {
                    PrintWriter tmpWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                    tmpWriter.println("Inny admin jest juz zalogowany. Sprobuj pozniej.");
                    clientSocket.close();
                    System.out.println("Rozłączono klienta ponieważ admin jest już zalogowany");
                } else {
                    // Uruchomienie nowego wątku do obsługi klienta
                    new ClientHandler(clientSocket).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Klasa wewnętrzna do obsługi klienta
    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                writer.println("Polaczono z serwerem");
                writer.println("Witaj admin!");
                writer.println("Podaj haslo");

                String password = reader.readLine();

                if (password.trim().equals("tajnehaslo")) {
                    writer.println("Poprawne haslo. Zapraszam");
                    isClientConnected = true;

                    String message;
                    while ((message = reader.readLine()) != null) {
                        if (message.trim().equals("bye")) {
                            writer.println("adioz");
                            disconnect();
                            break;
                        }

                        // KROK 8

                        if (message.startsWith("ban")) {
                            String[] messageParts = message.split(" ");
                            if (messageParts.length == 2 && isInteger(messageParts[1])) {
                                int id = Integer.parseInt(messageParts[1]);
                                System.out.println("Użytkownik o id: " + id + " zbanowany");

                                synchronized (this) { // w nawiasach trzeba dać wątek
                                    int numberOfDeletedRecords = AdminUtills.ban(id);
                                    writer.println("Usunieto " + numberOfDeletedRecords + " rekordow");
                                }
                            }
                        }

                        // KROK 9
                        if (message.startsWith("video")) {
                            writer.println("Rozpoczynam generowanie wideo...");
                            AdminUtills.generateVideo();
                            writer.println("Wideo zostało wygenerowane.");
                        }
                    }
                } else {
                    writer.println("Niepoprawne haslo. Wypierdzielaj");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public void disconnect(){
        try {
            clientSocket.close();
            System.out.println("Rozłączono klienta");
            isClientConnected = false; // Ustawienie flagi na false, aby można było połączyć kolejnego klienta
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
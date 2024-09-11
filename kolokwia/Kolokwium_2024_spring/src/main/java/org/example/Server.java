package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private Client client;
    private BufferedReader reader;
    private String login;
    private String password;

    public Server (int port) {
        try {
            this.serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean isClientConnected = false;
        while (true) {
            try {
                if (!isClientConnected) {
                    clientSocket = serverSocket.accept();
                    System.out.println("Połączono z klientem");

                    OutputStream outputStream = clientSocket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    printWriter.println("Polaczono z serwerem");

                    InputStream inputStream = clientSocket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    printWriter.println("Witaj admin!");
                    printWriter.println("Podaj haslo");
                    password = reader.readLine();

                    if (password.trim().equals("tajnehaslo")) {
                        printWriter.println("Poprawne haslo. Zapraszam");
                        isClientConnected = true;

                        String message;
                        message = reader.readLine();
                        if (message.trim().equals("bye")) {
                            disconnect();
                        }
                    } else {
                        printWriter.println("Niepoprawne haslo. Wypierdzielaj");
                        isClientConnected = false;
                        disconnect();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void disconnect(){
        try {
            clientSocket.close();
            System.out.println("Rozłączono klienta");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package org.example;

import java.io.*;
import java.net.Socket;

// Klasa Client reprezentuje klienta czatu, który łączy się z serwerem, wysyła wiadomości i odbiera je.
public class Client {
    // Socket reprezentuje połączenie klienta z serwerem.
    private Socket socket;

    // PrintWriter jest używany do wysyłania wiadomości do serwera.
    private PrintWriter out;

    // BufferedReader jest używany do odbierania wiadomości od serwera.
    private BufferedReader in;

    // Konstruktor Client przyjmuje adres serwera i port, na którym serwer nasłuchuje.
    public Client(String serverAddress, int serverPort) throws IOException {
        // Tworzymy socket i łączymy się z serwerem na podanym adresie i porcie.
        socket = new Socket(serverAddress, serverPort);

        // Inicjalizujemy PrintWriter do wysyłania wiadomości do serwera. Auto-flush jest ustawione na true,
        // co oznacza, że dane będą wysyłane od razu po wywołaniu metody println().
        out = new PrintWriter(socket.getOutputStream(), true);

        // Inicjalizujemy BufferedReader do odbierania wiadomości od serwera.
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    // Metoda start() rozpoczyna komunikację z serwerem: wysyłanie i odbieranie wiadomości.
    public void start() {
        // Tworzymy wątek, który będzie obsługiwał odbieranie wiadomości od serwera.
        new Thread(new ReceivedMessagesHandler()).start();

        // Wczytujemy dane od użytkownika (wiadomości) z konsoli i wysyłamy je do serwera.
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            String message;
            while ((message = consoleReader.readLine()) != null) {
                // Wysyłamy wiadomość do serwera.
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Klasa ReceivedMessagesHandler odpowiada za odbieranie wiadomości od serwera w osobnym wątku.
    private class ReceivedMessagesHandler implements Runnable {
        @Override
        public void run() {
            String message;
            try {
                // Pętla, która odbiera wiadomości od serwera.
                while ((message = in.readLine()) != null) {
                    // Wyświetlamy otrzymaną wiadomość w konsoli.
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Gdy połączenie zostanie zamknięte, zamykamy również socket.
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private ServerSocket serverSocket; // Gniazdo serwera, umożliwia nasłuchiwanie połączeń
    private Socket clientSocket; // Gniazdo klienta, do komunikacji z klientem
    private boolean isClientConnected = false; // Flaga określająca, czy klient jest już połączony

    // Konstruktor klasy Server, inicjuje gniazdo serwera na danym porcie
    public Server (int port) {
        try {
            this.serverSocket = new ServerSocket(port); // Tworzymy nowe gniazdo serwera na danym porcie
        } catch (IOException e) {
            e.printStackTrace(); // Obsługa błędów połączenia
        }
    }

    @Override
    public void run() {
        while (true) { // Serwer działa w nieskończonej pętli
            try {
                clientSocket = serverSocket.accept(); // Akceptujemy nowe połączenie od klienta
                System.out.println("Połączono klienta"); // Informacja o połączeniu klienta

                // Jeśli klient jest już połączony, odrzucamy nowe połączenia
                if (isClientConnected) {
                    PrintWriter tmpWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                    tmpWriter.println("Inny admin jest juz zalogowany. Sprobuj pozniej."); // Komunikat o odrzuceniu
                    clientSocket.close(); // Zamykamy połączenie
                    System.out.println("Rozłączono klienta ponieważ admin jest już zalogowany"); // Informacja o rozłączeniu
                } else {
                    // Tworzymy nowy wątek do obsługi klienta
                    new ClientHandler(clientSocket).start(); // Uruchamiamy nowy wątek dla nowego klienta
                }
            } catch (IOException e) {
                e.printStackTrace(); // Obsługa błędów
            }
        }
    }

    // Klasa wewnętrzna do obsługi klienta
    private class ClientHandler extends Thread {
        private Socket clientSocket; // Gniazdo połączenia z klientem
        private BufferedReader reader; // Odczyt danych od klienta
        private PrintWriter writer; // Wysyłanie danych do klienta

        // Konstruktor klasy ClientHandler, inicjuje gniazdo klienta
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket; // Przypisanie gniazda klienta
        }

        @Override
        public void run() {
            try {
                // Inicjalizacja obiektów do odczytu i zapisu danych
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Powitanie klienta
                writer.println("Polaczono z serwerem");
                writer.println("Witaj admin!");
                writer.println("Podaj haslo");

                String password = reader.readLine(); // Odczytanie hasła od klienta

                // Sprawdzanie poprawności hasła
                if (password.trim().equals("tajnehaslo")) {
                    writer.println("Poprawne haslo. Zapraszam"); // Komunikat o poprawnym hasle
                    isClientConnected = true; // Oznaczenie, że klient jest połączony

                    String message;
                    while ((message = reader.readLine()) != null) {
                        // Jeśli klient wpisze "bye", rozłączamy go
                        if (message.trim().equals("bye")) {
                            writer.println("adioz"); // Wysłanie komunikatu o zakończeniu
                            disconnect(); // Rozłączenie klienta
                            break;
                        }

                        // KROK 8 - Obsługa komendy "ban"
                        if (message.startsWith("ban")) {
                            String[] messageParts = message.split(" "); // Podział wiadomości na części
                            if (messageParts.length == 2 && isInteger(messageParts[1])) {
                                int id = Integer.parseInt(messageParts[1]); // Pobranie ID do zbanowania
                                System.out.println("Użytkownik o id: " + id + " zbanowany");

                                synchronized (this) { // Synchronizacja operacji wątku
                                    int numberOfDeletedRecords = AdminUtills.ban(id); // Wywołanie metody ban
                                    writer.println("Usunieto " + numberOfDeletedRecords + " rekordow");
                                }
                            }
                        }

                        // KROK 9 - Obsługa komendy "video"
                        if (message.startsWith("video")) {
                            writer.println("Rozpoczynam generowanie wideo..."); // Komunikat o rozpoczęciu generowania
                            AdminUtills.generateVideo(); // Generowanie wideo
                            writer.println("Wideo zostało wygenerowane."); // Komunikat o zakończeniu
                        }
                    }
                } else {
                    writer.println("Niepoprawne haslo. Wypierdzielaj"); // Komunikat o błędnym haśle
                }
            } catch (IOException e) {
                e.printStackTrace(); // Obsługa błędów
            } finally {
                disconnect(); // Rozłączenie po zakończeniu pracy z klientem
            }
        }
    }

    // Metoda pomocnicza do sprawdzania, czy podany string jest liczbą
    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s); // Próbujemy przekonwertować string na liczbę
        } catch (NumberFormatException | NullPointerException e) {
            return false; // Jeśli nie jest liczbą, zwracamy false
        }
        return true; // Jeśli jest liczbą, zwracamy true
    }

    // Metoda do rozłączania klienta
    public void disconnect(){
        try {
            clientSocket.close(); // Zamykamy połączenie
            System.out.println("Rozłączono klienta"); // Logujemy rozłączenie klienta
            isClientConnected = false; // Flaga ustawiona na false, by można było podłączyć kolejnego klienta
        } catch (IOException e) {
            e.printStackTrace(); // Obsługa błędów przy rozłączaniu
        }
    }
}
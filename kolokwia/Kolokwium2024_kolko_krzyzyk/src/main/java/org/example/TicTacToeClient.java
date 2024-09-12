package org.example; // Deklaracja pakietu

import java.io.BufferedReader; // Importowanie klasy do odczytu danych wejściowych
import java.io.IOException; // Importowanie klasy do obsługi wyjątków wejścia/wyjścia
import java.io.InputStreamReader; // Importowanie klasy do odczytu strumienia danych
import java.io.PrintWriter; // Importowanie klasy do zapisu danych do strumienia wyjściowego
import java.net.Socket; // Importowanie klasy Socket do obsługi połączeń sieciowych
import java.util.Scanner; // Importowanie klasy do odczytu danych od użytkownika z konsoli

public class TicTacToeClient {

    private String serverAddress; // Pole przechowujące adres serwera
    private int serverPort; // Pole przechowujące numer portu serwera

    public TicTacToeClient(String serverAddress, int serverPort) { // Konstruktor klienta
        this.serverAddress = serverAddress; // Inicjalizacja adresu serwera
        this.serverPort = serverPort; // Inicjalizacja portu serwera
    }

    public void startClient() { // Metoda do uruchomienia klienta
        try (Socket socket = new Socket(serverAddress, serverPort); // Tworzenie gniazda i łączenie z serwerem
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Tworzenie strumienia do odbioru danych od serwera
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // Tworzenie strumienia do wysyłania danych do serwera
             Scanner scanner = new Scanner(System.in)) { // Tworzenie skanera do odczytu danych od użytkownika

            System.out.println("Połączono z serwerem. Rozpocznij grę."); // Informacja o połączeniu z serwerem

            // Wątek odbierający komunikaty od serwera
            Thread receiverThread = new Thread(() -> { // Tworzenie nowego wątku do odbierania komunikatów od serwera
                try {
                    String message; // Zmienna do przechowywania wiadomości od serwera
                    while ((message = reader.readLine()) != null) { // Odczytywanie komunikatów od serwera
                        System.out.println("Serwer: " + message); // Wyświetlanie wiadomości od serwera
                        // Zakończenie gry, jeśli serwer wysyła informację o zakończeniu
                        if (message.equals("Game over") || message.equals("You win") || message.equals("You lose")) { // Sprawdzanie, czy gra się zakończyła
                            System.out.println("Gra zakończona. Wcisnij Enter, aby zamknąć..."); // Informacja o zakończeniu gry
                            break; // Przerwanie pętli
                        }
                    }
                } catch (IOException e) { // Obsługa wyjątku
                    e.printStackTrace(); // Wydrukowanie szczegółów błędu
                }
            });
            receiverThread.start(); // Uruchomienie wątku do odbierania komunikatów od serwera

            // Wysyłanie ruchów do serwera
            while (true) { // Pętla nieskończona do wysyłania ruchów gracza
                System.out.println("Podaj współrzędne [x y]: "); // Prośba o podanie współrzędnych ruchu
                String input = scanner.nextLine(); // Odczytanie współrzędnych od użytkownika

                // Jeśli ruch został wykonany, wyślij do serwera
                if (!input.trim().isEmpty()) { // Sprawdzanie, czy użytkownik podał jakieś dane
                    writer.println(input); // Wysłanie współrzędnych do serwera
                }
            }

        } catch (IOException e) { // Obsługa wyjątku w przypadku błędu połączenia
            e.printStackTrace(); // Wydrukowanie szczegółów błędu
        }
    }

    // Metoda pomocnicza, aby ułatwić uruchomienie klienta bezpośrednio
    public static void startClient(String serverAddress, int serverPort) { // Statyczna metoda do uruchomienia klienta
        TicTacToeClient client = new TicTacToeClient(serverAddress, serverPort); // Tworzenie nowego klienta
        client.startClient(); // Uruchomienie klienta
    }
}
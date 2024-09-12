package org.example; // Deklaracja pakietu, w którym znajduje się klasa

public class TicTacToe { // Deklaracja głównej klasy aplikacji
    public static void main(String[] args) { // Główna metoda aplikacji
        // Sprawdzenie, czy podano jakiekolwiek argumenty
        if (args.length > 0) {
            // Wykonanie odpowiednich działań w zależności od argumentu
            switch (args[0]) { // Sprawdzanie, który tryb gry został podany w argumencie
                case "hotseats": // Tryb hotseats (gra lokalna dwóch graczy na jednym urządzeniu)
                    LocalBoard localBoard = new LocalBoard(); // Tworzenie lokalnej planszy do gry
                    localBoard.start(); // Rozpoczęcie gry
                    break;

                case "server": // Tryb serwera (oczekiwanie na graczy)
                    RemoteBoard serverBoard = new RemoteBoard(12345); // Tworzenie zdalnej planszy (serwera) na porcie 12345
                    serverBoard.start(); // Rozpoczęcie nasłuchiwania na graczy
                    break;

                case "client": // Tryb klienta (łączenie się do serwera)
                    TicTacToeClient.startClient("localhost", 12345); // Uruchomienie klienta i połączenie z serwerem działającym na localhost:12345
                    break;

                case "host": // Tryb hosta (uruchomienie serwera i klienta jednocześnie na jednym urządzeniu)
                    // Uruchomienie serwera w osobnym wątku
                    Thread serverThread = new Thread(() -> { // Tworzenie nowego wątku dla serwera
                        RemoteBoard hostServer = new RemoteBoard(12345); // Tworzenie serwera na porcie 12345
                        hostServer.start(); // Rozpoczęcie nasłuchiwania
                    });
                    serverThread.start(); // Uruchomienie wątku serwera

                    // Poczekanie, aby serwer miał czas na start
                    try {
                        Thread.sleep(2000); // Pauza na 2 sekundy, aby dać serwerowi czas na uruchomienie
                    } catch (InterruptedException e) { // Obsługa przerwania wątku
                        e.printStackTrace(); // Wydrukowanie szczegółów błędu
                    }

                    // Uruchomienie klienta, który łączy się z lokalnym serwerem
                    TicTacToeClient.startClient("localhost", 12345); // Połączenie klienta z lokalnie uruchomionym serwerem
                    break;

                default: // Jeśli podano nieznany tryb gry
                    // Informacja o nieznanym trybie
                    System.out.println("Nieznany tryb gry. Użyj 'hotseats', 'server', 'client' lub 'host'.");
                    break;
            }
        } else { // Jeśli nie podano żadnych argumentów
            // Informacja o braku argumentów
            System.out.println("Brak argumentów. Użyj 'hotseats', 'server', 'client' lub 'host'.");
        }
    }
}
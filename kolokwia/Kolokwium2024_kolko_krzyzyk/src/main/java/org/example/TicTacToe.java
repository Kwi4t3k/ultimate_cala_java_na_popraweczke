package org.example;

public class TicTacToe {
    public static void main(String[] args) {
        // Sprawdzenie, czy podano jakiekolwiek argumenty
        if (args.length > 0) {
            // Wykonanie odpowiednich działań w zależności od argumentu
            switch (args[0]) {
                case "hotseats":
                    // Uruchomienie gry lokalnej (tryb hotseat)
                    LocalBoard localBoard = new LocalBoard();
                    localBoard.start();
                    break;

                case "server":
                    // Uruchomienie serwera na porcie 12345
                    RemoteBoard serverBoard = new RemoteBoard(12345); // Ustaw port serwera
                    serverBoard.start();
                    break;

                case "client":
                    // Uruchomienie klienta, który łączy się z serwerem działającym na localhost:12345
                    TicTacToeClient.startClient("localhost", 12345); // Adres i port serwera
                    break;

                case "host":
                    // Uruchomienie serwera w osobnym wątku
                    Thread serverThread = new Thread(() -> {
                        RemoteBoard hostServer = new RemoteBoard(12345);
                        hostServer.start();
                    });
                    serverThread.start();

                    // Poczekanie, aby serwer miał czas na start
                    try {
                        Thread.sleep(2000); // Dostosuj czas w zależności od potrzeb
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Uruchomienie klienta, który łączy się z lokalnym serwerem
                    TicTacToeClient.startClient("localhost", 12345);
                    break;

                default:
                    // Informacja o nieznanym trybie
                    System.out.println("Nieznany tryb gry. Użyj 'hotseats', 'server', 'client' lub 'host'.");
                    break;
            }
        } else {
            // Informacja o braku argumentów
            System.out.println("Brak argumentów. Użyj 'hotseats', 'server', 'client' lub 'host'.");
        }
    }
}
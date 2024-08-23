package org.example.Client;

import java.io.*; // Importuje klasy wejścia/wyjścia potrzebne do pracy z plikami i strumieniami.
import java.util.Scanner; // Importuje klasę Scanner do wczytywania danych z wejścia.

public class Client { // Deklaracja klasy Client, która reprezentuje aplikację kliencką.
    public static void main(String[] args) { // Główna metoda programu, która jest punktem wejścia aplikacji.
        Scanner scanner = new Scanner(System.in); // Tworzy obiekt Scanner do wczytywania danych ze standardowego wejścia (konsoli).

        System.out.println("Podaj imię"); // Wyświetla komunikat do użytkownika, prosząc o podanie imienia.
        String userName = scanner.nextLine(); // Wczytuje imię użytkownika z konsoli i zapisuje je w zmiennej userName.

        System.out.println("Podaj ścieżkę"); // Wyświetla komunikat do użytkownika, prosząc o podanie ścieżki do pliku CSV.
        String path = scanner.nextLine(); // Wczytuje ścieżkę do pliku CSV z konsoli i zapisuje ją w zmiennej path.

        Client client = new Client(); // Tworzy nową instancję klasy Client.

        client.sendData(userName, path); // Wywołuje metodę sendData na obiekcie client, aby wysłać dane na serwer.
    }

    ServerHandler serverHandler = new ServerHandler("localhost", 6000); // Tworzy obiekt ServerHandler, który będzie odpowiedzialny za komunikację z serwerem na lokalnym hoście i porcie 6000.

    public void sendData(String name, String csvPath) { // Definicja metody sendData, która przyjmuje nazwę użytkownika i ścieżkę do pliku CSV.
        try { // Blok try, aby przechwycić wyjątki, które mogą wystąpić podczas wykonywania operacji I/O.
            serverHandler.send(name); // Wysyła nazwę użytkownika na serwer za pomocą metody send obiektu serverHandler.

            Scanner fileScanner = new Scanner(new File(csvPath)); // Tworzy obiekt Scanner do odczytu pliku CSV ze ścieżki podanej przez użytkownika.

            while (fileScanner.hasNextLine()) { // Pętla while, która iteruje przez każdą linię w pliku CSV.
                String line = fileScanner.nextLine(); // Odczytuje kolejną linię z pliku CSV.
                serverHandler.send(line); // Wysyła odczytaną linię na serwer.
                Thread.sleep(2000); // Wstrzymuje wykonanie programu na 2000 milisekund (2 sekundy), aby spełnić wymaganie zadania.
            }

            serverHandler.send("bye"); // Wysyła wiadomość "bye" na serwer, sygnalizując zakończenie transmisji danych.
            serverHandler.close(); // Zamyka połączenie z serwerem po zakończeniu transmisji.

        } catch (InterruptedException | FileNotFoundException e) { // Przechwytuje wyjątki InterruptedException lub FileNotFoundException, które mogą wystąpić w bloku try.
            throw new RuntimeException(e); // Rzuca nowe niekontrolowane wyjątki, jeśli wystąpiły błędy podczas operacji I/O.
        }
    }
}
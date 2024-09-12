package org.example.kolos_2023_odczytpliku_zapispliku_java_fx;

import java.io.*;  // Importuje klasy do operacji wejścia/wyjścia
import java.net.Socket;  // Importuje klasę Socket do komunikacji sieciowej

public class Client {

    // Metoda do nawiązania połączenia z serwerem
    public static Socket connectToServer(String address, int port) {
        try {
            // Tworzy nowe połączenie z serwerem na określonym adresie i porcie
            Socket socket = new Socket(address, port);
            return socket;
        } catch (IOException e) {
            // Rzuca wyjątek w przypadku błędu połączenia
            throw new RuntimeException(e);
        }
    }

    // Metoda do wysyłania pliku do serwera
    public static void send(String path, Socket socket) {
        try {
            // Tworzy obiekt File dla określonej ścieżki pliku
            File file = new File(path);
            // Tworzy strumień wejściowy do odczytu zawartości pliku
            FileInputStream input = new FileInputStream(file);
            // Tworzy strumień wyjściowy do wysyłania danych do serwera
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // Ustawia bufor o rozmiarze 8192 bajtów
            byte[] buffer = new byte[8192];
            int count;
            // Wysyła długość pliku jako pierwszą informację
            output.writeLong(file.length());
            // Odczytuje dane z pliku i wysyła je do serwera w kawałkach
            while ((count = input.read(buffer)) != -1)
                output.write(buffer, 0, count);

            // Wymusza zapisanie wszystkich danych do strumienia
            output.flush();

            // Informuje o zakończeniu wysyłania
            System.out.println("File sent.");

        } catch (IOException e) {
            // Rzuca wyjątek w przypadku błędu operacji wejścia/wyjścia
            throw new RuntimeException(e);
        }
    }

    // Metoda do odbierania pliku od serwera
    public static void receive(Socket socket, String path) {
        try {
            // Tworzy strumień wejściowy do odbierania danych z serwera
            DataInputStream input = new DataInputStream(socket.getInputStream());
            // Tworzy strumień wyjściowy do zapisywania danych do pliku
            FileOutputStream output = new FileOutputStream(path);

            // Ustawia bufor o rozmiarze 8192 bajtów
            byte[] buffer = new byte[8192];
            int count;
            // Zmienna do śledzenia rozmiaru odebranego pliku
            int receivedSize = 0;
            // Odczytuje długość pliku, którą wcześniej wysłał serwer
            long fileSize = input.readLong();

            // Odbiera dane i zapisuje je do pliku, aż do osiągnięcia pełnego rozmiaru pliku
            while (receivedSize < fileSize) {
                count = input.read(buffer);
                output.write(buffer, 0, count);
                // Wyświetla liczbę odebranych bajtów
                System.out.println(count);
                receivedSize += count;
            }
            // Zamyka strumień wyjściowy po zakończeniu zapisywania
            output.close();

            // Informuje o zakończeniu odbierania
            System.out.println("File received.");

        } catch (IOException e) {
            // Rzuca wyjątek w przypadku błędu operacji wejścia/wyjścia
            throw new RuntimeException(e);
        }
    }

    // Metoda main do uruchomienia klienta
    public static void main(String[] args) {
        // Nawiązuje połączenie z serwerem działającym na localhost i porcie 5000
        Socket socket = connectToServer("localhost", 5000);
        // Wysyła plik input.png do serwera
        send("input.png", socket);
        // Odbiera plik od serwera i zapisuje go jako output.png
        receive(socket, "output.png");
    }
}
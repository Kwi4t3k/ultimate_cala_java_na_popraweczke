package org.example.Client; // Deklaracja pakietu, w którym znajduje się klasa ServerHandler

import java.io.IOException; // Import klasy IOException do obsługi wyjątków związanych z operacjami wejścia/wyjścia
import java.io.PrintWriter; // Import klasy PrintWriter do wysyłania danych tekstowych przez strumień wyjściowy
import java.net.Socket; // Import klasy Socket do obsługi połączeń sieciowych

public class ServerHandler { // Definicja klasy ServerHandler, która zarządza połączeniem z serwerem
    private Socket socket; // Deklaracja zmiennej socket typu Socket, reprezentującej połączenie z serwerem
    private PrintWriter output; // Deklaracja zmiennej output typu PrintWriter do wysyłania wiadomości do serwera

    public ServerHandler(String serverAddress, int serverPort) { // Konstruktor klasy ServerHandler, który inicjalizuje połączenie z serwerem
        try { // Blok try do obsługi wyjątków, które mogą wystąpić podczas inicjalizacji połączenia
            this.socket = new Socket(serverAddress, serverPort); // Utworzenie nowego gniazda (socket) do połączenia z serwerem pod podanym adresem i portem
            this.output = new PrintWriter(socket.getOutputStream(), true); // Inicjalizacja PrintWriter do wysyłania danych na strumień wyjściowy gniazda
        } catch (IOException e) { // Blok catch do przechwytywania wyjątków IOException
            throw new RuntimeException(e); // Rzucenie nowego wyjątku RuntimeException, jeśli wystąpił błąd przy tworzeniu połączenia lub strumienia
        }
    }

    public void send(String message) { // Metoda do wysyłania wiadomości do serwera
        output.println(message); // Wysłanie wiadomości przez strumień wyjściowy do serwera
    }

    public void close(){ // Metoda do zamykania połączenia z serwerem
        try { // Blok try do obsługi wyjątków, które mogą wystąpić podczas zamykania połączenia
            socket.close(); // Zamknięcie gniazda (socket), co kończy połączenie z serwerem
        } catch (IOException e) { // Blok catch do przechwytywania wyjątków IOException
            throw new RuntimeException(e); // Rzucenie nowego wyjątku RuntimeException, jeśli wystąpił błąd przy zamykaniu połączenia
        }
    }
}
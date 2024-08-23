package org.example.Server; // Deklaracja pakietu, w którym znajduje się klasa

import java.io.IOException; // Import klasy IOException, używanej do obsługi wyjątków I/O
import java.net.Socket; // Import klasy Socket, używanej do komunikacji sieciowej
import java.util.Scanner; // Import klasy Scanner, używanej do odczytywania danych wejściowych z wejścia strumieniowego

public class ClientHandler implements Runnable { // Definicja klasy ClientHandler, która implementuje interfejs Runnable, aby mogła działać w wątku
    private Socket socket; // Deklaracja zmiennej socket typu Socket, reprezentującej połączenie z klientem
    private Server server; // Deklaracja zmiennej server typu Server, referencja do serwera obsługującego tego klienta
    private Scanner input; // Deklaracja zmiennej input typu Scanner, używanej do odczytywania danych wejściowych od klienta
    private String clientName; // Deklaracja zmiennej clientName typu String, przechowującej nazwę klienta
    private int nElectrode; // Deklaracja zmiennej nElectrode typu int, licznika numeru elektrody przetwarzanej dla danego klienta

    public ClientHandler(Socket socket, Server server) { // Konstruktor klasy ClientHandler przyjmujący obiekt Socket i referencję do Server
        try { // Blok try do obsługi potencjalnych wyjątków
            this.socket = socket; // Inicjalizacja pola socket wartością przekazaną w parametrze konstruktora
            this.server = server; // Inicjalizacja pola server wartością przekazaną w parametrze konstruktora
            input = new Scanner(socket.getInputStream()); // Inicjalizacja obiektu Scanner używając wejścia strumieniowego z gniazda (Socket)
        } catch (IOException e) { // Blok catch do przechwytywania wyjątków IOException
            throw new RuntimeException(e); // Rzucenie nowego wyjątku RuntimeException w przypadku wystąpienia wyjątku I/O
        }
    }

    @Override
    public void run() { // Implementacja metody run() z interfejsu Runnable, która zawiera logikę dla wątku
        try { // Blok try do obsługi potencjalnych wyjątków I/O
            boolean connected = isClientConnected(); // Sprawdzenie, czy klient jest połączony i przypisanie wyniku do zmiennej connected

            if (!connected) { // Sprawdzenie, czy klient nie jest połączony
                return; // Zakończenie metody run(), jeśli klient nie jest połączony
            }

            join(); // Wywołanie metody join(), aby zarejestrować klienta na serwerze
            communicate(); // Wywołanie metody communicate(), aby rozpocząć komunikację z klientem
            disconnect(); // Wywołanie metody disconnect(), aby rozłączyć klienta i posprzątać zasoby
        } catch (IOException e) { // Blok catch do przechwytywania wyjątków IOException
            e.printStackTrace(); // Wypisanie śladu stosu błędu na standardowe wyjście błędów
        }
    }

    private boolean isClientConnected() { // Prywatna metoda sprawdzająca, czy klient jest połączony
        if (input.hasNextLine()) { // Sprawdzenie, czy jest następna linia wejściowa od klienta
            clientName = input.nextLine(); // Odczytanie nazwy klienta i przypisanie jej do zmiennej clientName
            return true; // Zwrócenie true, jeśli klient jest połączony
        }
        return false; // Zwrócenie false, jeśli klient nie jest połączony
    }

    private void join() { // Prywatna metoda do rejestracji klienta na serwerze
        server.addClient(clientName, this); // Dodanie klienta do listy klientów serwera
        server.printUsers(); // Wypisanie listy połączonych użytkowników na serwerze
    }

    private void communicate() { // Prywatna metoda do obsługi komunikacji z klientem
        String clientMessage; // Deklaracja zmiennej clientMessage typu String do przechowywania wiadomości od klienta
        boolean connected = true; // Inicjalizacja zmiennej connected na true, aby rozpocząć pętlę komunikacji

        while (connected) { // Pętla while do komunikacji z klientem, dopóki connected jest true
            if (input.hasNextLine()) { // Sprawdzenie, czy jest następna linia wejściowa od klienta
                clientMessage = input.nextLine(); // Odczytanie wiadomości od klienta i przypisanie jej do zmiennej clientMessage
                connected = parseClientMessage(clientMessage); // Przetworzenie wiadomości klienta i aktualizacja zmiennej connected
            } else { // Jeśli nie ma następnej linii wejściowej
                connected = false; // Ustawienie connected na false, aby zakończyć pętlę
            }
        }
    }

    public boolean parseClientMessage(String clientMessage) { // Publiczna metoda do przetwarzania wiadomości od klienta
        if (!clientMessage.equalsIgnoreCase("bye")) { // Sprawdzenie, czy wiadomość klienta nie jest równa "bye" (ignorując wielkość liter)
            server.process(clientMessage, clientName); // Przetwarzanie wiadomości klienta na serwerze
            return true; // Zwrócenie true, jeśli wiadomość nie była "bye"
        }
        return false; // Zwrócenie false, jeśli wiadomość była "bye"
    }

    private void disconnect() throws IOException { // Prywatna metoda do rozłączenia klienta i posprzątania zasobów
        server.removeClient(clientName); // Usunięcie klienta z listy klientów serwera
        socket.close(); // Zamknięcie gniazda połączenia z klientem
        server.printUsers(); // Wypisanie listy połączonych użytkowników na serwerze
    }

    public int getAndIncrease() { // Publiczna metoda do pobierania aktualnego numeru elektrody i zwiększania go o jeden
        int i = this.nElectrode; // Przypisanie aktualnej wartości nElectrode do zmiennej i
        nElectrode++; // Zwiększenie licznika nElectrode o jeden
        return i; // Zwrócenie aktualnego numeru elektrody
    }
}
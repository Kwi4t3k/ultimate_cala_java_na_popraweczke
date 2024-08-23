package org.example.Server; // Deklaracja pakietu, w którym znajduje się klasa Server

import javax.imageio.ImageIO; // Import klasy ImageIO do operacji wejścia/wyjścia na obrazach
import java.awt.*; // Import klasy Graphics i innych klas związanych z grafiką
import java.awt.image.BufferedImage; // Import klasy BufferedImage do reprezentacji obrazu w pamięci
import java.io.ByteArrayOutputStream; // Import klasy ByteArrayOutputStream do operacji na bajtach strumienia
import java.io.IOException; // Import klasy IOException do obsługi wyjątków wejścia/wyjścia
import java.net.ServerSocket; // Import klasy ServerSocket do tworzenia serwera nasłuchującego na określonym porcie
import java.net.Socket; // Import klasy Socket do obsługi połączenia z klientem
import java.sql.Connection; // Import klasy Connection do zarządzania połączeniami z bazą danych
import java.sql.DriverManager; // Import klasy DriverManager do zarządzania sterownikami baz danych
import java.sql.PreparedStatement; // Import klasy PreparedStatement do wykonywania zapytań SQL z parametrami
import java.sql.SQLException; // Import klasy SQLException do obsługi wyjątków SQL
import java.util.Arrays; // Import klasy Arrays do operacji na tablicach
import java.util.Base64; // Import klasy Base64 do kodowania danych binarnych w formacie Base64
import java.util.HashMap; // Import klasy HashMap do przechowywania par klucz-wartość
import java.util.Map; // Import interfejsu Map do przechowywania i manipulowania mapami

public class Server { // Definicja klasy Server, która będzie pełnić rolę serwera nasłuchującego połączeń klientów
    private String databaseURL = "jdbc:sqlite:src/main/resources/usereeg.db"; // Deklaracja i inicjalizacja URL bazy danych SQLite
    private ServerSocket serverSocket; // Deklaracja zmiennej serverSocket typu ServerSocket, reprezentującej gniazdo serwera
    private boolean running = false; // Deklaracja zmiennej running typu boolean, kontrolującej stan pracy serwera
    private Map<String, ClientHandler> clientHandlers = new HashMap<>(); // Mapa do przechowywania obiektów ClientHandler związanych z klientami

    public static void main(String[] args) { // Metoda główna, punkt wejścia do aplikacji
        Server server = new Server(); // Tworzenie nowej instancji klasy Server
        server.start(6000); // Uruchamianie serwera na porcie 6000
    }

    public void start(int port){ // Metoda start, która uruchamia serwer na określonym porcie
        try { // Blok try do obsługi wyjątków
            serverSocket = new ServerSocket(port); // Inicjalizacja gniazda serwera na określonym porcie
            System.out.println("Server started on port: " + port); // Wypisanie informacji o uruchomieniu serwera
            running = true; // Ustawienie flagi running na true, co oznacza, że serwer jest uruchomiony

            while (running) { // Pętla while, która działa dopóki serwer jest uruchomiony
                Socket socket = serverSocket.accept(); // Akceptowanie połączeń od klientów i tworzenie nowego gniazda dla każdego połączenia
                System.out.println("Client connected"); // Wypisanie informacji o połączeniu klienta

                ClientHandler clientHandler = new ClientHandler(socket, this); // Tworzenie nowego obiektu ClientHandler dla każdego połączenia
                new Thread(clientHandler).start(); // Uruchamianie obiektu ClientHandler w nowym wątku
            }
            serverSocket.close(); // Zamknięcie gniazda serwera po zakończeniu pętli
        } catch (IOException e) { // Blok catch do przechwytywania wyjątków IOException
            throw new RuntimeException(e); // Rzucenie nowego wyjątku RuntimeException w przypadku wystąpienia błędu wejścia/wyjścia
        }
    }

    public void addClient(String clientName, ClientHandler clientHandler) { // Metoda do dodawania klienta do mapy clientHandlers
        clientHandlers.put(clientName, clientHandler); // Dodanie nowej pary (nazwa klienta, obiekt ClientHandler) do mapy clientHandlers
    }

    public void process(String message, String userName){ // Metoda do przetwarzania wiadomości od klienta
        int electrode = clientHandlers.get(userName).getAndIncrease(); // Pobranie i zwiększenie numeru elektrody dla danego klienta
        System.out.println("processing: " + message + " " + userName + " electrode: " + electrode); // Wypisanie informacji o przetwarzanej wiadomości
        saveToDataBase(message, userName, electrode); // Zapisanie przetworzonej wiadomości do bazy danych
    }

    public void printUsers() { // Metoda do wypisania listy połączonych użytkowników
        System.out.println("Users connected: " + clientHandlers.keySet()); // Wypisanie nazw połączonych użytkowników
    }

    public void removeClient(String clientName){ // Metoda do usuwania klienta z listy połączonych użytkowników
        ClientHandler userThread = clientHandlers.remove(clientName); // Usunięcie obiektu ClientHandler dla danego klienta z mapy clientHandlers
        if (userThread != null) { // Sprawdzenie, czy klient istniał w mapie
            System.out.println("The user " + clientName + " finished"); // Wypisanie informacji o zakończeniu połączenia przez klienta
        }
    }

    public void saveToDataBase(String message, String userName, int electrode) { // Metoda do zapisywania danych do bazy danych
        String chart = drawChart(message); // Generowanie obrazu (wykresu) na podstawie wiadomości
        String insertSQL = "INSERT INTO user_eeg(username, electrode_number, image) VALUES(?, ?, ?)"; // SQL do wstawiania nowych danych do tabeli

        try (Connection connection = DriverManager.getConnection(databaseURL); // Użycie połączenia z bazą danych
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) { // Przygotowanie zapytania SQL

            preparedStatement.setString(1, userName); // Ustawienie wartości parametru 1 (username)
            preparedStatement.setInt(2, electrode); // Ustawienie wartości parametru 2 (electrode_number)
            preparedStatement.setString(3, chart); // Ustawienie wartości parametru 3 (image w formacie Base64)

            preparedStatement.executeUpdate(); // Wykonanie zapytania SQL (INSERT)
            System.out.println("Data saved."); // Wypisanie informacji o zapisaniu danych
        } catch (SQLException e) { // Blok catch do przechwytywania wyjątków SQLException
            System.err.println(e.getMessage()); // Wypisanie wiadomości błędu na standardowe wyjście błędów
        }
    }

    public String drawChart(String message) { // Metoda do generowania wykresu na podstawie wiadomości
        int height = 100; // Ustawienie wysokości obrazu
        int width = 200; // Ustawienie szerokości obrazu

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); // Utworzenie nowego obrazu w pamięci
        Graphics2D graphics2D = bufferedImage.createGraphics(); // Pobranie kontekstu graficznego do rysowania na obrazie

        graphics2D.setColor(Color.white); // Ustawienie koloru na biały
        graphics2D.fillRect(0, 0, width, height); // Wypełnienie całego obrazu białym kolorem

        graphics2D.setColor(Color.red); // Ustawienie koloru na czerwony

        String[] messageParts = message.split(","); // Podzielenie wiadomości na części, zakładając, że jest to ciąg liczb oddzielonych przecinkami
        double[] data = Arrays.stream(messageParts) // Konwersja tablicy Stringów na tablicę double
                .mapToDouble(Double::parseDouble) // Parsowanie każdej części jako double
                .toArray(); // Konwersja strumienia na tablicę

        for (int i = 0; i < data.length; i++) { // Pętla do rysowania punktów na wykresie
            int y = height / 2 - (int) (data[i]); // Obliczenie pozycji Y na podstawie wartości danych
            graphics2D.drawRect(i, y, 1, 1); // Rysowanie pojedynczego punktu (prostokąta 1x1) na obrazie
        }
        return encodeImageToBase64(bufferedImage); // Kodowanie obrazu na Base64 i zwracanie go jako String
    }

    private static String encodeImageToBase64(BufferedImage bufferedImage) { // Prywatna metoda do kodowania obrazu w formacie Base64

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) { // Użycie ByteArrayOutputStream do zapisywania obrazu w pamięci
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream); // Zapisanie obrazu do strumienia w formacie PNG
            byte[] imageBytes = byteArrayOutputStream.toByteArray(); // Pobranie bajtów obrazu ze strumienia
            return Base64.getEncoder().encodeToString(imageBytes); // Kodowanie bajtów na Base64 i zwracanie jako String

        } catch (IOException e) { // Blok catch do przechwytywania wyjątków IOException
            e.printStackTrace(); // Wypisanie śladu stosu błędu na standardowe wyjście błędów
            return null; // Zwrócenie null w przypadku błędu
        }
    }
}
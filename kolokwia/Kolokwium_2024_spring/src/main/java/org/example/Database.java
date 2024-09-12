package org.example;

import org.apache.tomcat.jni.Pool;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Klasa Database zarządza połączeniem z bazą danych SQLite i operacjami na tabelach
public class Database {
    // Stałe określające sterownik JDBC i adres URL bazy danych
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:pixel.db";

    // Obiekty do zarządzania połączeniem i zapytaniami SQL
    private Connection connection;
    private Statement statement;

    // Singleton - statyczne odniesienie do instancji bazy danych
    private static Database instance;

    // Prywatny konstruktor - zapobiega tworzeniu wielu instancji
    private Database() {
        // Nawiązanie połączenia z bazą danych
        connectionOfDatabase();
        // Tworzenie tabeli w bazie danych (jeśli nie istnieje)
        createTable();
    }

    // Metoda nawiązująca połączenie z bazą danych SQLite
    private void connectionOfDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL); // Nawiązywanie połączenia z bazą danych
            statement = connection.createStatement(); // Tworzenie obiektu do wykonywania zapytań
        } catch (SQLException e) {
            System.err.println("Problem z otwarciem połączenia"); // Obsługa błędu połączenia
            e.printStackTrace();
        }
    }

    // Statyczna metoda zwracająca instancję klasy (singleton)
    public static Database getInstance() {
        if (instance == null) { // Sprawdzenie, czy instancja już istnieje
            instance = new Database(); // Jeśli nie, tworzy nową
        }
        return instance; // Zwracanie instancji
    }

    // Tworzenie tabeli do przechowywania pikseli w bazie danych
    private void createTable() {
        // SQL do tworzenia tabeli, jeśli nie istnieje
        String createMyTable = "CREATE TABLE IF NOT EXISTS entry (token INTEGER NOT NULL, x INTEGER NOT NULL, y INTEGER NOT NULL, color TEXT NOT NULL, timestamp TEXT NOT NULL);";

        try {
            statement.execute(createMyTable); // Wykonanie zapytania SQL
        } catch (SQLException e) {
            System.err.println("Błąd przy tworzeniu tabeli"); // Obsługa błędu
            e.printStackTrace();
        }
    }

    // KROK 8: Usuwanie rekordów powiązanych z tokenem
    public int removeRecords(int id) {
        // SQL do usuwania rekordów dla określonego tokena
        String delete = "DELETE FROM entry WHERE token=?";

        try {
            // Przygotowanie zapytania SQL
            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setInt(1, id); // Ustawienie parametru tokena

            // Wykonanie zapytania i zwrócenie liczby usuniętych rekordów
            int records = preparedStatement.executeUpdate();
            return records;
        } catch (SQLException e) {
            throw new RuntimeException(); // Obsługa błędu SQL
        }
    }

    // Dodawanie informacji o pikselu do bazy danych
    public void addPixelToDatabase(int tokenId, int x, int y, String hexColor) {
        // SQL do wstawiania nowego rekordu do tabeli
        String insert = "INSERT INTO entry (token, x, y, color, timestamp) VALUES(?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(insert); // Przygotowanie zapytania

            // Ustawianie parametrów zapytania
            preparedStatement.setInt(1, tokenId);
            preparedStatement.setInt(2, x);
            preparedStatement.setInt(3, y);
            preparedStatement.setString(4, hexColor);
            preparedStatement.setString(5, LocalDateTime.now().toString()); // Aktualny czas

            preparedStatement.execute(); // Wykonanie zapytania

        } catch (SQLException e) {
            e.printStackTrace(); // Obsługa błędu
        }
    }

    // KROK 6: Pobieranie listy pikseli z bazy danych
    public List<Pixel> getListOfPixelsFromDatabase() {
        String select = "select * from entry"; // SQL do pobierania wszystkich rekordów z tabeli
        List<Pixel> pixels = new ArrayList<>(); // Lista pikseli do zwrócenia

        try {
            // Wykonanie zapytania i pobranie wyników
            ResultSet resultSet = statement.executeQuery(select);

            while (resultSet.next()) { // Przetwarzanie wyników
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                String color = resultSet.getString("color");

                pixels.add(new Pixel(x, y, color)); // Dodanie piksela do listy
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Obsługa błędu
        }

        return pixels; // Zwracanie listy pikseli
    }

    // Zamknięcie połączenia z bazą danych
    public void closeConnection() {
        try {
            connection.close(); // Zamknięcie połączenia
        } catch (SQLException e) {
            System.err.println("Problem z zamknięciem połączenia"); // Obsługa błędu
            e.printStackTrace();
        }
    }
}
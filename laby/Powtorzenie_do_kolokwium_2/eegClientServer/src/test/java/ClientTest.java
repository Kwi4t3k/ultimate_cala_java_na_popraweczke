// Importowanie klasy Client z pakietu org.example.Client. Klasa ta reprezentuje klienta, który wysyła dane do serwera.
import org.example.Client.Client;

// Importowanie klasy Creator z pakietu org.example.DatabaseCreator. Klasa ta służy do tworzenia i zarządzania bazą danych.
import org.example.DatabaseCreator.Creator;

// Importowanie klasy Server z pakietu org.example.Server. Klasa ta reprezentuje serwer, który odbiera dane od klienta.
import org.example.Server.Server;

// Importowanie odpowiednich klas z biblioteki JUnit do przeprowadzania testów jednostkowych.
import org.junit.jupiter.api.AfterAll; // Anotacja wskazująca, że metoda ma zostać wykonana po wszystkich testach.
import org.junit.jupiter.api.BeforeAll; // Anotacja wskazująca, że metoda ma zostać wykonana przed wszystkimi testami.
import org.junit.jupiter.params.ParameterizedTest; // Anotacja umożliwiająca wykonanie testu z wieloma różnymi danymi wejściowymi.
import org.junit.jupiter.params.provider.CsvFileSource; // Umożliwia użycie danych z pliku CSV jako źródła danych dla testu sparametryzowanego.

// Importowanie klas potrzebnych do obsługi bazy danych SQL (Connection, DriverManager, PreparedStatement, ResultSet).
import java.sql.*;

// Importowanie statyczne metody assertEquals z klasy Assertions, aby móc używać jej bez konieczności poprzedzania nazwą klasy.
import static org.junit.jupiter.api.Assertions.assertEquals;

// Deklaracja publicznej klasy ClientTest, która zawiera testy jednostkowe dotyczące klasy Client.
public class ClientTest {

    // Deklaracja zmiennej statycznej server, która będzie przechowywać instancję serwera działającego podczas testów.
    private static Server server;

    // Tworzenie statycznej instancji klasy Creator, która będzie używana do zarządzania bazą danych (tworzenie i usuwanie).
    private static Creator creator = new Creator();

    // Statyczna zmienna przechowująca ścieżkę URL do pliku bazy danych SQLite, która będzie używana w trakcie testów.
    private static String dbURL = "jdbc:sqlite:src/main/resources/usereeg.db";

    // Metoda oznaczona jako @BeforeAll, która zostanie uruchomiona raz przed wszystkimi testami.
    // Służy do przygotowania środowiska testowego (tj. stworzenia bazy danych i uruchomienia serwera).
    @BeforeAll
    public static void setup() {
        // Wywołanie metody create() na obiekcie creator w celu stworzenia nowej bazy danych pod podanym adresem URL.
        creator.create(dbURL);

        // Inicjalizacja instancji serwera, który będzie używany podczas testów.
        server = new Server();

        // Ustawienie URL bazy danych dla serwera, tak aby mógł on komunikować się z odpowiednią bazą danych.
        server.setDatabaseURL(dbURL);

        // Uruchomienie serwera w nowym wątku, aby działał równolegle z testami i nie blokował głównego wątku testowego.
        new Thread(() -> server.start(6000)).start();
    }

    // Metoda oznaczona jako @AfterAll, która zostanie uruchomiona raz po zakończeniu wszystkich testów.
    // Służy do wyczyszczenia środowiska testowego (tj. zatrzymania serwera i usunięcia bazy danych).
    @AfterAll
    public static void stop() {
        // Wywołanie metody stop() na instancji serwera w celu zatrzymania jego działania po zakończeniu testów.
        server.stop();

        // Wywołanie metody delete() na obiekcie creator w celu usunięcia pliku bazy danych po zakończeniu testów.
        creator.delete(dbURL);
    }

    // Sparametryzowany test jednostkowy, który wykonuje testy z różnymi danymi wejściowymi pobranymi z pliku CSV.
    // Każda linia pliku CSV stanowi zestaw danych wejściowych dla jednego wykonania testu.
    @ParameterizedTest
    @CsvFileSource(files = "src/main/resources/test.csv", numLinesToSkip = 1)
    public void clientTest(String userName, String filePath, int electrode, String image) {
        // Tworzenie nowej instancji klasy Client, która będzie używana do wysyłania danych do serwera w trakcie testu.
        Client client = new Client();

        // Wywołanie metody sendData() na obiekcie klienta, aby wysłać dane EEG (plik CSV) do serwera.
        client.sendData(userName, filePath);

        // Wywołanie metody getImage(), która pobiera z bazy danych obraz EEG na podstawie nazwy użytkownika i numeru elektrody.
        String realImage = getImage(userName, electrode);

        // Sprawdzenie, czy obraz pobrany z bazy danych (realImage) jest równy oczekiwanemu obrazowi (image) podanemu w danych testowych.
        assertEquals(image, realImage);
    }

    // Metoda pomocnicza, która pobiera obraz EEG z bazy danych na podstawie nazwy użytkownika i numeru elektrody.
    public String getImage(String userName, int electrode) {
        // Deklaracja zmiennej image, która będzie przechowywać obraz pobrany z bazy danych. Na początku jest ustawiona na null.
        String image = null;

        // Definicja zapytania SQL do pobrania obrazu z tabeli user_eeg, gdzie nazwa użytkownika i numer elektrody są zgodne z podanymi wartościami.
        String query = "SELECT image FROM user_eeg WHERE username = ? AND electrode_number = ?";

        // Użycie bloku try-with-resources, który automatycznie zamyka połączenie i zasoby po zakończeniu ich użycia.
        try (Connection connection = DriverManager.getConnection(dbURL); // Nawiązanie połączenia z bazą danych za pomocą URL.
             PreparedStatement statement = connection.prepareStatement(query)) { // Przygotowanie zapytania SQL z parametrami.

            // Ustawienie pierwszego parametru zapytania SQL (nazwa użytkownika) na wartość przekazaną do metody.
            statement.setString(1, userName);

            // Ustawienie drugiego parametru zapytania SQL (numer elektrody) na wartość przekazaną do metody.
            statement.setInt(2, electrode);

            // Wykonanie zapytania SQL i pobranie wyników do obiektu ResultSet.
            try (ResultSet rs = statement.executeQuery()) {
                // Sprawdzenie, czy wynik zawiera przynajmniej jeden wiersz.
                if (rs.next()) {
                    // Jeśli wynik istnieje, pobieramy wartość kolumny "image" i przypisujemy ją do zmiennej image.
                    image = rs.getString("image");
                }
            }
        } catch (SQLException e) {
            // W przypadku wystąpienia wyjątku SQL (błędu podczas pracy z bazą danych), drukujemy szczegóły błędu.
            e.printStackTrace();
        }

        // Zwracamy wartość zmiennej image (może być null, jeśli obraz nie został znaleziony w bazie danych).
        return image;
    }
}
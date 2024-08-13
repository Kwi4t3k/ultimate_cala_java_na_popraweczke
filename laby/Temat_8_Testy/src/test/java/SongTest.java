import org.example.database.DatabaseConnection;
import org.example.music.Song;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SongTest {

    @BeforeAll
    public static void setup() {
        // Połączenie z bazą danych przed wykonaniem wszystkich testów
        DatabaseConnection.connect("src/main/resources/songs.db");
    }

    @AfterAll
    public static void close() {
        // Rozłączenie z bazą danych po wykonaniu wszystkich testów
        DatabaseConnection.disconnect();
    }

    // Metoda zwracająca strumień argumentów (indeks i oczekiwane wartości piosenek)
    private static Stream<Arguments> songs() {
        return Stream.of(
                Arguments.arguments(1, "The Beatles", "Hey Jude", 431),
                Arguments.arguments(2, "The Rolling Stones", "(I Can't Get No) Satisfaction", 224),
                Arguments.arguments(3, "Led Zeppelin", "Stairway to Heaven", 482)
        );
    }

    // Test sparametryzowany, który wykorzystuje strumień argumentów z metody 'songs'
    @ParameterizedTest
    @MethodSource("songs")
    public void streamTest(int index, String artist, String title, int length) throws SQLException {
        // Odczyt piosenki z bazy danych na podstawie indeksu
        Optional<Song> song = Song.Persistence.read(index);

        // Sprawdzenie, czy tytuł piosenki jest zgodny z oczekiwanym
        assertEquals(title, song.get().title());
    }

    // Test sparametryzowany plikiem CSV, który zawiera dane o piosenkach
    @ParameterizedTest
    @CsvFileSource(files = "src/main/resources/songs.csv", numLinesToSkip = 1)
    public void csvTest(int index, String artist, String title, int length) throws SQLException {
        // Odczyt piosenki z bazy danych na podstawie indeksu
        Optional<Song> song = Song.Persistence.read(index);

        // Sprawdzenie, czy tytuł piosenki jest zgodny z oczekiwanym
        assertEquals(title, song.get().title());
    }
}

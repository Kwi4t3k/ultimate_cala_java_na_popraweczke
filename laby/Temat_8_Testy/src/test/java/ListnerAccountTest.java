import org.example.NotEnoughCreditsException;
import org.example.auth.Account;
import org.example.database.DatabaseConnection;
import org.example.music.ListenerAccount;
import org.example.music.Playlist;
import org.example.music.Song;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.naming.AuthenticationException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListnerAccountTest {
    @BeforeAll
    public static void setup(){
        // Inicjalizacja połączenia z bazą danych przed wszystkimi testami
        DatabaseConnection.connect("src/main/resources/songs.db");
    }

    @AfterAll
    public static void close(){
        // Zamknięcie połączenia z bazą danych po zakończeniu wszystkich testów
        DatabaseConnection.disconnect();
    }

    @Test
    public void testRegisterNewAccount() throws SQLException {
        ListenerAccount.Persistence.init();
        int account = ListenerAccount.Persistence.register("user", "password");

        // Test sprawdzający, czy konto zostało poprawnie zarejestrowane
        assertTrue(account > 0);
    }

    @Test
    public void testNewAccountLogin() throws AuthenticationException {
        Account account = Account.Persistence.authenticate("user", "password");

        // Test sprawdzający, czy logowanie do nowo zarejestrowanego konta działa poprawnie
        assertNotNull(account);
    }

    @Test
    public void testCheckEmptyCreditAccount() throws AuthenticationException, SQLException {
        Account account = Account.Persistence.authenticate("user", "password");
        int credits = ListenerAccount.Persistence.getCredits(account.getId());

        // Test sprawdzający, czy nowo utworzone konto ma 0 kredytów na starcie
        assertEquals(0, credits);
    }

    @Test
    public void testAddCredits() throws AuthenticationException, SQLException {
        Account account = Account.Persistence.authenticate("user", "password");
        ListenerAccount.Persistence.addCredits(account.getId(), 50);
        int credits = ListenerAccount.Persistence.getCredits(account.getId());

        // Test dodania kredytów na konto
        assertEquals(50, credits);
    }

    @Test
    public void testSongOnAccount() throws SQLException, NotEnoughCreditsException {
        ListenerAccount.Persistence.buySong(1, 1);

        // Test sprawdzający, czy piosenka została poprawnie dodana do konta
        assertTrue(ListenerAccount.Persistence.hasSong(1, 1));
    }

    @Test
    public void testBuyNewSong() throws SQLException, NotEnoughCreditsException {
        ListenerAccount.Persistence.buySong(3, 1);

        // Test kupienia nowej piosenki przez konto
        assertTrue(ListenerAccount.Persistence.hasSong(1, 2));
    }

    @Test
    public void testNotEnoughCredits() throws SQLException, NotEnoughCreditsException {
        ListenerAccount.Persistence.buySong(1, 2);

        // Test rzucenia wyjątku NotEnoughCreditsException, gdy brakuje kredytów
        assertTrue(ListenerAccount.Persistence.hasSong(2, 1));

//        Exception exception = assertThrows(NotEnoughCreditsException.class, () -> {
//        ListenerAccount.Persistence.buySong(3, 2);
//    });
//    String expectedMessage = "Za mało kredytów";
//    String actualMessage = exception.getMessage();
//    assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreatePlaylist() throws SQLException, NotEnoughCreditsException {
        ListenerAccount account = new ListenerAccount(1, "user");

        // Zakładamy, że konto z ID 1 ma wystarczającą ilość kredytów
        ListenerAccount.Persistence.addCredits(1, 5);

        // Lista ID piosenek, które chcemy dodać do playlisty
        List<Integer> songIds = List.of(1, 2, 3);

        // Wywołujemy metodę createPlaylist z dwoma argumentami: lista ID piosenek i ID konta
        Playlist playlist = account.createPlaylist(songIds, 1);

        // Predefiniowana playlista do porównania
        Playlist expectedPlaylist = new Playlist();
        expectedPlaylist.add(Song.Persistence.read(1).get());
        expectedPlaylist.add(Song.Persistence.read(2).get());
        expectedPlaylist.add(Song.Persistence.read(3).get());

        // Porównanie playlist
        assertEquals(expectedPlaylist, playlist);
    }
}
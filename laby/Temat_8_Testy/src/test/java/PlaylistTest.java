import org.example.music.Playlist;
import org.example.music.Song;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaylistTest {
    @Test
    public void testIfPlaylistIsEmpty(){
        Playlist playlist = new Playlist();
        assertTrue(playlist.isEmpty());
    }

    @Test
    public void testIfPlaylistHasOneSong(){
        Playlist playlist = new Playlist();
        playlist.add(new Song("song", "artist", 10));
        assertEquals(1, playlist.size());
        //lub
//        assertTrue(playlist.size() == 1);
    }

    @Test
    public void testIfPlaylistHasThatSong() {
        Song song = new Song("title", "artist", 100);
        Playlist playlist = new Playlist();
        playlist.add(song);
        assertTrue(playlist.contains(song));
    }

    @Test
    public void testIfPlaylistHasSameSong() {
        Song song = new Song("title", "artist", 100);
        Playlist playlist = new Playlist();
        playlist.add(song);
        assertTrue(playlist.get(0).equals(song));
    }

    @Test
    public void testAtSecond(){
        Playlist playlist = new Playlist();
        Song song = new Song("title", "artist", 100);

        playlist.add(song);

        assertEquals(song, playlist.atSecond(0));
        assertEquals(song, playlist.atSecond(99));
    }

    @Test
    public void testAtSecondTooLittleTime(){
        Playlist playlist = new Playlist();
        Song song = new Song("title", "artist", 100);

        playlist.add(song);

        assertEquals(song, playlist.atSecond(-100));
    }

    @Test
    public void testAtSecondTooMuchTime(){
        Playlist playlist = new Playlist();
        Song song = new Song("title", "artist", 100);

        playlist.add(song);

        assertEquals(song, playlist.atSecond(100));
    }
}

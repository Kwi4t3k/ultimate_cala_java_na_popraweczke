package org.example.music;

import java.util.ArrayList;

public class Playlist extends ArrayList<Song> {

    // Metoda zwraca utwór, który jest odtwarzany po określonej liczbie sekund
    public Song atSecond(int second) {
        int sumTime = 0; // Suma czasu trwania wszystkich przetworzonych utworów

        // Sprawdzenie, czy podana wartość sekund nie jest ujemna
        if (second < 0) {
            throw new IndexOutOfBoundsException("Podano wartość ujemną: " + second);
        }

        // Iteracja przez wszystkie utwory na playliście
        for (Song song : this) {
            sumTime += song.duration(); // Dodawanie czasu trwania aktualnego utworu do sumy

            // Sprawdzenie, czy podana liczba sekund mieści się w czasie trwania aktualnego utworu
            if (second < sumTime) {
                return song; // Zwracamy utwór, który jest odtwarzany w tym momencie
            }
        }

        // Jeśli liczba sekund przekracza całkowity czas trwania playlisty, rzucamy wyjątek
        throw new IndexOutOfBoundsException("Przekroczono czas trwania playlisty: " + second);
    }
}
package org.example.music;

import org.example.database.DatabaseConnection;

import java.util.Optional;
import java.sql.*;

public record Song(
        String title,
        String artist,
        int duration) {

    public static class Persistence {
        public static Optional<Song> read(int index) throws SQLException {
            // Przygotowanie zapytania SQL do pobrania utworu z bazy danych na podstawie jego ID
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM song WHERE id = ?");

            // Ustawienie wartości ID (index) w zapytaniu SQL
            statement.setInt(1, index);

            // Wykonanie zapytania SQL
            statement.execute();

            // Pobranie wyniku zapytania do obiektu ResultSet
            ResultSet result = statement.getResultSet();

            // Tworzymy pusty Optional, który później zostanie wypełniony, jeśli znajdziemy utwór
            Optional<Song> song = Optional.empty();

            // Sprawdzenie, czy zapytanie zwróciło jakikolwiek rekord
            if (result.next()) {
                // Jeśli rekord istnieje, pobieramy jego dane z kolumn
                String artist = result.getString("artist");
                String title = result.getString("title");
                int duration = result.getInt("length");

                // Tworzymy nowy obiekt Song na podstawie danych i zawijamy go w Optional
                song = Optional.of(new Song(title, artist, duration));
            }

            // Zwracamy Optional z utworem, lub pusty Optional, jeśli nie znaleziono rekordu
            return song;
        }

    }
}

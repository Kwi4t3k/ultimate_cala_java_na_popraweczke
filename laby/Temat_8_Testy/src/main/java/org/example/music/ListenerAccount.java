package org.example.music;

import org.example.NotEnoughCreditsException;
import org.example.auth.Account;
import org.example.database.DatabaseConnection;

import javax.naming.AuthenticationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.example.music.ListenerAccount.Persistence.buySong;
import static org.example.music.ListenerAccount.Persistence.hasSong;

public class ListenerAccount extends Account {
    // Konstruktor, który inicjalizuje konto słuchacza na podstawie id i nazwy
    public ListenerAccount(int id, String name) {
        super(id, name);
    }

    // Metoda do tworzenia playlisty z podanych ID piosenek.
    // Jeśli piosenka nie jest na koncie, zostanie zakupiona (kosztuje 1 kredyt).
    public Playlist createPlaylist(List<Integer> songIds, int accountId) throws SQLException, NotEnoughCreditsException {
        Playlist playlist = new Playlist();
        for(var id: songIds) {
            // Sprawdza, czy piosenka jest już posiadana przez konto.
            if(!hasSong(this.getId(), id)) {
                // Kupuje piosenkę, jeśli nie jest posiadana.
                buySong(id, this.getId());
            }
            var optionalSong = Song.Persistence.read(id);
            if(optionalSong.isPresent())
                playlist.add(optionalSong.get());
            else
                throw new SQLException("Song with ID " + id + " not found");
        }
        return playlist;
    }

    // Statyczna klasa Persistence zawiera metody związane z bazą danych.
    public static class Persistence {
        // Inicjalizuje tabele w bazie danych, jeśli jeszcze nie istnieją.
        public static void init() throws SQLException {
            Account.Persistence.init();
            {
                String sql = "CREATE TABLE IF NOT EXISTS listener_account( " +
                        "id_account INTEGER NOT NULL PRIMARY KEY," +
                        "credits INTEGER NOT NULL)";
                PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
                statement.executeUpdate();
            }
            {
                String sql = "CREATE TABLE IF NOT EXISTS owned_songs( " +
                        "id_account INTEGER NOT NULL," +
                        "id_song INTEGER NOT NULL," +
                        "PRIMARY KEY (id_account, id_song))";
                PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
                statement.executeUpdate();
            }
        }

        // Rejestruje nowe konto słuchacza.
        public static int register(String username, String password) throws SQLException {
            try {
                int id = Account.Persistence.register(username, password);
                String sql = "INSERT INTO listener_account(id_account, credits) VALUES (?, 0)";
                PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
                statement.setInt(1, id);
                statement.executeUpdate();
                return id;
            } catch (SQLException | RuntimeException e) {
                throw new RuntimeException(e);
            }
        }

        // Pobiera liczbę kredytów na koncie.
        public static int getCredits(int id) throws SQLException {
            String sql = "SELECT credits FROM listener_account WHERE id_account = ?";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt("credits");
            } else throw new SQLException();
        }

        // Dodaje kredyty do konta.
        public static void addCredits(int id, int amount) throws SQLException {
            int currentCredits = getCredits(id);
            String sql = "UPDATE listener_account SET credits = ? WHERE id_account = ?";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
            statement.setInt(1, currentCredits + amount);
            statement.setInt(2, id);
            statement.executeUpdate();
        }

        // Dodaje piosenkę do listy posiadanych piosenek na koncie.
        public static void addSong(int accountId, int songId) throws SQLException {
            String sql = "INSERT INTO owned_songs VALUES(?, ?)";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
            statement.setInt(1, accountId);
            statement.setInt(2, songId);
            statement.executeUpdate();
        }

        // Sprawdza, czy piosenka jest posiadana na koncie.
        public static boolean hasSong(int accountId, int songId) throws SQLException {
            String sql = "SELECT * FROM owned_songs WHERE id_account = ? AND id_song = ?";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
            statement.setInt(1, accountId);
            statement.setInt(2, songId);
            return statement.executeQuery().next();
        }

        // Loguje użytkownika na podstawie nazwy użytkownika i hasła.
        static ListenerAccount authenticate(String username, String password) throws AuthenticationException {
            Account account = Account.Persistence.authenticate(username, password);
            return new ListenerAccount(account.getId(), account.getUsername());
        }

        // Kupuje piosenkę za kredyt. Jeśli kredytów brak, rzuca wyjątek NotEnoughCreditsException.
        public static void buySong(int songId, int accountId) throws SQLException, NotEnoughCreditsException {
            if (hasSong(accountId, songId)) {
                return;
            }
            String sql = "UPDATE listener_account SET credits = ? WHERE id_account = ?";

            int currentCredits = getCredits(accountId);
            if (currentCredits > 0) {
                addSong(accountId, songId);

                PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
                statement.setInt(1, currentCredits - 1);
                statement.setInt(2, accountId);
                statement.executeUpdate();

            } else {
                throw new NotEnoughCreditsException("Za mało kredytów: " + currentCredits);
            }
        }
    }
}
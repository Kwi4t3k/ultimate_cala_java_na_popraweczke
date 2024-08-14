package org.example.auth;

import org.example.database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AccountManager {
    // Rejestracja nowego użytkownika
    public void register(String name, String password) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.connect(Path.of("src/main/resources/my.db"));

        Statement statement = databaseConnection.getConnection().createStatement();
        statement.executeUpdate("create table if not exists Account (id integer unique PRIMARY KEY AUTOINCREMENT, name text, password text unique)");

        // Szyfrowanie hasła
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String query = "insert into Account (name, password) values(?, ?)";
        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, hashedPassword);
        preparedStatement.executeUpdate();

        databaseConnection.disconnect();
    }

    public boolean authenticate(String name, String password) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.connect(Path.of("src/main/resources/my.db"));

        String query = "select * from Account where name = ?";
        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, name);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String databasePassword = resultSet.getString("password");

            // Porównanie hasła z zaszyfrowanym hasłem w bazie danych
            return BCrypt.checkpw(password, databasePassword);
        } else {
            return false; // użytkownik nie znaleziony
        }
    }

    public Account getAccount(int id) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.connect(Path.of("src/main/resources/my.db"));

        String query = "select * from Account where id = ?";

        try (PreparedStatement statement = databaseConnection.getConnection().prepareStatement(query)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Account(id, resultSet.getString("name"));
            } else {
                return null;
            }
        } finally {
            databaseConnection.disconnect();
        }
    }

    public Account getAccount(String name) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.connect(Path.of("src/main/resources/my.db"));

        String query = "select * from Account where name = ?";

        try (PreparedStatement statement = databaseConnection.getConnection().prepareStatement(query)) {
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Account(resultSet.getInt("id"), name);
            } else {
                return null;
            }
        } finally {
            databaseConnection.disconnect();
        }
    }
}

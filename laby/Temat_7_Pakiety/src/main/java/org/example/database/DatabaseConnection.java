package org.example.database;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void connect(Path path) throws SQLException {
//        "jdbc:sqlite:" trzeba dać to przed ścieżką bo się sypnie (musi byc cały url do bazy)
        connection = DriverManager.getConnection("jdbc:sqlite:" + String.valueOf(path));
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

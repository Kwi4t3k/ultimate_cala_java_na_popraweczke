package org.example;

import org.apache.tomcat.jni.Pool;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:pixel.db";

    private Connection connection;
    private Statement statement;
    private static Database instance;

    private Database() {
        connectionOfDatabase();
        createTable();
    }

    private void connectionOfDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Problem z otwarciem połączenia");
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private void createTable() {
        String createMyTable = "CREATE TABLE IF NOT EXISTS entry (token INTEGER NOT NULL, x INTEGER NOT NULL, y INTEGER NOT NULL, color TEXT NOT NULL, timestamp TEXT NOT NULL);";

        try {
            statement.execute(createMyTable);
        } catch (SQLException e) {
            System.err.println("Błąd przy tworzeniu tabeli");
            e.printStackTrace();
        }
    }

    // KROK 8
    public int removeRecords(int id) {
        String delete = "DELETE FROM entry WHERE token=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(delete);

            preparedStatement.setInt(1, id);

            int records = preparedStatement.executeUpdate();
            return records;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public void addPixelToDatabase(int tokenId, int x, int y, String hexColor) {
        String insert = "INSERT INTO entry (token, x, y, color, timestamp) VALUES(?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setInt(1, tokenId);
            preparedStatement.setInt(2, x);
            preparedStatement.setInt(3, y);
            preparedStatement.setString(4, hexColor);
            preparedStatement.setString(5, LocalDateTime.now().toString());

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  KROK 6

    public List<Pixel> getListOfPixelsFromDatabase() {
        String select = "select * from entry";
        List<Pixel> pixels = new ArrayList<>();

        try {
            ResultSet resultSet = statement.executeQuery(select);

            while (resultSet.next()) {
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                String color = resultSet.getString("color");

                pixels.add(new Pixel(x, y, color));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pixels;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Problem z zamknięciem połączenia");
            e.printStackTrace();
        }
    }
}
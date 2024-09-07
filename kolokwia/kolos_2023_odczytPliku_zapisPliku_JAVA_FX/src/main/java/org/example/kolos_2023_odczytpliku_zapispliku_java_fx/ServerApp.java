package org.example.kolos_2023_odczytpliku_zapispliku_java_fx;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class ServerApp extends Application {

    // Ścieżka katalogu, w którym będą przechowywane obrazy
    private static final String IMAGES_DIR = "images";

    // Obiekt połączenia do bazy danych SQLite
    private static Connection conn;

    // Elementy interfejsu graficznego (suwak i etykieta)
    private Slider radiusSlider;
    private Label radiusLabel;

    // Metoda główna uruchamiająca aplikację JavaFX
    public static void main(String[] args) {
        launch(args);  // Uruchamia interfejs użytkownika
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Konfiguracja interfejsu graficznego JavaFX
        primaryStage.setTitle("Server Application");

        // Suwak do wyboru promienia filtra
        radiusSlider = new Slider(1, 15, 3);  // Zakres od 1 do 15, początkowa wartość 3
        radiusSlider.setMajorTickUnit(2);  // Główne odstępy co 2 jednostki
        radiusSlider.setMinorTickCount(1);  // Dodatkowe małe odstępy
        radiusSlider.setSnapToTicks(true);  // Zaokrąglanie do najbliższej wartości tick
        radiusSlider.setShowTickMarks(true);  // Wyświetlaj znaczniki
        radiusSlider.setShowTickLabels(true);  // Wyświetlaj wartości liczbowe przy suwaku

        // Etykieta pokazująca bieżącą wartość promienia
        radiusLabel = new Label("Promień filtra: 3");  // Ustawienie początkowego tekstu
        // Dodanie słuchacza do suwaka, by aktualizował etykietę przy zmianie wartości
        radiusSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int radius = (newVal.intValue() % 2 == 0) ? newVal.intValue() + 1 : newVal.intValue();
            radiusLabel.setText("Promień filtra: " + radius);
        });

        // Ustawienie układu graficznego (VBox z odstępami między elementami)
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(radiusLabel, radiusSlider);

        // Tworzenie sceny z układem
        Scene scene = new Scene(layout, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Utwórz katalog na obrazy, jeśli nie istnieje
        createImageDirectory();

        // Utwórz bazę danych, jeśli nie istnieje
        createDatabase();

        // Uruchom serwer w osobnym wątku, aby nie blokować interfejsu użytkownika
        new Thread(() -> {
            try {
                startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Metoda tworząca katalog "images"
    private void createImageDirectory() {
        File dir = new File(IMAGES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();  // Tworzy katalog jeśli nie istnieje
        }
    }

    // Metoda do tworzenia bazy danych SQLite
    private void createDatabase() {
        try {
            // Łączenie się z bazą danych SQLite (plik "images.db")
            conn = DriverManager.getConnection("jdbc:sqlite:images.db");

            // Tworzenie tabeli, jeśli nie istnieje
            String sql = "CREATE TABLE IF NOT EXISTS image_logs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "path TEXT, " +
                    "size INTEGER, " +
                    "delay INTEGER)";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metoda uruchamiająca serwer
    private void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);  // Tworzy serwer nasłuchujący na porcie 12345
        System.out.println("Serwer uruchomiony. Oczekiwanie na połączenia...");

        // Pętla obsługująca klientów
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {  // Oczekuje na połączenie klienta
                System.out.println("Połączono z klientem");

                // Odbieramy plik PNG od klienta
                File receivedFile = receiveFile(clientSocket);

                // Odczytujemy wartość promienia z suwaka
                int radius = (int) radiusSlider.getValue();

                // Przekształcamy obraz algorytmem box blur
                BufferedImage blurredImage = applyBoxBlur(receivedFile, radius);

                // Zapisujemy informacje o przekształceniu do bazy danych
                saveToDatabase(receivedFile.getName(), radius, 100);  // Przykładowy czas przekształcenia

                // Wysyłamy przekształcony obraz do klienta
                sendFileToClient(clientSocket, blurredImage);
            }
        }
    }

    // Metoda odbierająca plik od klienta
    private File receiveFile(Socket clientSocket) throws IOException {
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

        // Odbieramy nazwę pliku
        String fileName = dis.readUTF();
        File file = new File(IMAGES_DIR + "/" + fileName);

        // Zapisujemy plik do katalogu "images"
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        System.out.println("Plik otrzymany: " + fileName);
        return file;
    }

    // Metoda przekształcająca obraz algorytmem box blur
    private BufferedImage applyBoxBlur(File file, int radius) throws IOException {
        BufferedImage image = ImageIO.read(file);
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        // Przetwarzamy każdy piksel obrazu
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color blurredColor = getBlurredPixel(image, x, y, radius);
                result.setRGB(x, y, blurredColor.getRGB());
            }
        }

        return result;
    }

    // Metoda obliczająca wartość rozmytego piksela
    private Color getBlurredPixel(BufferedImage image, int x, int y, int radius) {
        int red = 0, green = 0, blue = 0;
        int count = 0;

        // Przetwarzamy sąsiadujące piksele
        for (int dy = -radius; dy <= radius; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                int newX = x + dx;
                int newY = y + dy;
                if (newX >= 0 && newY >= 0 && newX < image.getWidth() && newY < image.getHeight()) {
                    Color pixelColor = new Color(image.getRGB(newX, newY));
                    red += pixelColor.getRed();
                    green += pixelColor.getGreen();
                    blue += pixelColor.getBlue();
                    count++;
                }
            }
        }

        return new Color(red / count, green / count, blue / count);
    }

    // Metoda zapisująca informacje do bazy danych
    private void saveToDatabase(String fileName, int radius, int delay) {
        try {
            // Przygotowujemy zapytanie SQL do wstawienia danych
            String sql = "INSERT INTO image_logs (path, size, delay) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fileName);
            pstmt.setInt(2, radius);
            pstmt.setInt(3, delay);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metoda wysyłająca przekształcony obraz do klienta
    private void sendFileToClient(Socket clientSocket, BufferedImage image) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

        // Zapisujemy przekształcony obraz do pliku
        File outputFile = new File(IMAGES_DIR + "/blurred.png");
        ImageIO.write(image, "png", outputFile);

        // Wysyłamy obraz do klienta
        try (FileInputStream fis = new FileInputStream(outputFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, bytesRead);
            }
        }

        System.out.println("Przekształcony obraz wysłany do klienta.");
    }
}
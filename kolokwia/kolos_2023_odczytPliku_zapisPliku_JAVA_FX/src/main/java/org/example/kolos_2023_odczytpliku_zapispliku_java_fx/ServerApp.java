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

    // Ścieżka katalogu, w którym zapiszemy plik
    private static final String IMAGES_DIR = "images";

    // Połączenie z bazą danych SQLite
    private static Connection conn;

    // Interfejs graficzny JavaFX
    private Slider radiusSlider;
    private Label radiusLabel;

    // Metoda główna programu
    public static void main(String[] args) {
        launch(args);  // Uruchamiamy aplikację JavaFX
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Konfiguracja interfejsu graficznego
        primaryStage.setTitle("Server Application");

        // Tworzymy suwak do wyboru promienia filtra
        radiusSlider = new Slider(1, 15, 3);
        radiusSlider.setMajorTickUnit(2);
        radiusSlider.setMinorTickCount(1);
        radiusSlider.setSnapToTicks(true);
        radiusSlider.setShowTickMarks(true);
        radiusSlider.setShowTickLabels(true);

        // Etykieta do wyświetlania wartości promienia
        radiusLabel = new Label("Promień filtra: 3");
        radiusSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int radius = (newVal.intValue() % 2 == 0) ? newVal.intValue() + 1 : newVal.intValue();
            radiusLabel.setText("Promień filtra: " + radius);
        });

        // Layout interfejsu
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(radiusLabel, radiusSlider);

        // Tworzymy scenę
        Scene scene = new Scene(layout, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Tworzymy katalog na obrazy, jeśli nie istnieje
        createImageDirectory();

        // Tworzymy bazę danych, jeśli nie istnieje
        createDatabase();

        // Uruchamiamy serwer w osobnym wątku, aby nie blokować interfejsu
        new Thread(() -> {
            try {
                startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Metoda do tworzenia katalogu "images"
    private void createImageDirectory() {
        File dir = new File(IMAGES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Metoda do tworzenia bazy danych
    private void createDatabase() {
        try {
            // Łączymy się z bazą danych SQLite
            conn = DriverManager.getConnection("jdbc:sqlite:images.db");

            // Tworzymy tabelę, jeśli nie istnieje
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
    // Metoda do uruchomienia serwera
    private void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Serwer uruchomiony. Oczekiwanie na połączenia...");

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Połączono z klientem");

                // Odbieramy plik PNG od klienta
                File receivedFile = receiveFile(clientSocket);

                // Odczytujemy wartość promienia z interfejsu użytkownika
                int radius = (int) radiusSlider.getValue();

                // Przekształcamy obraz algorytmem box blur
                BufferedImage blurredImage = applyBoxBlur(receivedFile, radius);

                // Zapisujemy przekształcenie do bazy danych
                saveToDatabase(receivedFile.getName(), radius, 100); // przykładowy czas: 100 ms

                // Wysyłamy przekształcony plik z powrotem do klienta
                sendFileToClient(clientSocket, blurredImage);
            }
        }
    }

    // Metoda do odbierania pliku od klienta
    private File receiveFile(Socket clientSocket) throws IOException {
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

        // Odbieramy nazwę pliku
        String fileName = dis.readUTF();
        File file = new File(IMAGES_DIR + "/" + fileName);

        // Zapisujemy otrzymany plik do katalogu "images"
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

    // Metoda do przekształcania obrazu algorytmem box blur
    private BufferedImage applyBoxBlur(File file, int radius) throws IOException {
        BufferedImage image = ImageIO.read(file);
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        // Przetwarzanie obrazu
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color blurredColor = getBlurredPixel(image, x, y, radius);
                result.setRGB(x, y, blurredColor.getRGB());
            }
        }

        return result;
    }

    // Metoda do obliczania rozmytego piksela
    private Color getBlurredPixel(BufferedImage image, int x, int y, int radius) {
        int red = 0, green = 0, blue = 0;
        int count = 0;

        // Przetwarzamy otoczenie piksela
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

    // Metoda do zapisu informacji do bazy danych
    private void saveToDatabase(String filePath, int filterSize, int delay) {
        try {
            String sql = "INSERT INTO image_logs(path, size, delay) VALUES(?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, filePath);
            pstmt.setInt(2, filterSize);
            pstmt.setInt(3, delay);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metoda do wysyłania przetworzonego pliku do klienta
    private void sendFileToClient(Socket clientSocket, BufferedImage image) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

        // Konwertujemy obraz na strumień PNG
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        // Wysyłamy obraz do klienta
        dos.write(imageBytes);
        dos.flush();

        System.out.println("Przekształcony obraz został wysłany do klienta.");
    }
}
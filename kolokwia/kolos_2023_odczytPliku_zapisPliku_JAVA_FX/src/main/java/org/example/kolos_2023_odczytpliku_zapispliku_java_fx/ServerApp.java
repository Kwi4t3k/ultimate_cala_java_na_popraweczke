package org.example.kolos_2023_odczytpliku_zapispliku_java_fx;

// Importujemy niezbędne klasy i pakiety
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

// Klasa główna aplikacji serwera
public class ServerApp extends Application {

    // Ścieżka katalogu, w którym będą przechowywane obrazy
    private static final String IMAGES_DIR = "images";

    // Obiekt połączenia do bazy danych SQLite
    private static Connection conn;

    // Elementy interfejsu graficznego (suwak i etykieta)
    @FXML
    private Slider radiusSlider;
    @FXML
    private Label radiusLabel;

    public static void main(String[] args) {
        launch(args);  // Uruchamia interfejs użytkownika aplikacji JavaFX
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ładujemy plik FXML z definicją interfejsu użytkownika
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/kolos_2023_odczytpliku_zapispliku_java_fx/view.fxml"));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Server Application");  // Ustawiamy tytuł okna
        primaryStage.setScene(scene);  // Ustawiamy scenę w oknie
        primaryStage.show();  // Wyświetlamy okno

        // Pobieramy kontroler z pliku FXML i inicjalizujemy elementy interfejsu
        ServerAppController controller = loader.getController();
        radiusSlider = controller.getRadiusSlider();
        radiusLabel = controller.getRadiusLabel();

        // Utwórz katalog na obrazy, jeśli nie istnieje
        createImageDirectory();  // Ad 2

        // Utwórz bazę danych, jeśli nie istnieje
        createDatabase();  // Ad 5

        // Uruchom serwer w osobnym wątku, aby nie blokować interfejsu użytkownika
        new Thread(() -> {
            try {
                startServer();  // Ad 1, 3, 4, 5, 6
            } catch (IOException e) {
                e.printStackTrace();  // Wypisujemy wyjątek w przypadku błędu
            }
        }).start();
    }

    // Metoda tworząca katalog "images", jeśli nie istnieje
    private void createImageDirectory() {
        File dir = new File(IMAGES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();  // Tworzy katalog, jeśli nie istnieje
        }
    }

    // Metoda do tworzenia bazy danych SQLite, jeśli nie istnieje
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
            stmt.execute(sql);  // Wykonanie zapytania SQL
        } catch (Exception e) {
            e.printStackTrace();  // Wypisanie wyjątku w przypadku błędu
        }
    }

    // Metoda uruchamiająca serwer
    private void startServer() throws IOException {
        // Tworzymy serwer nasłuchujący na porcie 5000
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Serwer uruchomiony. Oczekiwanie na połączenia...");

        // Pętla obsługująca klientów
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {  // Ad 1
                System.out.println("Połączono z klientem");

                // Odbieramy plik PNG od klienta
                File receivedFile = receiveFile(clientSocket);  // Ad 1, 2

                // Odczytujemy wartość promienia z suwaka  // Ad 3
                int radius = (int) radiusSlider.getValue();

                // Przekształcamy obraz algorytmem box blur  // Ad 4
                BufferedImage blurredImage = applyBoxBlur(receivedFile, radius);

                // Zapisujemy informacje o przekształceniu do bazy danych  // Ad 5
                saveToDatabase(receivedFile.getName(), radius, 100);  // Przykładowy czas przekształcenia

                // Wysyłamy przekształcony obraz do klienta  // Ad 6
                sendFileToClient(clientSocket, blurredImage);
            }
        }
    }

    // Metoda odbierająca plik od klienta
    private File receiveFile(Socket clientSocket) throws IOException {
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

        // Generujemy nazwę pliku na podstawie aktualnego czasu
        String fileName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".png";
        File file = new File(IMAGES_DIR + "/" + fileName);

        // Zapisujemy plik do katalogu "images"
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesRead);  // Zapisujemy odebrane dane do pliku
            }
        }

        System.out.println("Plik otrzymany: " + fileName);
        return file;
    }

    // Metoda przekształcająca obraz algorytmem box blur
    private BufferedImage applyBoxBlur(File file, int radius) throws IOException {
        BufferedImage image = ImageIO.read(file);  // Wczytujemy obraz z pliku
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        // Przetwarzamy każdy piksel obrazu
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color blurredColor = getBlurredPixel(image, x, y, radius);
                result.setRGB(x, y, blurredColor.getRGB());  // Ustawiamy rozmyty kolor w wyniku
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

        // Obliczamy średni kolor piksela
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
            pstmt.executeUpdate();  // Wykonanie zapytania
        } catch (Exception e) {
            e.printStackTrace();  // Wypisanie wyjątku w przypadku błędu
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
                dos.write(buffer, 0, bytesRead);  // Wysyłamy dane do klienta
            }
        }

        System.out.println("Przekształcony obraz wysłany do klienta.");
    }
}
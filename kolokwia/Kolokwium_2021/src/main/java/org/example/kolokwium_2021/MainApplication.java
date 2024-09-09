package org.example.kolokwium_2021;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainApplication extends Application {

    private appCanvas appCanvas; // Płótno, na którym rysowane są linie
    private double offsetX = 0; // Przesunięcie w osi X dla rysunków
    private double offsetY = 0; // Przesunięcie w osi Y dla rysunków

    @Override
    public void start(Stage stage) {
        // Tworzenie płótna o wymiarach 500x500
        appCanvas = new appCanvas(500, 500);

        // Tworzenie głównego kontenera sceny
        Pane root = new Pane(appCanvas);
        Scene scene = new Scene(root);

        // Ustawienie tytułu okna
        stage.setTitle("Drawing lines");
        stage.setScene(scene);
        stage.show();

        // Ustawienie obsługi naciśnięć klawiszy strzałek
        scene.setOnKeyPressed(keyEvent -> handleKeyPress(keyEvent, stage));

        // Uruchomienie serwera w osobnym wątku, aby nie blokować interfejsu użytkownika
        new Thread(() -> {
            try {
                startServer(); // Rozpoczęcie działania serwera
            } catch (IOException e) {
                e.printStackTrace(); // Obsługa wyjątków związanych z uruchomieniem serwera
            }
        }).start();
    }

    // Obsługa naciśnięć klawiszy strzałek
    private void handleKeyPress(KeyEvent keyEvent, Stage stage) {
        switch (keyEvent.getCode()) {
            case UP -> offsetY -= 10; // Przesunięcie w górę
            case DOWN -> offsetY += 10; // Przesunięcie w dół
            case LEFT -> offsetX -= 10; // Przesunięcie w lewo
            case RIGHT -> offsetX += 10; // Przesunięcie w prawo
        }

        // Zaktualizowanie tytułu okna, aby pokazać bieżące przesunięcie
        stage.setTitle(getWindowTitle());

        // Przerysowanie wszystkich odcinków z uwzględnieniem nowego przesunięcia
        appCanvas.setOffset(offsetX, offsetY);
        appCanvas.redraw();
    }

    // Metoda generująca tytuł okna z aktualnym przesunięciem
    private String getWindowTitle() {
        return "Drawing lines - Offset: (" + offsetX + ", " + offsetY + ")";
    }

    // Metoda uruchamiająca serwer nasłuchujący na porcie 4000
    private void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(4000); // Tworzenie gniazda serwera nasłuchującego na porcie 4000
        System.out.println("Serwer uruchomiony. Oczekiwanie na połączenie...");

        // Pętla obsługująca klientów
        while (true) {
            try {
                // Akceptowanie połączenia od klienta
                Socket clientSocket = serverSocket.accept();

                System.out.println("Połączono z klientem");

                // Każdy klient jest obsługiwany w osobnym wątku
                new ClientHandler(clientSocket, appCanvas).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Serwer będzie kontynuował nasłuch, nie zamykamy serverSocket
        }
    }

    public static void main(String[] args) {
        launch(args); // Uruchomienie aplikacji JavaFX
    }
}
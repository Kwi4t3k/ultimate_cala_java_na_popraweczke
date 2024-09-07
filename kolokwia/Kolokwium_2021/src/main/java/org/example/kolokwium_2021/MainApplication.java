package org.example.kolokwium_2021;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainApplication extends Application {

    private appCanvas appCanvas;

    @Override
    public void start(Stage stage) {
        // Tworzenie płótna o wymiarach 500x500
        appCanvas = new appCanvas(500, 500);

        // Tworzenie sceny
        Pane root = new Pane(appCanvas);
        Scene scene = new Scene(root);

        stage.setTitle("Drawing lines");
        stage.setScene(scene);
        stage.show();

        // Uruchomienie serwera w osobnym wątku, aby nie blokować interfejsu użytkownika
        new Thread(() -> {
            try {
                startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Metoda uruchamiająca serwer nasłuchujący na porcie 4000
    private void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(4000);
        System.out.println("Serwer uruchomiony. Oczekiwanie na połączenie...");

        // Pętla obsługująca klientów
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Połączono z klientem");

                // Każdy klient jest obsługiwany w osobnym wątku
                new ClientHandler(clientSocket, appCanvas).start();
            }
            System.out.println("Rozłączono z klientem");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
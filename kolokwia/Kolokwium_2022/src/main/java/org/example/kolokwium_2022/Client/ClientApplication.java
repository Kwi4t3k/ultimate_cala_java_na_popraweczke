package org.example.kolokwium_2022.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Ładowanie pliku FXML i automatyczne wiązanie z kontrolerem
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("view.fxml"));
        // Tworzenie sceny o szerokości 320 i wysokości 240 pikseli
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        // Pobieranie kontrolera po załadowaniu pliku FXML
        AppController controller = fxmlLoader.getController();

        // Ustawianie tytułu okna
        stage.setTitle("Words");
        // Przypisanie sceny do okna (stage)
        stage.setScene(scene);
        // Wyświetlenie okna
        stage.show();

        // Tworzenie nowego wątku, który będzie obsługiwał komunikację z serwerem
        new Thread(() -> {
            try {
                // Łączenie się z serwerem na porcie 5000
                Socket socket = new Socket("localhost", 5000);
                // Tworzenie obiektu do czytania danych z serwera
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                // Odbieranie wiadomości z serwera, dopóki są dostępne
                while ((message = bufferedReader.readLine()) != null) {
                    System.out.println("Dodano nowe słowo");  // Debugowanie
                    String finalMessage = message;

                    // Ponieważ operacje GUI muszą być wykonywane na wątku głównym, używamy Platform.runLater
                    Platform.runLater(() -> {
                        // Dodawanie słowa do GUI przez kontroler
                        controller.addWord(finalMessage);
                    });
                }

            } catch (IOException e) {
                // Obsługa błędów związanych z połączeniem
                e.printStackTrace();
            }
        }).start(); // Uruchomienie wątku
    }

    public static void main(String[] args) {
        // Uruchomienie aplikacji JavaFX
        launch();
    }
}
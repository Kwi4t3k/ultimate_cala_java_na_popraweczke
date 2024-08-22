package org.example.temat_12_interfejs_uzytkownika;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * MainApplication uruchamia aplikację, ładuje widok logowania
 * i inicjuje połączenie z serwerem.
 */
public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Ładowanie pliku FXML dla widoku czatu
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        // Inicjalizacja połączenia z serwerem
        ConnectionThread thread = new ConnectionThread("localhost", 5000);
        thread.start();
        ClientReceiver.thread = thread;

        // Wyświetlenie dialogu logowania
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Logowanie");
        dialog.setContentText("Wpisz login");

        // Pobieranie loginu od użytkownika
        Optional<String> login = dialog.showAndWait();

        if (login.isPresent()) {
            // Wysyłanie loginu do serwera
            thread.login(login.get());

            // Ustawienie widoku czatu
            stage.setTitle("Chat Application");
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
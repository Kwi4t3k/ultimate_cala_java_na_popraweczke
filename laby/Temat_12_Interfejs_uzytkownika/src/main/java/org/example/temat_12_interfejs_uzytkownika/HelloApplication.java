package org.example.temat_12_interfejs_uzytkownika;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Załadowanie pliku FXML
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // Utworzenie sceny i przypisanie jej do okna
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setScene(scene);

        // Wyświetlenie okna
        stage.show();
    }

    public static void main(String[] args) {
        launch();  // Uruchomienie aplikacji JavaFX
    }
}
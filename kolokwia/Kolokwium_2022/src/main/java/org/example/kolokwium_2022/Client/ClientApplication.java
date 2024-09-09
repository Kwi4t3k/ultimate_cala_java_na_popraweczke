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
        // Ładowanie FXML i automatyczne powiązanie z kontrolerem
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        // Pobranie kontrolera po załadowaniu FXML
        AppController controller = fxmlLoader.getController();

        stage.setTitle("Words");
        stage.setScene(scene);
        stage.show();

        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 5000);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = bufferedReader.readLine()) != null) {
                    System.out.println("Dodano nowe słowo");
                    String finalMessage = message;

                    // Potrzebne, ponieważ lambda musi mieć finalną zmienną
                    Platform.runLater(() -> {
                        // Dodaj słowo do ListView w GUI
                        controller.addWord(finalMessage);
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch();
    }
}
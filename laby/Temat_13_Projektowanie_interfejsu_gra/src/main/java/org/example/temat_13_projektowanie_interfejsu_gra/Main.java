package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Tworzenie instancji GameCanvas o wymiarach 800x600 pikseli
        GameCanvas gameCanvas = new GameCanvas(800, 600);

        // Tworzenie głównego kontenera dla sceny, umieszczamy w nim GameCanvas
        Pane root = new Pane(gameCanvas);

        // Tworzenie sceny, która zawiera kontener (root)
        Scene scene = new Scene(root);

        // Ustawienie tytułu okna
        stage.setTitle("Breakout Game");

        // Ustawienie sceny dla okna
        stage.setScene(scene);

        // Wyświetlenie okna
        stage.show();
    }

    public static void main(String[] args) {
        // Uruchomienie aplikacji JavaFX
        launch();
    }
}
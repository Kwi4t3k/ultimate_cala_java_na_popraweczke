package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        GameCanvas gameCanvas = new GameCanvas(800, 600);

        Pane root = new Pane(gameCanvas);

        Scene scene = new Scene(root);

        stage.setTitle("Breakout Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
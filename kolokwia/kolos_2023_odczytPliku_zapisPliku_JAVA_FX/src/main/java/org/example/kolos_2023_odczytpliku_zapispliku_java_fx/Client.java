package org.example.kolos_2023_odczytpliku_zapispliku_java_fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client extends Application {

    private File selectedFile;
    private Slider radiusSlider;
    private Label radiusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client Application");

        // Przycisk do wyboru pliku
        Button fileButton = new Button("Wybierz plik PNG");
        Label fileLabel = new Label("Nie wybrano pliku.");
        fileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                fileLabel.setText("Wybrano plik: " + selectedFile.getName());
            }
        });

        // Slider do wyboru promienia filtra (1-15, tylko nieparzyste wartości)
        radiusSlider = new Slider(1, 15, 3);
        radiusSlider.setMajorTickUnit(2);
        radiusSlider.setMinorTickCount(1);
        radiusSlider.setSnapToTicks(true);
        radiusSlider.setShowTickMarks(true);
        radiusSlider.setShowTickLabels(true);
        radiusLabel = new Label("Promień filtra: 3");

        // Zmienianie etykiety promienia filtra
        radiusSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int radius = newVal.intValue() % 2 == 0 ? newVal.intValue() + 1 : newVal.intValue();
            radiusLabel.setText("Promień filtra: " + radius);
        });

        // Przycisk do wysłania pliku do serwera
        Button sendButton = new Button("Wyślij do serwera");
        sendButton.setOnAction(e -> {
            if (selectedFile != null) {
                try {
                    sendFileToServer(selectedFile, (int) radiusSlider.getValue());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Brak pliku");
                alert.setHeaderText(null);
                alert.setContentText("Proszę wybrać plik PNG przed wysłaniem.");
                alert.showAndWait();
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(fileButton, fileLabel, radiusLabel, radiusSlider, sendButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendFileToServer(File file, int radius) throws IOException {
        // Adres i port serwera
        String serverAddress = "localhost";
        int port = 12345;

        // Połączenie z serwerem
        try (Socket socket = new Socket(serverAddress, port);
             FileInputStream fileInputStream = new FileInputStream(file);
             OutputStream outputStream = socket.getOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {

            // Wysyłanie pliku do serwera
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Najpierw wysyłamy nazwę pliku
            dataOutputStream.writeUTF(file.getName());

            // Wysyłamy plik
            while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }

            // Wysyłamy promień filtra
            dataOutputStream.writeInt(radius);

            // Odbieramy odpowiedź od serwera (przetworzony plik)
            InputStream inputStream = socket.getInputStream();
            File outputFile = new File("output.png");
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Plik przetworzony i zapisany jako output.png");
        }
    }
}
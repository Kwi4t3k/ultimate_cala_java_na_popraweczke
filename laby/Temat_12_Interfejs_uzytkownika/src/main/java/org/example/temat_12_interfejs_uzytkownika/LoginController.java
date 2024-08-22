package org.example.temat_12_interfejs_uzytkownika;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * LoginController obsługuje proces logowania użytkownika do aplikacji czatu.
 * Odpowiada za walidację danych logowania i przełączanie na widok czatu.
 */
public class LoginController {

    @FXML
    private TextField textFieldName; // Pole tekstowe na nazwę użytkownika

    @FXML
    private PasswordField passwordField; // Pole tekstowe na hasło

    @FXML
    private Button buttonLogin; // Przycisk "LOGIN"

    @FXML
    public void onLoginButtonClick() {
        // Pobiera wartości z pól tekstowych
        String login = textFieldName.getText();
        String password = passwordField.getText();

        // Sprawdza, czy pola logowania nie są puste
        if (!login.trim().isEmpty() && !password.trim().isEmpty()) {
            try {
                // Ładowanie pliku FXML dla widoku czatu
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("chat-view.fxml"));
                Scene chatScene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) buttonLogin.getScene().getWindow();
                stage.setScene(chatScene);

                // Inicjalizacja połączenia z serwerem
                ConnectionThread thread = new ConnectionThread("localhost", 5000);
                thread.start();
                thread.login(login);

                // Ustawienie nowego wątku połączenia
                ClientReceiver.thread = thread;
            } catch (IOException e) {
                e.printStackTrace();
                // Ustawienie komunikatu o błędzie w polu tekstowym
                textFieldName.setPromptText("Error loading chat");
            }
        } else {
            // Ustawienie komunikatów w przypadku pustych pól
            textFieldName.setPromptText("Please enter your login");
            passwordField.setPromptText("Please enter your password");
        }
    }
}
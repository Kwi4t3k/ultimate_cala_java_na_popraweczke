package org.example.temat_12_interfejs_uzytkownika;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HelloController {

    @FXML
    private TextArea textAreaChat;  // Pole do wyświetlania wiadomości czatu

    @FXML
    private TextField textFieldNewMessage;  // Pole do wpisywania nowych wiadomości

    @FXML
    private ListView<String> listViewActiveUsers;  // Lista aktywnych użytkowników

    @FXML
    private Button buttonSend;  // Przycisk do wysyłania wiadomości

    // Metoda obsługująca kliknięcie przycisku "Send" lub wciśnięcie Enter w polu tekstowym
    @FXML
    public void onSendButtonClick() {
        // Pobranie wiadomości z pola tekstowego
        String message = textFieldNewMessage.getText();

        // Jeśli wiadomość nie jest pusta
        if (!message.isEmpty()) {
            // Dopisanie wiadomości do pola czatu
            textAreaChat.appendText(message + "\n");

            // Wyczyść pole tekstowe po wysłaniu wiadomości
            textFieldNewMessage.clear();
        }
    }
}
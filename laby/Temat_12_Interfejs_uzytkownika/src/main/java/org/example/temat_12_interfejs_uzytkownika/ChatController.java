package org.example.temat_12_interfejs_uzytkownika;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * ChatController obsługuje interakcje użytkownika z widokiem czatu.
 * Odpowiada za wysyłanie i odbieranie wiadomości, zarządzanie listą aktywnych użytkowników
 * oraz aktualizowanie widoku czatu.
 */
public class ChatController {

    @FXML
    private TextArea textAreaChat;  // Pole tekstowe do wyświetlania wiadomości czatu

    @FXML
    private TextField textFieldNewMessage;  // Pole tekstowe do wprowadzania nowych wiadomości

    @FXML
    private ListView<String> listViewActiveUsers;  // ListView do wyświetlania aktywnych użytkowników

    public ChatController() {
        // Inicjalizuje kontroler w ClientReceiver, aby odbierać wiadomości
        ClientReceiver.controller = this;
    }

    @FXML
    public void onSendButtonClick() throws JsonProcessingException {
        // Pobiera tekst wiadomości z pola tekstowego
        String message = textFieldNewMessage.getText();
        // Dodaje wiadomość do pola tekstowego czatu
        textAreaChat.appendText("Me: " + message + "\n");
        // Czyści pole tekstowe dla nowej wiadomości
        textFieldNewMessage.clear();

        // Tworzy wiadomość do wysłania
        Message messageToSend = new Message(MessageType.Broadcast, message);
        // Wysyła wiadomość przez wątek połączenia
        ClientReceiver.thread.send(messageToSend);
    }

    @FXML
    public void onRequestOnlineUsers() throws JsonProcessingException {
        // Wysyła żądanie o listę użytkowników do serwera
        ClientReceiver.thread.requestUsers();
    }

    public void onMessage(String message) {
        // Dodaje wiadomość broadcast do pola tekstowego czatu
        textAreaChat.appendText("Broadcast: " + message + "\n");
    }

    public void onWhisper(String message) {
        // Dodaje wiadomość prywatną do pola tekstowego czatu
        textAreaChat.appendText("Whisper: " + message + "\n");
    }

    public void onLogin(String message) throws JsonProcessingException {
        // Dodaje wiadomość o logowaniu do pola tekstowego czatu
        textAreaChat.appendText("Login: " + message + "\n");
        // Po zalogowaniu aktualizuje listę użytkowników
        onRequestOnlineUsers();
    }

    public void onLogout(String message) throws JsonProcessingException {
        // Dodaje wiadomość o wylogowaniu do pola tekstowego czatu
        textAreaChat.appendText("Logout: " + message + "\n");
        // Po wylogowaniu aktualizuje listę użytkowników
        onRequestOnlineUsers();
    }

    public void onOnline(String message) throws JsonProcessingException {
        // Dodaje wiadomość o statusie online do pola tekstowego czatu
        textAreaChat.appendText("Online: " + message + "\n");
        // Po odebraniu informacji o użytkownikach aktualizuje listę
        onRequestOnlineUsers();
    }

    public void onFile(String message) {
        // Dodaje wiadomość o pliku do pola tekstowego czatu
        textAreaChat.appendText("File: " + message + "\n");
    }

    // Metoda do aktualizacji listy użytkowników w ListView
    public void updateUserList(String[] users) {
        // Czyści obecną listę użytkowników
        listViewActiveUsers.getItems().clear();
        // Dodaje nowych użytkowników do listy
        listViewActiveUsers.getItems().addAll(users);
    }

    @FXML
    private void initialize() {
        // Konfiguracja pola tekstowego do wysyłania wiadomości
        textFieldNewMessage.setOnAction(event -> {
            try {
                // Obsługuje kliknięcie przycisku wysyłania wiadomości
                onSendButtonClick();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        // Dodanie przycisku do ręcznego odświeżania listy użytkowników
        Button refreshButton = new Button("Refresh Users");
        refreshButton.setOnAction(e -> {
            try {
                // Obsługuje kliknięcie przycisku odświeżania listy użytkowników
                onRequestOnlineUsers();
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        });
    }
}
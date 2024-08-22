package org.example.temat_12_interfejs_uzytkownika;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * ClientReceiver odbiera wiadomości z serwera i przekazuje je do ChatController.
 * Odpowiada za rozdzielanie wiadomości w zależności od ich typu.
 */
public class ClientReceiver {
    public static ChatController controller;  // Kontroler czatu
    public static ConnectionThread thread;  // Wątek połączenia z serwerem

    public static void receiveBroadcast(String message) throws JsonProcessingException {
        // Obsługuje wiadomość broadcast
        String[] parts = message.split(":", 2);
        if (parts.length < 2) {
            return;
        }

        String messageType = parts[0];
        String messageContent = parts[1];

        switch (messageType) {
            case "BROADCAST" -> controller.onMessage(messageContent);
            case "WHISPER" -> controller.onWhisper(messageContent);
            case "LOGIN" -> controller.onLogin(messageContent);
            case "LOGOUT" -> controller.onLogout(messageContent);
            case "ONLINE" -> controller.onOnline(messageContent);
            case "FILE" -> controller.onFile(messageContent);
            case "USERS" -> {
                // Aktualizacja listy użytkowników
                String[] users = messageContent.split(",");
                controller.updateUserList(users);
            }
            default -> System.out.println("Unknown message type: " + messageType);
        }
    }

    public static void receiveWhisper(String message) {
        // Obsługuje wiadomość prywatną
        controller.onWhisper(message);
    }

    public static void receiveLogin(String message) throws JsonProcessingException {
        // Obsługuje wiadomość o logowaniu
        controller.onLogin(message);
    }

    public static void receiveLogout(String message) throws JsonProcessingException {
        // Obsługuje wiadomość o wylogowaniu
        controller.onLogout(message);
    }

    public static void receiveOnline(String message) throws JsonProcessingException {
        // Obsługuje wiadomość o statusie online
        controller.onOnline(message);
    }

    public static void receiveFile(String message) {
        // Obsługuje wiadomość o pliku
        controller.onFile(message);
    }

    public static void receiveUsers(String users) {
        // Obsługuje listę użytkowników
        String[] userArray = users.split(",");
        controller.updateUserList(userArray);
    }
}
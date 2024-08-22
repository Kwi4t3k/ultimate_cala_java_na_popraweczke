package org.example.temat_12_interfejs_uzytkownika;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;

/**
 * ConnectionThread obsługuje komunikację z serwerem.
 * Odpowiada za odbieranie wiadomości i wysyłanie wiadomości do serwera.
 */
public class ConnectionThread extends Thread {
    private Socket socket;  // Gniazdo do połączenia z serwerem
    private PrintWriter writer;  // Writer do wysyłania danych do serwera

    public ConnectionThread(String address, int port) throws IOException {
        // Inicjalizuje połączenie z serwerem
        socket = new Socket(address, port);
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String rawMessage;
            while ((rawMessage = reader.readLine()) != null) {
                // Odbiera wiadomość w formie JSON i przetwarza ją
                Message message = new ObjectMapper().readValue(rawMessage, Message.class);

                switch (message.type) {
                    case Broadcast -> ClientReceiver.receiveBroadcast(message.content);
                    case Whisper -> ClientReceiver.receiveWhisper(message.content);
                    case Login -> ClientReceiver.receiveLogin(message.content);
                    case Logout -> ClientReceiver.receiveLogout(message.content);
                    case Online -> ClientReceiver.receiveOnline(message.content);
                    case File -> ClientReceiver.receiveFile(message.content);
                    case Users -> ClientReceiver.receiveUsers(message.content); // Obsługuje listę użytkowników
                    default -> System.out.println("Unknown message type: " + message.type);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) throws JsonProcessingException {
        // Wysyła wiadomość do serwera w formie JSON
        String rawMessage = new ObjectMapper().writeValueAsString(message);
        writer.println(rawMessage);
    }

    public void login(String login) throws JsonProcessingException {
        // Tworzy wiadomość logowania i wysyła ją do serwera
        Message message = new Message(MessageType.Login, login);
        send(message);
    }

    public void requestUsers() throws JsonProcessingException {
        // Tworzy wiadomość żądania listy użytkowników i wysyła ją do serwera
        Message message = new Message(MessageType.RequestUsers, "");
        send(message);
    }
}
package org.example.temat_12_interfejs_uzytkownika;

/**
 * Message reprezentuje strukturę wiadomości przesyłanych między klientem a serwerem.
 * Zawiera typ wiadomości oraz jej treść.
 */
public class Message {
    public MessageType type; // Typ wiadomości
    public String content; // Treść wiadomości

    public Message() {}

    public Message(MessageType type, String content) {
        this.type = type;
        this.content = content;
    }
}
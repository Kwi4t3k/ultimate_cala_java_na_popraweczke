package org.example.temat_12_interfejs_uzytkownika;

// Typy wiadomości wysyłane przez serwer do klienta
public enum MessageType {
    Broadcast,     // Wiadomości publiczne
    Whisper,       // Wiadomości prywatne
    Login,         // Wiadomości logowania
    Logout,        // Wiadomości wylogowania
    Online,        // Status online użytkownika
    File,          // Wiadomości zawierające pliki
    RequestUsers,  // Żądanie listy użytkowników
    Users          // Lista użytkowników
}
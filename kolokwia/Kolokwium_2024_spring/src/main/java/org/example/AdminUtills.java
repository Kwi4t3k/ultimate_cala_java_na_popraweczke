package org.example;

import java.io.IOException;

// KROK 8
public class AdminUtills {
    // Metoda banująca użytkownika na podstawie jego identyfikatora (tokena)
    public static int ban(int id) {
        // Usunięcie tokena użytkownika
        Token.removeToken(id);

        // Uzyskanie instancji obiektu bazy danych
        Database database = Database.getInstance();
        // Usunięcie rekordów powiązanych z danym tokenem z bazy danych
        int numberOfDeletedRecords = database.removeRecords(id);

        // Regeneracja obrazu po usunięciu danych (zaktualizowanie wizualizacji)
        ImageRGB imageRGB = ImageRGB.getInstance();
        imageRGB.setImageBasedOnPixels();

        // Zwrócenie liczby usuniętych rekordów jako wyniku operacji
        return numberOfDeletedRecords;
    }

    // KROK 9
    // Funkcja, która uruchomi proces generowania wideo za pomocą FFmpeg
    public static void generateVideo() {
        // Tworzenie obiektu ProcessBuilder do uruchomienia zewnętrznego procesu (FFmpeg)
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Ustawienie komendy FFmpeg do generowania wideo na podstawie obrazów PNG
        processBuilder.command("ffmpeg", "-framerate", "30", "-pattern_type", "glob", "-i", "src/main/resources/kon/*.png", "-c:v", "libx265", "output.mp4");

        try {
            // Uruchomienie procesu FFmpeg
            Process process = processBuilder.start();

            // Oczekiwanie na zakończenie procesu i uzyskanie kodu wyjścia
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // Jeśli proces zakończył się pomyślnie, wyświetla informację o sukcesie
                System.out.println("Wideo zostało wygenerowane pomyślnie.");
            } else {
                // Jeśli proces zakończył się niepowodzeniem, wyświetla komunikat o błędzie
                System.err.println("Błąd podczas generowania wideo.");
            }
        } catch (IOException | InterruptedException e) {
            // Obsługa wyjątków, gdy coś pójdzie nie tak podczas uruchamiania procesu
            e.printStackTrace();
        }
    }
}
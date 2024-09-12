package org.example;

import java.io.IOException;

// KROK 8
public class AdminUtills {
    //banowanie
    public static int ban(int id) {
        // usunięcie tokena
        Token.removeToken(id);

        // usunięcie z bazy danych rekordów z danym tokenem/id
        Database database = Database.getInstance();
        int numberOfDeletedRecords = database.removeRecords(id);

        // regeneracja obrazu po usunięciu danych z bazy
        ImageRGB imageRGB = ImageRGB.getInstance();
        imageRGB.setImageBasedOnPixels();

        // Zwracamy liczbę usuniętych rekordów
        return numberOfDeletedRecords;
    }


    // KROK 9
    // Funkcja, która uruchomi proces generowania wideo
    public static void generateVideo() {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Komenda FFmpeg do generowania wideo
        processBuilder.command("ffmpeg", "-framerate", "30", "-pattern_type", "glob", "-i", "src/main/resources/kon/*.png", "-c:v", "libx265", "output.mp4");

        try {
            // Uruchomienie procesu
            Process process = processBuilder.start();

            // Czekanie na zakończenie procesu
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Wideo zostało wygenerowane pomyślnie.");
            } else {
                System.err.println("Błąd podczas generowania wideo.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

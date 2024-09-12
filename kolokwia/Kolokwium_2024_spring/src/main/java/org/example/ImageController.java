package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;

// Kontroler REST obsługujący zapytania związane z obrazem i pikselami
@RestController
public class ImageController {

    // KROK 3: Pobieranie obrazu w formacie base64
    @GetMapping("/image")
    public String getImageRGB() {
        ImageRGB image = ImageRGB.getInstance(); // Pobranie instancji obrazu

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); // Strumień wyjściowy do zapisania obrazu

        try {
            ImageIO.write(image.getImage(), "png", outputStream); // Zapisanie obrazu do strumienia
        } catch (final IOException e) {
            throw new UncheckedIOException(e); // Obsługa błędu zapisu
        }

        // Generowanie kodu HTML z obrazem w formacie base64
        String output = "<div>\n" +
                "  <img src=\"data:image/jpeg;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray()) + "\" alt=\"IMG\" />\n" +
                "</div>";

        return output; // Zwrócenie obrazu w formacie base64 jako string HTML
    }

    // KROK 4
    // http://localhost:8080/pixel?tokenId=1&x=300&y=300&color=0x0015FF
    // albo curl: curl -X POST http://localhost:8080/pixel?tokenId=11"&"x=40"&"y=70"&"color=%2300FF00
    //            curl -X POST "http://localhost:8080/pixel?tokenId=12&x=80&y=130&color=%2300FF00"
    public ResponseEntity setColorOfPixel(@RequestParam("tokenId") int tokenId, @RequestParam("x") int x, @RequestParam("y") int y, @RequestParam("color") String color) {
        System.out.println("setColor_start"); // Logowanie rozpoczęcia operacji

        // Sprawdzenie, czy podany token istnieje
        boolean tokenFound = false;
        for (Token token1 : Token.getTokens()) {
            if (token1.getId() == tokenId) {

                tokenFound = true;

                // Sprawdzenie, czy token jest aktywny
                if (!token1.isTokenActive()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("token nieaktywny"); // Token nieaktywny
                } else {
                    break;
                }
            }
        }

        if (!tokenFound) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak tokenu"); // Token nie został znaleziony
        }

        // Sprawdzenie, czy współrzędne piksela są w granicach obrazu
        if (x < 0 || y < 0 || x > 512 || y > 512) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Podano złe dane"); // Współrzędne są nieprawidłowe
        }

        // Ustawienie koloru piksela na obrazie
        ImageRGB imageRGB = ImageRGB.getInstance();
        imageRGB.setPixelOfImage(x, y, color);

        // Dodanie piksela do bazy danych
        Database database = Database.getInstance();
        database.addPixelToDatabase(tokenId, x, y, color);

        System.out.println("setColor_end"); // Logowanie zakończenia operacji

        return ResponseEntity.status(HttpStatus.OK).body("Git"); // Operacja zakończona sukcesem
    }
}
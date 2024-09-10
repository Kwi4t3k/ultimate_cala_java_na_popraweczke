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

@RestController
public class ImageController {
    //    KROK 3

    @GetMapping("/image")
    public String getImageRGB() {
        ImageRGB image = ImageRGB.getInstance();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(image.getImage(), "png", outputStream);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

        String output = "<div>\n" +
                "  <img src=\"data:image/jpeg;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray()) + "\" alt=\"IMG\" />\n" +
                "</div>";

        return output;
    }

    // KROK 4
    @PostMapping("/pixel")
    public ResponseEntity setColorOfPixel(@RequestParam int tokenId, @RequestParam int x, @RequestParam int y, @RequestParam String color) {
        System.out.println("setColor_start");

        boolean tokenFound = false;
        for (Token token1 : Token.getTokens()) {
            if (token1.getId() == tokenId) {

                tokenFound = true;

                if (!token1.isTokenActive()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("token nieaktywny");
                } else {
                    break;
                }
            }
        }

        if (!tokenFound) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak tokenu");
        }

        if (x < 0 || y < 0 || x > 512 || y > 512) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Podano złe dane");
        }

        ImageRGB imageRGB = ImageRGB.getInstance();
        imageRGB.setPixelOfImage(x, y, color);

        Database database = Database.getInstance();
        database.addPixelToDatabase(tokenId, x, y, color);
        database.closeConnection();

        System.out.println("setColor_end");

        return ResponseEntity.status(HttpStatus.OK).body("Git");
    }
}
package org.example;

import org.springframework.web.bind.annotation.GetMapping;
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
}
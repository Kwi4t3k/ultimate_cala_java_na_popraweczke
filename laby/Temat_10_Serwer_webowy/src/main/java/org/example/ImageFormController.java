package org.example;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

//musi być zwykły controller, bo jakby był rest to  nie pobrałby sobie templatki
@Controller
public class ImageFormController {
    @GetMapping("/home")
    public String viewHomePage() {
        return "index";
    }

    @PostMapping("/imageform/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file, Model model) {
        // Przekazanie obrazu do widoku image.html
        try {
            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            model.addAttribute("image", base64Image);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/home"; // Jeśli wystąpi błąd, wróć do strony głównej
        }

        // Zwracanie widoku image.html, aby wyświetlić obraz
        return "image";
    }

    @PostMapping("/imageform/upload2")
    public String uploadImage2(@RequestParam("image") MultipartFile file,
                              @RequestParam("brightness") int brightness,
                              Model model) {
        try {
            // Pobieranie obrazu z przesłanego pliku
            byte[] bytes = file.getBytes();
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));

            // Zmiana jasności obrazu
            BufferedImage brightenedImage = adjustBrightness(bufferedImage, brightness);

            // Konwersja obrazu do formatu Base64
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(brightenedImage, "jpg", os);
            String base64Image = Base64.getEncoder().encodeToString(os.toByteArray());

            // Przekazanie obrazu do widoku
            model.addAttribute("image", base64Image);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/home"; // W przypadku błędu powrót do strony głównej
        }

        // Zwracanie widoku image.html, aby wyświetlić obraz
        return "image";
    }

    // Funkcja do zmiany jasności obrazu
    private BufferedImage adjustBrightness(BufferedImage image, int level) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int b = rgb & 0XFF;
                int g = (rgb & 0XFF00) >> 8;
                int r = (rgb & 0XFF0000) >> 16;

                int newB = clamp(b + level, 0, 255);
                int newG = clamp(g + level, 0, 255);
                int newR = clamp(r + level, 0, 255);

                image.setRGB(x, y, (newR << 16) + (newG << 8) + newB);
            }
        }
        return image;
    }

    // Metoda pomocnicza do ograniczenia wartości RGB
    private int clamp(int value, int min, int max) {
        if (value > max)
            return max;
        if (value < min)
            return min;
        return value;
    }
}
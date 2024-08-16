package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/*
zadanie 6
Napisz kontroler REST ImageController, w którym znajdzie się metoda zawracająca obraz ze zmodyfikowaną jasnością.
Metoda typu GET powinna przyjąć obraz w formacie base64 oraz liczbę całkowitą określającą jasność.
Metoda powinna rozjaśnić obraz o podaną wartość i zwrócić go w formacie base64.*/

// rest controller nie pozwoli na pobranie templatki html, czyl ijakby dać return image.html,
// to wyświetli na stronie "image.html", a nie afaktyczny plik html
// jak będzie zwykły @Controller, to wtedy pobierze sobie tę tempaltkę html
@RestController
public class ImageController {

    /**
     * Metoda showBrightnessImage przyjmuje obraz w formacie base64 oraz poziom jasności,
     * modyfikuje jasność obrazu, a następnie zwraca go w formie elementu HTML <img>.
     *
     * @param base64Image obraz zakodowany w formacie Base64, który ma zostać przetworzony
     * @param level poziom jasności, o jaki ma zostać zmodyfikowany obraz
     * @return zmodyfikowany obraz w formie elementu HTML <img>
     */
    @GetMapping("/imageBrightness")
    public String showBrightnessImage(@RequestParam String base64Image, @RequestParam int level) {
        // Sprawdzenie, czy ciąg base64 zawiera prefiks typu danych, np. "data:image/jpeg;base64,"
        if (base64Image.startsWith("data:image")) {
            // Jeśli tak, usuwamy ten prefiks, aby pozostał tylko właściwy ciąg base64
            base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
        }
        // Zamiana spacji na znak plus (+), ponieważ czasami formatowanie base64 może być błędne
        base64Image = base64Image.replace(" ", "+");
        // Usunięcie wszystkich nowych linii, aby ciąg base64 był ciągły
        base64Image = base64Image.replace("\n", "");

        // Wywołanie metody, która zwiększa jasność obrazu i zwraca go w formacie base64
        String increasedBrightness = increaseBrigthnessReturnBase64(level, base64Image);

        // Utworzenie i zwrócenie elementu HTML <img>, który zawiera zmodyfikowany obraz
        String output = "<div>\n" +
                "  <img src=\"data:image/jpeg;base64," + increasedBrightness + "\" alt=\"Cursed cat\" />\n" +
                "</div>";
        return output;
    }

    /**
     * Metoda increaseBrigthnessReturnBase64 zwiększa jasność obrazu, a następnie zwraca
     * go w formacie base64.
     *
     * @param level poziom jasności, o jaki ma zostać zmodyfikowany obraz
     * @param base64Image obraz zakodowany w formacie Base64
     * @return zmodyfikowany obraz w formacie Base64
     */
    private String increaseBrigthnessReturnBase64(int level, String base64Image) {
        // Przekształcenie base64 na BufferedImage i zwiększenie jasności obrazu
        BufferedImage image = increaseBrigthnessReturnBufferedImage(level, base64Image);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            // Zapisanie zmodyfikowanego obrazu do strumienia ByteArrayOutputStream w formacie JPG
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            // Jeśli wystąpi błąd, obsługujemy go, chociaż w tym przypadku nie robimy nic (można dodać logowanie)
        }
        // Zwrócenie zmodyfikowanego obrazu w formacie Base64
        return Base64.getEncoder().encodeToString(os.toByteArray());
    }

    /*
    Zadanie 7.
    Napisz kolejną, zbliżoną metodę, w której wyniku znajdzie się niezakodowany obraz.
    */

    /**
     * Metoda increaseBrigthnessReturnBufferedImage przekształca obraz zakodowany w formacie base64
     * na BufferedImage, a następnie zwiększa jego jasność o podany poziom.
     *
     * @param level poziom jasności, o jaki ma zostać zmodyfikowany obraz
     * @param base64Image obraz zakodowany w formacie Base64
     * @return zmodyfikowany obraz w formacie BufferedImage
     */
    private BufferedImage increaseBrigthnessReturnBufferedImage(int level, String base64Image) {
        // Konwersja ciągu base64 na tablicę bajtów
        byte[] bytes = DatatypeConverter.parseBase64Binary(base64Image);
        BufferedImage image = null;
        try {
            // Wczytanie tablicy bajtów jako obrazu BufferedImage
            image = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            // Obsługa błędów odczytu obrazu - wyrzucenie wyjątku runtime, jeśli coś pójdzie nie tak
            throw new RuntimeException(e);
        }

        // Iteracja po każdym pikselu obrazu w celu zwiększenia jego jasności
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                // Pobranie wartości RGB piksela
                int rgb = image.getRGB(x, y);

                // Rozbicie wartości RGB na składowe niebieską (B), zieloną (G) i czerwoną (R)
                int b = rgb & 0XFF;
                int g = (rgb & 0XFF00) >> 8;
                int r = (rgb & 0XFF0000) >> 16;

                // Zwiększenie wartości każdej składowej o wartość level z zachowaniem zakresu 0-255
                int newB = clamp(b + level, 0, 255);
                int newG = clamp(g + level, 0, 255);
                int newR = clamp(r + level, 0, 255);

                // Złożenie nowego koloru i ustawienie go na obrazie
                image.setRGB(x, y, (newR << 16) + (newG << 8) + newB);
            }
        }
        // Zwrócenie zmodyfikowanego obrazu
        return image;
    }

    /**
     * Metoda clamp ogranicza wartość do zakresu między min i max.
     *
     * @param value wartość do sprawdzenia
     * @param min minimalna dopuszczalna wartość
     * @param max maksymalna dopuszczalna wartość
     * @return wartość ograniczona do przedziału [min, max]
     */
    private static int clamp(int value, int min, int max) {
        if (value > max)
            return max;
        if (value < min)
            return min;
        return value;
    }
}

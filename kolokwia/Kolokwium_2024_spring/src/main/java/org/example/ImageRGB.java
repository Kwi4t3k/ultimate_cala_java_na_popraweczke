package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageRGB {
    // KROK 3 - Tworzymy obraz 512x512 pikseli, z kolorami w formacie RGB

    private BufferedImage image; // Obiekt reprezentujący obraz w pamięci
    private static ImageRGB instance; // Statyczna instancja klasy (singleton)

    // Konstruktor klasy ImageRGB, tworzy nowy obraz o wymiarach 512x512 z użyciem przestrzeni barw RGB
    public ImageRGB() {
        this.image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        // Utworzono nowy obraz z kolorami w formacie RGB o rozmiarze 512x512
    }

    // Getter zwracający obiekt obrazu
    public BufferedImage getImage() {
        return image; // Zwracamy aktualny obraz
    }

    // Singleton - metoda, która zapewnia jedyną instancję obiektu ImageRGB
    public static ImageRGB getInstance() {
        if (instance == null) { // Sprawdzamy, czy instancja już istnieje
            instance = new ImageRGB(); // Tworzymy nową instancję, jeśli jeszcze nie istnieje
        }
        return instance; // Zwracamy instancję
    }

    // KROK 4 - Ustawianie piksela na obrazie
    public void setPixelOfImage(int x, int y, String hexColor) {
        Color color = Color.decode(hexColor); // Przekształcamy wartość HEX na obiekt koloru
        int rgb = color.getRGB(); // Pobieramy wartość RGB koloru
        image.setRGB(x, y, rgb); // Ustawiamy piksel na pozycji (x, y) na obrazku, z podanym kolorem
    }

    // KROK 6 - Ustawianie obrazu na podstawie pikseli z bazy danych
    public void setImageBasedOnPixels() {
        Database database = Database.getInstance(); // Pobieramy instancję bazy danych
        List<Pixel> pixels = database.getListOfPixelsFromDatabase(); // Pobieramy listę pikseli z bazy danych

        // Przechodzimy przez wszystkie piksele pobrane z bazy i ustawiamy je na obrazie
        for (Pixel pixel : pixels) {
            setPixelOfImage(pixel.getX(), pixel.getY(), pixel.getColor()); // Ustawiamy piksele na obrazku
        }
    }
}
package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageRGB {
    // KROK 3

    private BufferedImage image;
    private static ImageRGB instance;

    public ImageRGB() {
        this.image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage getImage() {
        return image;
    }

    //singleton | potrzebuje jednej zmiennej
    public static ImageRGB getInstance() {
        if (instance == null) {
            instance = new ImageRGB();
        }
        return instance;
    }

    // KROK 4
    public void setPixelOfImage(int x, int y, String hexColor) {
        Color color = Color.decode(hexColor);
        int rgb = color.getRGB();
        image.setRGB(x, y, rgb);
    }

    // KROK 6

    public void setImageBasedOnPixels(){
        Database database = Database.getInstance();
        List<Pixel> pixels = database.getListOfPixelsFromDatabase();

        for (Pixel pixel : pixels) {
            setPixelOfImage(pixel.getX(), pixel.getY(), pixel.getColor());
        }
    }
}
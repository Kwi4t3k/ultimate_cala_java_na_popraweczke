package org.example;

import java.awt.image.BufferedImage;

public class ImageRGB {
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
}
package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.scene.canvas.GraphicsContext;

public abstract class GraphicsItem {
    protected static double canvasWidth, canvasHeight;  // Statyczne wymiary Canvas

    // Metoda ustawiająca rozmiary Canvas
    public static void setCanvasWidthHeight(double canvasWidth, double canvasHeight) {
        GraphicsItem.canvasWidth = canvasWidth;
        GraphicsItem.canvasHeight = canvasHeight;
    }

    protected double x, y, width, height;  // Wymiary i położenie obiektu

    public double getX() {
        return x;  // Pobieranie współrzędnej x obiektu
    }

    public double getY() {
        return y;  // Pobieranie współrzędnej y obiektu
    }

    public double getWidth() {
        return width;  // Pobieranie szerokości obiektu
    }

    public double getHeight() {
        return height;  // Pobieranie wysokości obiektu
    }

    // Metoda abstrakcyjna do rysowania obiektu
    public abstract void draw(GraphicsContext graphicsContext);
}
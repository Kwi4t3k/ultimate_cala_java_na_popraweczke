package org.example.kolokwium_2021;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class appCanvas extends Canvas {
    private GraphicsContext graphicsContext;

    public appCanvas(double width, double height) {
        super(width, height);

        graphicsContext = this.getGraphicsContext2D();

        drawBackground();
    }

    // Metoda rysująca białe tło
    public void drawBackground() {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());
    }

    // Metoda rysująca linię
    public void drawLine(double x1, double y1, double x2, double y2) {
        graphicsContext.strokeLine(x1, y1, x2, y2);
    }

    public void setColor(String hexColor) {
        Color color = Color.web("#" + hexColor);
        graphicsContext.setStroke(color);
    }
}
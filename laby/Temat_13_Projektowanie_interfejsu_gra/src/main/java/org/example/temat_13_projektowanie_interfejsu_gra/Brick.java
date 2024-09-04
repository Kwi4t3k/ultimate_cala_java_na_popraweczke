package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

//klasa reprezentuje jedną z cegieł
public class Brick extends GraphicsItem {
    private static int gridRows, gridCols;
    private Paint color;

    public enum CrushType {NoCrush, HorizontalCrush, VerticalCrush}

    // Metoda do ustawiania rozmiaru siatki
    public static void setGrid(int rows, int cols) {
        gridRows = rows;
        gridCols = cols;
    }

    // Konstruktor przyjmujący pozycję x, y na siatce oraz kolor cegły
    public Brick(int gridX, int gridY, Paint color) {
        this.color = color;

        // Obliczenie szerokości i wysokości cegły na podstawie rozmiarów siatki
        this.width = canvasWidth / gridCols;
        this.height = canvasHeight / gridRows;

        // Ustawienie pozycji cegły w odpowiednim miejscu na siatce
        this.x = gridX * this.width;
        this.y = gridY * this.height;
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        // Rysowanie prostokąta reprezentującego cegłę
        graphicsContext.setFill(color);
        graphicsContext.fillRect(x, y, width, height);

        // Dodanie efektu trójwymiarowości (cienie)
        graphicsContext.setFill(Color.grayRgb(20, 0.4));
        graphicsContext.fillRect(x + 5, y + 5, width - 5, height - 5);
    }

    public CrushType checkCollision(double ballTop, double ballBottom, double ballLeft, double ballRight) {
        boolean collisionVertical = (ballRight > x && ballLeft < x + width) &&
                (ballBottom >= y && ballTop <= y);

        boolean collisionHorizontal = (ballBottom > y && ballTop < y + height) &&
                (ballRight >= x && ballLeft <= x);

        if (collisionVertical) {
            return CrushType.VerticalCrush;
        } else if (collisionHorizontal) {
            return CrushType.HorizontalCrush;
        }

        return CrushType.NoCrush;
    }
}

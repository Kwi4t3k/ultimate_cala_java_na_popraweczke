package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

// Klasa reprezentująca jedną cegłę
public class Brick extends GraphicsItem {
    private static int gridRows, gridCols;  // Liczba wierszy i kolumn w siatce
    private Paint color;  // Kolor cegły

    // Typy kolizji cegły z piłką
    public enum CrushType {NoCrush, HorizontalCrush, VerticalCrush}

    // Metoda do ustawiania rozmiaru siatki cegieł
    public static void setGrid(int rows, int cols) {
        gridRows = rows;
        gridCols = cols;
    }

    // Konstruktor cegły przyjmujący pozycję na siatce oraz kolor
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
        // Rysowanie cegły jako prostokąta
        graphicsContext.setFill(color);
        graphicsContext.fillRect(x, y, width, height);

        // Dodanie efektu trójwymiarowości (cienie)
        graphicsContext.setFill(Color.grayRgb(20, 0.4));
        graphicsContext.fillRect(x + 5, y + 5, width - 5, height - 5);
    }

    // Metoda sprawdzająca kolizję cegły z piłką
    public CrushType checkCollision(double ballTop, double ballBottom, double ballLeft, double ballRight) {
        // Sprawdzanie kolizji w pionie
        boolean collisionVertical = (ballRight > x && ballLeft < x + width) &&
                (ballBottom >= y && ballTop <= y);

        // Sprawdzanie kolizji w poziomie
        boolean collisionHorizontal = (ballBottom > y && ballTop < y + height) &&
                (ballRight >= x && ballLeft <= x);

        // Zwracanie typu kolizji
        if (collisionVertical) {
            return CrushType.VerticalCrush;
        } else if (collisionHorizontal) {
            return CrushType.HorizontalCrush;
        }

        return CrushType.NoCrush;
    }
}
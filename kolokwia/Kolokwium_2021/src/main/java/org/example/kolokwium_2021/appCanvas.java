package org.example.kolokwium_2021;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class appCanvas extends Canvas {
    private GraphicsContext graphicsContext; // Kontekst graficzny do rysowania
    private double offsetX = 0; // Przesunięcie w osi X
    private double offsetY = 0; // Przesunięcie w osi Y

    // Lista do przechowywania wszystkich narysowanych odcinków
    private List<LineSegment> lineSegments = new ArrayList<>();

    public appCanvas(double width, double height) {
        super(width, height); // Inicjalizacja płótna z określoną szerokością i wysokością

        graphicsContext = this.getGraphicsContext2D(); // Pobranie kontekstu graficznego

        drawBackground(); // Rysowanie tła
    }

    // Metoda rysująca białe tło
    public void drawBackground() {
        graphicsContext.setFill(Color.WHITE); // Ustawienie koloru wypełnienia tła na biały
        graphicsContext.fillRect(0, 0, getWidth(), getHeight()); // Rysowanie prostokąta wypełniającego całe płótno
    }

    // Metoda rysująca linię
    public void drawLine(double x1, double y1, double x2, double y2, String hexColor) {
        // Zapisujemy linię do listy
        lineSegments.add(new LineSegment(x1, y1, x2, y2, hexColor));

        // Przerysowywanie wszystkich odcinków
        redraw();
    }

    public void setColor(String hexColor) {
        Color color = Color.web("#" + hexColor); // Konwersja koloru HEX na obiekt Color
        graphicsContext.setStroke(color); // Ustawienie koloru rysowania
    }

    // Metoda do ustawiania przesunięcia układu współrzędnych
    public void setOffset(double offsetX, double offsetY) {
        this.offsetY = offsetY;
        this.offsetX = offsetX;
    }

    // Metoda do przerysowywania wszystkich odcinków z uwzględnieniem przesunięcia
    public void redraw() {
        drawBackground(); // Rysowanie tła

        for (LineSegment segment : lineSegments) {
            Color color = Color.web("#" + segment.color); // Konwersja koloru HEX na obiekt Color
            graphicsContext.setStroke(color); // Ustawienie koloru rysowania

            // Rysowanie odcinków z uwzględnieniem przesunięcia
            graphicsContext.strokeLine(
                    segment.x1 + offsetX, segment.y1 + offsetY,
                    segment.x2 + offsetX, segment.y2 + offsetY
            );
        }
    }
}
package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Klasa reprezentująca platformę, od której odbija się piłka
public class Paddle extends GraphicsItem {
    public Paddle() {
        // Ustawienie początkowych wymiarów i pozycji platformy
        this.width = 100;  // Szerokość platformy
        this.height = 20;  // Wysokość platformy
        this.x = (canvasWidth - width) / 2;  // Centrowanie platformy na osi x
        this.y = canvasHeight - height - 10;  // Ustawienie platformy blisko dolnej krawędzi
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        // Rysowanie platformy jako prostokąta
        graphicsContext.setFill(Color.GRAY);
        graphicsContext.fillRect(x, y, width, height);
    }

    // Metoda przesuwająca platformę do pozycji myszy
    public void move(double mouseX) {
        this.x = mouseX - width / 2;  // Przesunięcie platformy na podstawie pozycji myszy

        // Zapobieganie wyjściu platformy poza krawędź Canvas
        if (this.x < 0) {
            this.x = 0;  // Ustawienie platformy na lewej krawędzi
        } else if (this.x + width > canvasWidth) {
            this.x = canvasWidth - width;  // Ustawienie platformy na prawej krawędzi
        }
    }
}
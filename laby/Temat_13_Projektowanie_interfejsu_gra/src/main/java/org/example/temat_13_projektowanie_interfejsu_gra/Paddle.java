package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

//reprezentuje platformę, od której odbija się piłka
public class Paddle extends GraphicsItem {
    public Paddle() {
        // Ustawienie pozycji platformy na dole ekranu, szerokość i wysokość platformy
        this.width = 100; // szerokość platformy
        this.height = 20; // wysokość platformy
        this.x = (canvasWidth - width) / 2; // centrowanie na osi x
        this.y = canvasHeight - height - 10; // umiejscowienie blisko dolnej krawędzi
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        // Rysowanie platformy jako prostokąta w odpowiedniej pozycji
        graphicsContext.setFill(Color.GRAY);
        graphicsContext.fillRect(x, y, width, height);
    }

    // Metoda do ustawiania pozycji platformy na podstawie składowej x
    public void move(double mouseX) {
        this.x = mouseX - width / 2;

//         Zapobieganie wyjściu platformy poza krawędź kanwy
        if (this.x < 0) {
            this.x = 0;
        } else if (this.x + width > canvasWidth) {
            this.x = canvasWidth - width;
        }
    }
}

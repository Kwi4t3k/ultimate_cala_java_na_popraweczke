package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Klasa reprezentująca piłkę
public class Ball extends GraphicsItem {
    private Point2D moveVector;  // Wektor ruchu piłki
    private double velocity;  // Prędkość piłki

    private double previousX, previousY;  // Poprzednia pozycja piłki

    public Ball() {
        // Ustawienie początkowych wymiarów i parametrów piłki
        this.width = 15;  // Średnica piłki
        this.height = 15;

        this.velocity = 200;  // Początkowa prędkość piłki (piksele na sekundę)

        // Ustawienie wektora ruchu piłki pod kątem 45 stopni
        this.moveVector = new Point2D(1, -1).normalize();

        // Inicjalizacja poprzedniej pozycji piłki
        this.previousX = this.x;
        this.previousY = this.y;
    }

    // Metoda ustawiająca pozycję piłki
    public void setPosition(Point2D point2D) {
        this.x = point2D.getX() - width / 2;
        this.y = point2D.getY() - height / 2;
    }

    // Metoda aktualizująca pozycję piłki na podstawie upływu czasu
    public void updatePosition(double seconds) {
        this.previousX = this.x;
        this.previousY = this.y;

        // Aktualizacja pozycji piłki
        this.x += moveVector.getX() * velocity * seconds;
        this.y += moveVector.getY() * velocity * seconds;

        // Obsługa kolizji ze ścianami przeniesiona do metod prywatnych
//        if (this.x <= 0 || this.x + this.width >= canvasWidth) {
//            moveVector = new Point2D(-moveVector.getX(), moveVector.getY());
//        }
//
//        if (this.y <= 0 || this.y + this.height >= canvasHeight) {
//            moveVector = new Point2D(moveVector.getX(), -moveVector.getY());
//        }

        //test do pozycji piłki
//        System.out.println("Ball position: x=" + this.x + ", y=" + this.y);
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        // Rysowanie piłki jako okręgu
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(x, y, width, height);
    }

    // Metoda odbijająca piłkę w poziomie
    public void bounceHorizontally() {
        this.moveVector = new Point2D(-moveVector.getX(), moveVector.getY());
    }

    // Metoda odbijająca piłkę w pionie
    public void bounceVertically() {
        this.moveVector = new Point2D(moveVector.getX(), -moveVector.getY());
    }

    // Metody zwracające skrajne punkty piłki
    public double getTop() {
        return y;
    }

    public double getBottom() {
        return y + height;
    }

    public double getLeft() {
        return x;
    }

    public double getRight() {
        return x + width;
    }

    public double getPreviousBottom() {
        return previousY + height;
    }

    public double getPreviousTop() {
        return previousY;
    }

    public double getPreviousLeft() {
        return previousX;
    }

    public double getPreviousRight() {
        return previousX + width;
    }

    // Metoda odbijająca piłkę od platformy, zmieniając wektor ruchu zależnie od pozycji uderzenia
    public void bounceFromPaddle(double hitPosition) {
        // Obliczanie nowego kąta odbicia w zależności od pozycji uderzenia
        double maxAngle = Math.toRadians(60);  // Maksymalny kąt odbicia (60 stopni)
        double angle = hitPosition * maxAngle;  // Przesunięcie kąta odbicia w zależności od hitPosition

        // Nowy wektor ruchu po odbiciu
        double newDirectionX = Math.sin(angle);
        double newDirectionY = -Math.cos(angle);  // Ujemna wartość, aby odbić piłkę do góry

        // Ustawienie nowego znormalizowanego wektora ruchu
        this.moveVector = new Point2D(newDirectionX, newDirectionY).normalize();

        // Przyspieszenie piłki po odbiciu (opcjonalne)
        this.velocity *= 1.05;
    }
}
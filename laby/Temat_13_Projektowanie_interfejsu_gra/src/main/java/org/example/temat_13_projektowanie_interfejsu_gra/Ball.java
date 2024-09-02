package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

//Klasa reprezentująca piłkę
public class Ball extends GraphicsItem {
    private Point2D moveVector;
    private double velocity;

    public Ball() {
        // Średnica piłki
        this.width = 15;
        this.height = 15;

        // Początkowa szybkość piłki (piksele na sekundę)
        this.velocity = 200;

        // Wektor ruchu pod kątem 45 stopni
        this.moveVector = new Point2D(1, -1).normalize();
    }

    public void setPosition(Point2D point2D) {
        this.x = point2D.getX() - width / 2;
        this.y = point2D.getY() - height / 2;
    }

    public void updatePosition(double seconds){
        this.x += moveVector.getX() * velocity * seconds;
        this.y += moveVector.getY() * velocity * seconds;

        // Obsługa kolizji ze ścianami
        if (this.x <= 0 || this.x + this.width >= canvasWidth) {
            moveVector = new Point2D(-moveVector.getX(), moveVector.getY());
        }

        if (this.y <= 0 || this.y + this.height >= canvasHeight) {
            moveVector = new Point2D(moveVector.getX(), -moveVector.getY());
        }

        System.out.println("Ball position: x=" + this.x + ", y=" + this.y);
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(x, y, width, height);
    }
}

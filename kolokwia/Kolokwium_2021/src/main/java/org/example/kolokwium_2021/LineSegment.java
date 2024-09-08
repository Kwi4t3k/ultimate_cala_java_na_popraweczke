package org.example.kolokwium_2021;

// Klasa do przechowywania danych o odcinkach
public class LineSegment {
    double x1, y1, x2, y2; // Współrzędne końców odcinka
    String color; // Kolor odcinka

    public LineSegment(double x1, double y1, double x2, double y2, String color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }
}
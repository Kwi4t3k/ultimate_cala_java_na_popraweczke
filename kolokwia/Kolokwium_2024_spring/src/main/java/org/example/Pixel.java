package org.example;

public class Pixel {
    // KROK 6 - Klasa reprezentująca pojedynczy piksel na obrazie

    private int x; // Współrzędna x piksela
    private int y; // Współrzędna y piksela
    private String color; // Kolor piksela w formacie HEX

    // Konstruktor klasy Pixel, inicjuje współrzędne piksela i jego kolor
    public Pixel(int x, int y, String color) {
        this.x = x; // Ustawiamy wartość współrzędnej x
        this.y = y; // Ustawiamy wartość współrzędnej y
        this.color = color; // Ustawiamy kolor w formacie HEX
    }

    // Getter zwracający współrzędną x piksela
    public int getX() {
        return x; // Zwracamy współrzędną x
    }

    // Getter zwracający współrzędną y piksela
    public int getY() {
        return y; // Zwracamy współrzędną y
    }

    // Getter zwracający kolor piksela
    public String getColor() {
        return color; // Zwracamy kolor piksela w formacie HEX
    }
}
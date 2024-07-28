package org.example.singleton;

// Klasa z metodą main do testowania Singletona
public class SingletonExample {
    public static void main(String[] args) {
        // Uzyskiwanie jedynej instancji klasy DatabaseConnection
        DatabaseConnection connection1 = DatabaseConnection.getInstance();
        DatabaseConnection connection2 = DatabaseConnection.getInstance();

        // Sprawdzenie, czy oba odniesienia wskazują na tę samą instancję
        System.out.println(connection1 == connection2); // true

        // Wywołanie metody na instancji
        connection1.connect();
    }
}

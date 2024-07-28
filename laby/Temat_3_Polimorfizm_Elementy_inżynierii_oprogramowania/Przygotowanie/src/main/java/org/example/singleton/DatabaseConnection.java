package org.example.singleton;

//4. Singleton
//Cel: Singleton zapewnia, że dana klasa ma tylko jedną instancję i zapewnia globalny punkt dostępu do niej.
//
//Przykład:
//
//Załóżmy, że mamy klasę DatabaseConnection, która powinna mieć tylko jedną instancję.

// Klasa Singleton
// Klasa Singleton
public class DatabaseConnection {
    private static DatabaseConnection instance;

    // Prywatny konstruktor zapobiega tworzeniu instancji z zewnątrz
    private DatabaseConnection() {
        // prywatny konstruktor
    }

    // Metoda statyczna do uzyskania jedynej instancji klasy
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public void connect() {
        System.out.println("Connecting to the database...");
    }
}
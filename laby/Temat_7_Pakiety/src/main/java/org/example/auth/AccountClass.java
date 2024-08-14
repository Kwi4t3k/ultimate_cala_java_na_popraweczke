package org.example.auth;

public class AccountClass {
    private final int id;
    private final String name;

    // Konstruktor
    public AccountClass(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter dla id
    public int getId() {
        return id;
    }

    // Getter dla name
    public String getName() {
        return name;
    }

    // Opcjonalnie, możesz dodać metody equals, hashCode i toString, jeśli potrzebujesz
}

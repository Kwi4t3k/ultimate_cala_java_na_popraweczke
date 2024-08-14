package org.example;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptionExample {

    public static void main(String[] args) {
        String password = "mySecretPassword";

        // Hashowanie hasła
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("Hasło zaszyfrowane: " + hashedPassword);

        // Sprawdzanie hasła
        boolean matched = BCrypt.checkpw(password, hashedPassword);
        System.out.println("Czy hasło pasuje? " + matched);
    }
}

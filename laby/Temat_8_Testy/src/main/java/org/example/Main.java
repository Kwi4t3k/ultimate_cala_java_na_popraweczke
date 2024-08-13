package org.example;

import org.example.database.DatabaseConnection;
import org.example.auth.Account;

import javax.naming.AuthenticationException;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.connect("src/main/resources/my.db");

        Account.Persistence.init();

//        Account.Persistence.register("notch", "verysecurepassword");

        try {
            Account notch = Account.Persistence.authenticate("notch", "verysecurepassword");
            System.out.println(notch);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
        //test to commit
        DatabaseConnection.disconnect();
    }
}
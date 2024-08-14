package shop.music;
import org.example.*;
import org.example.auth.AccountManager;
import org.example.database.DatabaseConnection;
import shop.Cart;
import shop.Product;
import shop.ShopClient;

import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
//    public static void main(String[] args) {
        //zad 1
//        DatabaseConnection connection = new DatabaseConnection();
//        try {
//            connection.connect(Path.of("src/main/resources/my.db"));
//            Statement statement = connection.getConnection().createStatement();
//
//            // Tworzenie tabeli person
//            statement.executeUpdate("create table if not exists person (id integer unique, name text)");
//
//            // Wstawianie danych do tabeli person
//            statement.executeUpdate("insert into person (id, name) values(1, 'leo')");
//            statement.executeUpdate("insert into person (id, name) values(2, 'yui')");
//
//            // Zapytanie do tabeli person
//            ResultSet result = statement.executeQuery("select * from person");
//
//            while (result.next()) {
//                System.out.println("name: " + result.getString("name"));
//                System.out.println("id: " + result.getInt("id"));
//            }
//
//            // Zamknięcie połączenia
//            connection.disconnect();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        //zad 2
//        DatabaseConnection connection = new DatabaseConnection();
//        try {
//            connection.connect(Path.of("src/main/resources/my.db"));
//            Statement statement = connection.getConnection().createStatement();
//
//            AccountManager accountManager = new AccountManager();
////            statement.executeUpdate("drop table account");
////            accountManager.register("adam", "nowak");
//            System.out.println(accountManager.authenticate("adam", "nowak"));
//            System.out.println("po id: " + accountManager.getAccount(1));
//            System.out.println("po name: " + accountManager.getAccount("adam"));
//
//
//            // Zamknięcie połączenia
//            connection.disconnect();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        //zad 5
        public static void main (String[]args) {
            // Tworzenie klienta sklepu
            ShopClient client = new ShopClient(1, "Adam Nowak");

            // Tworzenie produktów
            Product product1 = new Product(1, "Product A", 100.0);
            Product product2 = new Product(2, "Product B", 150.0);

            // Tworzenie koszyka i dodawanie produktów
            Cart cart1 = new Cart(1);
            cart1.add(product1, 2);
            cart1.add(product2, 1);

            // Dodanie koszyka do klienta
            client.addCart(cart1);

            // Wyświetlenie koszyków klienta
            for (Cart cart : client.getCarts()) {
                System.out.println("Koszyk ID: " + cart.getId() + ", Cena: " + cart.price());
            }
        }
    }
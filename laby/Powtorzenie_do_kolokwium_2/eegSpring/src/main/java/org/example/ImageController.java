package org.example; // Deklaracja pakietu, w którym znajduje się klasa ImageController

import org.springframework.http.ResponseEntity; // Import klasy ResponseEntity do zwracania odpowiedzi HTTP
import org.springframework.stereotype.Controller; // Import adnotacji Controller, oznaczającej klasę jako kontroler Spring MVC
import org.springframework.ui.Model; // Import klasy Model, używanej do przekazywania danych do widoku
import org.springframework.web.bind.annotation.GetMapping; // Import adnotacji GetMapping, do obsługi żądań GET
import org.springframework.web.bind.annotation.RequestParam; // Import adnotacji RequestParam, do mapowania parametrów zapytania

import java.sql.*; // Import klas Java do obsługi połączeń i zapytań SQL

@Controller // Adnotacja oznaczająca, że klasa jest kontrolerem Spring MVC
public class ImageController { // Deklaracja publicznej klasy kontrolera
    private String databaseURL = "jdbc:sqlite:src/main/resources/usereeg.db"; // Definicja URL bazy danych SQLite

    @GetMapping("/image") // Mapowanie żądania HTTP GET na metodę image()
    public String image(@RequestParam String username, @RequestParam int electrode, Model model) {
        // Deklaracja metody kontrolera obsługującej żądanie z parametrami 'username' i 'electrode' oraz modelem 'model'
        String image = null; // Inicjalizacja zmiennej 'image', która będzie przechowywać wynik zapytania SQL
        String query = "select image from user_eeg where username = ? and electrode_number = ?";
        // Deklaracja zapytania SQL do pobrania obrazu z tabeli 'user_eeg' na podstawie nazwy użytkownika i numeru elektrody

        try (Connection connection = DriverManager.getConnection(databaseURL);
             // Uzyskanie połączenia z bazą danych przy użyciu URL
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Przygotowanie zapytania SQL z podanymi parametrami

            preparedStatement.setString(1, username); // Ustawienie wartości pierwszego parametru zapytania SQL na 'username'
            preparedStatement.setInt(2, electrode); // Ustawienie wartości drugiego parametru zapytania SQL na 'electrode'

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Wykonanie zapytania SQL i uzyskanie wyników w obiekcie ResultSet
                if (resultSet.next()) { // Sprawdzenie, czy istnieje wynik w ResultSet
                    image = resultSet.getString("image"); // Pobranie wartości kolumny 'image' z wyniku zapytania
                }
            }

        } catch (SQLException e) { // Obsługa wyjątków SQL
            e.printStackTrace(); // Wypisanie stosu śladu błędu w konsoli
        }

        model.addAttribute("username", username); // Dodanie atrybutu 'username' do modelu
        model.addAttribute("electrode", electrode); // Dodanie atrybutu 'electrode' do modelu
        model.addAttribute("image", image); // Dodanie atrybutu 'image' do modelu
        return "eegimage"; // Zwrócenie nazwy widoku (szablonu Thymeleaf)
    }

    // do sprawdzenia: http://localhost:8080/image?username=exampleUser&electrode=1
}
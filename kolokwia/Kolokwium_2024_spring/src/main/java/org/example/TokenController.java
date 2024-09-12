package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // żeby pokazać że jest kontrolerem springa
public class TokenController {

    //  - Sprawdzenie działania kontrolera w przeglądarce
    // W przeglądarce moglibyśmy uzyskać dostęp do poniższej metody, która zwracałaby "hi" jako prostą odpowiedź
    //    @GetMapping sprawdzenie czy kontroler działa i wyświetla cokolwiek w przeglądarce
    //    public String hi() {
    //        return "hi";
    //    }

//    @PostMapping("/register") //modyfikacja czegoś na serwerze to POST | GET jest tylko do wyciągania
//    public String register() {
//        System.out.println("register_start");
//
//        Token token = new Token();
//
//        Map<String, String> map = new HashMap<>();
//        map.put("token", String.valueOf(token.getId())); // klucz: token, wartość: token.getId()
//        map.put("dateOfCreation", String.valueOf(token.getTimeOfCreation())); // klucz: dateOfCreation, wartość: token.getTimeOfCreation()
//
//        Gson gson = new Gson();
//        String JSONtoken = gson.toJson(map);
//
//        System.out.println("register_end");
//
//        return JSONtoken;
//    }

    // KROK 1 - Endpoint rejestrujący nowy token (POST)

    //wersja ze zwracaniem mapy | mapa przechowuje jeden token, lista tokenów jest w arrayList
    @PostMapping("/register")
    public Map<String, String> register() {
        System.out.println("register_start"); // Logowanie rozpoczęcia rejestracji

        Token token = new Token(); // Tworzymy nowy token

        // Tworzymy mapę, która przechowuje dane tokena
        Map<String, String> map = new HashMap<>();
        map.put("token", String.valueOf(token.getId())); // Klucz: token, wartość: ID tokena
        map.put("dateOfCreation", String.valueOf(token.getTimeOfCreation())); // Klucz: dateOfCreation, wartość: czas utworzenia tokena

        System.out.println("register_end"); // Logowanie zakończenia rejestracji

        return map; // Zwracamy mapę z informacjami o tokenie
    }

    //sprawdzenie register w terminalu:
    //      curl -X POST http://localhost:8080/register
    // jak nie działa to najpierw: Remove-item alias:curl | w terminalu

//    KROK 2 - Endpoint zwracający listę wszystkich tokenów
    @GetMapping("/tokens")
    public List<Token.TokenDTO> getTokens() {
        List<Token.TokenDTO> tokenDTOS = new ArrayList<>(); // Tworzymy listę DTO dla tokenów

        // Iterujemy przez wszystkie tokeny i dodajemy do listy DTO
        for (Token token : Token.getTokens()) {
            tokenDTOS.add(new Token.TokenDTO(token)); // Dodajemy każdy token w formie DTO do listy
        }

        return tokenDTOS; // Zwracamy listę tokenów DTO
    }

    // sprawdzenie w przeglądarce:
    //      http://loclhost:8080/tokens
}
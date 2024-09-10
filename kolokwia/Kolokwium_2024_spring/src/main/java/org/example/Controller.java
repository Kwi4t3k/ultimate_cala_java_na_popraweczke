package org.example;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // żeby pokazać że jest kontrolerem springa
public class Controller {

    //KROK 1

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
    //sprawdzenie register w terminalu:
    //      curl -X POST http://localhost:8080/register

    //wersja ze zwracaniem mapy | mapa przechowuje jeden token, lista tokenów jest w arrayList
    @PostMapping("/register")
    public Map<String, String> register() {
        System.out.println("register_start");

        Token token = new Token();

        Map<String, String> map = new HashMap<>();
        map.put("token", String.valueOf(token.getId())); // klucz: token, wartość: token.getId()
        map.put("dateOfCreation", String.valueOf(token.getTimeOfCreation())); // klucz: dateOfCreation, wartość: token.getTimeOfCreation()

        System.out.println("register_end");

        return map;
    }

//    KROK 2
    @GetMapping("/tokens")
    public List<Token.TokenDTO> getTokens() {
        List<Token.TokenDTO> tokenDTOS = new ArrayList<>();

        for (Token token : Token.getTokenList()) {
            tokenDTOS.add(new Token.TokenDTO(token));
        }

        return tokenDTOS;
    }

    // sprawdzenie w przeglądarce:
    //      http://loclhost:8080/tokens
}
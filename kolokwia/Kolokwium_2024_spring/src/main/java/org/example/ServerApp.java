package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        // KROK 6
        ImageRGB.getInstance().setImageBasedOnPixels();

        SpringApplication.run(ServerApp.class, args);
    }
}
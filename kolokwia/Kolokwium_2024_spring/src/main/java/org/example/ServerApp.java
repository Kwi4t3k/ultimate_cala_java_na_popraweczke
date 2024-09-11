package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        // KROK 6
        ImageRGB.getInstance().setImageBasedOnPixels();

        // KROK 7

        Server server = new Server(5000);
        server.start();

        SpringApplication.run(ServerApp.class, args);
    }
}
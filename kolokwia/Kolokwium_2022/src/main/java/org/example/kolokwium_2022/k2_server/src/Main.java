package org.example.kolokwium_2022.k2_server.src;

public class Main {
    public static void main(String[] args) {
        WordBag wordBag = new WordBag();
        wordBag.populate();
        Server server = new Server(5000, wordBag);
        server.start();
        server.startSending();
    }
}
package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //zad 1
//        Person linia = Person.fromCsvLine("Tomasz Dąbrowski,24.01.1966,,Anna Dąbrowska,");
//        System.out.println(linia + "\n");

        //zad2
//        List<Person> lista = Person.fromCsv(Path.of("src/main/resources/family.csv"));
//        int i = 1;
//        for (Person person : lista){
//            System.out.println(i + ": " + person);
//            i++;
//        }

        //zad3
//        Person linia = null;
//        try {
//            linia = Person.fromCsvLine("Anna Dąbrowska,07.02.1930,22.12.1991,Ewa Kowalska,Marek Kowalski");
//            System.out.println(linia + "\n");
//        } catch (NegativeLifespanException e) {
//            System.err.println(e.getMessage());
//        }

        //zad4
//        try {
//            List<Person> lista = Person.fromCsv(Path.of("src/main/resources/family.csv"));
//            int i = 0;
//            for (Person person : lista) {
//                System.out.println(i + ": " + person + "\n");
//                i++;
//            }
//        } catch (AmbiguousPersonException e) {
//            System.err.println("Wykryto powtarzające się imię i nazwisko: " + e.getMessage());
//        }

        //zad 5
//        try {
//            List<Person> lista = Person.fromCsv(Path.of("src/main/resources/family.csv"));
//            int i = 1;
//            for (Person person : lista) {
//                System.out.println(i + ": " + person + "\n");
//                i++;
//            }
//        } catch (AmbiguousPersonException | IOException | NegativeLifespanException e) {
//            System.err.println("Wykryto powtarzające się imię i nazwisko: " + e.getMessage());
//        }

        //zad 6
//        try {
//            List<Person> lista = Person.fromCsv(Path.of("src/main/resources/family.csv"));
//            int i = 0;
//            for (Person person : lista) {
//                System.out.println(i + ": " + person + "\n");
//                i++;
//            }
//        } catch (AmbiguousPersonException | NegativeLifespanException e) {
//            System.err.println("Wykryto błąd: " + e.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //zad 7
        try {
            Path csvPath = Path.of("src/main/resources/family.csv");
            Path binaryPath = Path.of("src/main/resources/family.dat");

            List<Person> lista = Person.fromCsv(csvPath);
            int i = 0;
            for (Person person : lista) {
                System.out.println(i + ": " + person + "\n");
                i++;
            }

            // Zapis do pliku binarnego
            Person.toBinaryFile(csvPath, binaryPath);

            // Odczyt z pliku binarnego
            List<Person> binaryList = Person.fromBinaryFile(binaryPath);
            for (Person person : binaryList) {
                System.out.println(person);
            }
        } catch (AmbiguousPersonException | NegativeLifespanException | IOException e) {
            System.err.println("Wykryto błąd: " + e.getMessage());
        }
    }
}
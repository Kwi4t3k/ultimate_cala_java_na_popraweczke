package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        System.out.println("zadanie 1: " + DeathCauseStatistic.fromCsvLine("A02.1          ,5,-,-,-,-,-,-,-,-,-,-,-,-,1,2,-,1,1,-,-,-"));
//
//        //zad 2
//        String csvLine = "A02.1,5,-,-,-,-,-,-,-,-,-,-,-,-,1,2,-,1,1,-,-,-";
//        DeathCauseStatistic statistic = DeathCauseStatistic.fromCsvLine(csvLine);
//
//        int age = 1;
//        DeathCauseStatistic.AgeBracketDeaths bracket = statistic.getAgeBracketDeathsForAge(age);
//
//        if (bracket != null) {
//            System.out.println("For age " + age + ": " + bracket.toString());
//        } else {
//            System.out.println("No age bracket found for age " + age);
//        }

        //zad 3
//        try {
//            // Utworzenie obiektu DeathCauseStatisticList
//            DeathCauseStatisticList statisticList = new DeathCauseStatisticList();
//
//            // Ścieżka do pliku CSV z danymi
//            Path path = Path.of("src/main/resources/zgony.csv");
//
//            // Repopulacja listy z pliku CSV
//            statisticList.repopulate(path);
//
//            // Testowanie metody mostDeadlyDiseases
//            int age = 50; // Przykładowy wiek
//            int n = 2;    // Liczba najczęstszych przyczyn zgonów do zwrócenia
//
//            List<DeathCauseStatistic> deadlyDiseases = statisticList.mostDeadlyDiseases(age, n);
//
//            // Wyświetlanie wyników
//            System.out.println("Najczęstsze przyczyny zgonów dla wieku " + age + ":");
//            for (DeathCauseStatistic stat : deadlyDiseases) {
//                System.out.println("Kod ICD-10: " + stat.getICD10());
//
//                DeathCauseStatistic.AgeBracketDeaths ageBracket = stat.getAgeBracketDeathsForAge(age);
//
//                System.out.println("Liczba zgonów w przedziale wiekowym " + ageBracket.young + " - " + ageBracket.old + ": " + ageBracket.deathCount);
//                System.out.println("------------------------------------");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //zad 4

        ICDCodeTabularOptimizedForMemory icdmemory = new ICDCodeTabularOptimizedForMemory("src/main/resources/icd10.txt");

        ICDCodeTabularOptimizedForTime icdtime = new ICDCodeTabularOptimizedForTime("src/main/resources/icd10.txt");

        System.out.println("MEMORY:");
        System.out.println(icdmemory.getDescription("C00"));

        System.out.println("TIME:");
        System.out.println(icdtime.getDescription("Z99.0"));
    }
}
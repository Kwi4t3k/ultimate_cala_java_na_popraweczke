package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("zadanie 1: " + DeathCauseStatistic.fromCsvLine("A02.1          ,5,-,-,-,-,-,-,-,-,-,-,-,-,1,2,-,1,1,-,-,-"));

        //zad 2
        String csvLine = "A02.1,5,-,-,-,-,-,-,-,-,-,-,-,-,1,2,-,1,1,-,-,-";
        DeathCauseStatistic statistic = DeathCauseStatistic.fromCsvLine(csvLine);

        int age = 1;
        DeathCauseStatistic.AgeBracketDeaths bracket = statistic.getAgeBracketDeathsForAge(age);

        if (bracket != null) {
            System.out.println("For age " + age + ": " + bracket.toString());
        } else {
            System.out.println("No age bracket found for age " + age);
        }
    }
}
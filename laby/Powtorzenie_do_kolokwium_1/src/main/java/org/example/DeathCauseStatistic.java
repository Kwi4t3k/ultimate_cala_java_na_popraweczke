package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathCauseStatistic {
    private String ICD10;
    private List<Integer> deathCount;

    public DeathCauseStatistic(String ICD10, List<Integer> deathCount) {
        this.ICD10 = ICD10;
        this.deathCount = deathCount;
    }

    public String getICD10() {
        return ICD10;
    }

    public static DeathCauseStatistic fromCsvLine(String line){
        String[] parts = line.split(",");

        String ICD10 = parts[0].trim(); // Usuwanie nadmiarowych białych znaków

        List<Integer> deathCounter = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) { // Iteracja przez pozostałe części (grupy wiekowe) i musi być od 1 żeby ominąć tabulator
            if (parts[i].equals("-")){ // Jeśli liczba zgonów to "-", dodajemy 0
                deathCounter.add(0);
            } else {
                deathCounter.add(Integer.valueOf(parts[i])); // W przeciwnym razie konwertujemy na int i dodajemy do listy
            }
        }

        return new DeathCauseStatistic(ICD10, deathCounter);
    }

    @Override
    public String toString() {
        return "DeathCauseStatistic{" +
                "ICD10='" + ICD10 + '\'' +
                ", deathCount=" + deathCount +
                '}';
    }

    public static class AgeBracketDeaths {
        public final int young, old, deathCount;

        public AgeBracketDeaths(int young, int old, int deathCounter) {
            this.young = young;
            this.old = old;
            this.deathCount = deathCounter;
        }

        @Override
        public String toString() {
            return "AgeBracketDeaths{" +
                    "young=" + young +
                    ", old=" + (old == Integer.MAX_VALUE ? "95+" : old) +
                    ", deathCount=" + deathCount +
                    '}';
        }
    }
    public AgeBracketDeaths getAgeBracketDeathsForAge(int age) {
        // Tablica z granicami przedziałów wiekowych w latach
        int[] ageBrackets = {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95};

        // Iteracja przez tablicę granic przedziałów wiekowych, z wyjątkiem ostatniego elementu
        for (int i = 0; i < ageBrackets.length - 1; i++) {
            // Sprawdzenie, czy podany wiek należy do bieżącego przedziału
            if (age >= ageBrackets[i] && age < ageBrackets[i + 1]) {
                // Utworzenie obiektu AgeBracketDeaths dla bieżącego przedziału
                // ageBrackets[i] - dolna granica przedziału wiekowego
                // ageBrackets[i + 1] - 1 - górna granica przedziału wiekowego (nie wlicza granicy górnej, aby obejmować przedział)
                // deathCount.get(i + 1) - liczba zgonów dla bieżącego przedziału
                return new AgeBracketDeaths(ageBrackets[i], ageBrackets[i + 1] - 1, deathCount.get(i + 1));
            }
        }

        // Sprawdzenie, czy wiek przekracza 95 lat (ostatni przedział)
        if (age > 95) {
            // Utworzenie obiektu AgeBracketDeaths dla osób powyżej 95 lat
            // 95 - dolna granica przedziału wiekowego
            // Integer.MAX_VALUE - górna granica przedziału wiekowego (reprezentuje nieskończoność)
            // deathCount.get(deathCount.size() - 1) - liczba zgonów dla ostatniego przedziału (95 lat i więcej)
            return new AgeBracketDeaths(95, Integer.MAX_VALUE, deathCount.get(deathCount.size() - 1));
        }

        // Jeśli wiek nie pasuje do żadnego z przedziałów (np. wiek jest mniejszy niż 0), zwróć null
        return null;
    }

}

package org.example;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class DeathCauseStatisticList {
    private List<DeathCauseStatistic> deathCauseStatistics = new ArrayList<>();

    public void repopulate(Path path) throws IOException {
        deathCauseStatistics.clear(); // Usuwanie istniejących danych

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;

            // Pomijanie nagłówków do linii zawierającej dane
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("Kod")) {
                    // Linia z nagłówkiem, więc przeskakujemy do następnej
                    continue;
                }
                // Przerwij, gdy napotkamy pierwszą linię z danymi
                break;
            }

            // Odczytywanie danych z pliku i tworzenie obiektów DeathCauseStatistic
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    DeathCauseStatistic statistic = DeathCauseStatistic.fromCsvLine(line);
                    deathCauseStatistics.add(statistic);
                }
            }
        }
    }

    public List<DeathCauseStatistic> mostDeadlyDiseases(int age, int n){
        List<DeathCauseStatistic> resultList = new ArrayList<>();

        // Uzyskanie odpowiedniego przedziału wiekowego
        for (DeathCauseStatistic statistic : deathCauseStatistics){
            DeathCauseStatistic.AgeBracketDeaths ageBracket = statistic.getAgeBracketDeathsForAge(age);

            if (ageBracket != null){
                resultList.add(statistic);
            }
        }

        // Sortowanie według liczby zgonów w danym przedziale wiekowym
        resultList.sort((s1, s2) -> Integer.compare(
                s2.getAgeBracketDeathsForAge(age).deathCount,
                s1.getAgeBracketDeathsForAge(age).deathCount
        ));

        // Zwracanie n najbardziej śmiertelnych chorób
        return resultList.subList(0, Math.min(n, resultList.size()));
    }
}

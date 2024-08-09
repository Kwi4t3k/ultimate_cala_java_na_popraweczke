package org.example;

import java.util.List;
import java.util.function.Predicate;

public class RangeChecker {

    // Metoda statyczna tworząca predykat sprawdzający, czy wartość znajduje się w otwartym przedziale
    public static <T extends Comparable<T>> Predicate<T> isWithinRange(T lowerBound, T upperBound) {
        // Predicate, który sprawdza, czy wartość jest większa od lowerBound i mniejsza od upperBound
        return value -> value.compareTo(lowerBound) > 0 && value.compareTo(upperBound) < 0;
    }

    // Metoda statyczna, która zlicza elementy listy spełniające warunek predykatu
    public static <T extends Comparable<T>> long countElementsInRange(List<T> list, T lowerBound, T upperBound) {
        // Użycie strumienia, aby przefiltrować elementy listy i policzyć te, które spełniają warunek predykatu
        return list.stream()
                .filter(isWithinRange(lowerBound, upperBound)) // Filtracja elementów przy użyciu predykatu
                .count(); // Zliczanie przefiltrowanych elementów
    }
}

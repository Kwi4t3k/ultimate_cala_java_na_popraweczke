package org.example;

import java.util.Collection;
import java.util.Comparator;

public class CollectionSumComparator<T extends Number> implements Comparator<Collection<T>> {
    @Override
    public int compare(Collection<T> c1, Collection<T> c2) {
        double sum1 = c1.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
        double sum2 = c2.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
        return Double.compare(sum1, sum2);
    }
}
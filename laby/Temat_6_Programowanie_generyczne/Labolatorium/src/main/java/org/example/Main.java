package org.example;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.example.CustomList.countElementsInRange;

public class Main {
    public static void main(String[] args) {
//        CustomList<Number> customList = new CustomList<>();
//        customList.addLast(1);
//        customList.addLast(2);
//        customList.addLast(3);
//        customList.addLast(4);
//
//        System.out.println("ostatni " + customList.getLast());
//
//        customList.addFirst(0);
//        System.out.println("pierwszy " + customList.getFirst());
//
//        customList.removeFirst();
//        customList.removeLast();
//
//        System.out.println("rozmiar listy " + customList.size());
//
//        System.out.println("indeks 1 listy: " + customList.get(0));
//
//        // Wyświetlanie elementów listy przy użyciu iteratora
//        System.out.println("Elementy listy przy użyciu iteratora:");
//        Iterator<Number> iterator = customList.iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//        }
//
//        // Wyświetlanie elementów listy przy użyciu strumienia
//        System.out.println("Elementy listy przy użyciu strumienia:");
//        String elements = customList.stream()
//                .map(Object::toString)
//                .collect(Collectors.joining(", "));
//        System.out.println(elements);
//
//        // Testowanie wyjątku przy próbie pobrania elementu z pustej listy
//        try {
//            customList.removeLast(); // Lista powinna być już pusta
//        } catch (NoSuchElementException e) {
//            System.out.println("Wyjątek: " + e.getMessage());
//        }
//
//        //zad4
//        customList.add(1); // Integer
//        customList.add(2.0); // Double
//        customList.add(3L); // Long
//
//        // Filtracja obiektów typu Integer
//        List<Number> integers = CustomList.filterByClass(customList, Integer.class);
//        System.out.println("Integers: " + integers);
//
//        // Filtracja obiektów typu Number (wszystkie)
//        List<Number> numbers = CustomList.filterByClass(customList, Number.class);
//        System.out.println("Numbers: " + numbers);

        //zad 5
//        CustomList<Integer> numbers = new CustomList<>();
//        numbers.add(1);
//        numbers.add(5);
//        numbers.add(10);
//        numbers.add(15);
//        numbers.add(20);
//        numbers.add(25);
//        numbers.add(30);
//
//        int lowerBound = 10;
//        int upperBound = 25;
//
//        long count = countElementsInRange(numbers, lowerBound, upperBound);
//
//        System.out.println("Number of elements in range (" + lowerBound + ", " + upperBound + "): " + count);

        //zad 6

        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5);

        // Komparator porównujący liczność
        CollectionSizeComparator sizeComparator = new CollectionSizeComparator();
        int sizeComparison = sizeComparator.compare(list1, list2);
        System.out.println("Porównanie liczności: " + sizeComparison);

        // Komparator porównujący sumy
        CollectionSumComparator<Integer> sumComparator = new CollectionSumComparator<>();
        int sumComparison = sumComparator.compare(list1, list2);
        System.out.println("Porównanie sum: " + sumComparison);
    }
}
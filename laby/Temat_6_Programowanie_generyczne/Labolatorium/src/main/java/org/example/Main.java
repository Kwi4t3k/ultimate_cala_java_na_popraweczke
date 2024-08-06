package org.example;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        CustomList<Integer> customList = new CustomList<>();
        customList.addLast(1);
        customList.addLast(2);
        customList.addLast(3);
        customList.addLast(4);

        System.out.println("ostatni " + customList.getLast());

        customList.addFirst(0);
        System.out.println("pierwszy " + customList.getFirst());

        customList.removeFirst();
        customList.removeLast();

        System.out.println("rozmiar listy " + customList.size());

        System.out.println("indeks 1 listy: " + customList.get(0));

        // Wyświetlanie elementów listy przy użyciu iteratora
        System.out.println("Elementy listy przy użyciu iteratora:");
        Iterator<Integer> iterator = customList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        // Wyświetlanie elementów listy przy użyciu strumienia
        System.out.println("Elementy listy przy użyciu strumienia:");
        String elements = customList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        System.out.println(elements);

        // Testowanie wyjątku przy próbie pobrania elementu z pustej listy
        try {
            customList.removeLast(); // Lista powinna być już pusta
        } catch (NoSuchElementException e) {
            System.out.println("Wyjątek: " + e.getMessage());
        }
    }
}
package org.example;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CustomList<T> extends AbstractList<T> {
    private Node<T> head; // Wskaźnik na początek listy
    private Node<T> tail; // Wskaźnik na koniec listy
    private int size; // Przechowywanie rozmiaru listy

    // Wewnętrzna klasa węzła, przechowująca wartość i wskaźnik na następny węzeł
    private static class Node<T> {
        T value; // Przechowywana wartość
        Node<T> next; // Wskaźnik na następny węzeł

        // Konstruktor węzła
        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    // Konstruktor inicjalizujący pustą listę
    public CustomList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Dodawanie elementu na koniec listy
    @Override
    public boolean add(T t) {
        Node<T> newNode = new Node<>(t, null); // Tworzenie nowego węzła z wartością
        if (tail == null) { // Jeśli lista jest pusta
            head = newNode; // Nowy węzeł staje się początkiem listy
            tail = newNode; // Nowy węzeł staje się końcem listy
        } else {
            tail.next = newNode; // Ustawienie wskaźnika next obecnego końca na nowy węzeł
            tail = newNode; // Nowy węzeł staje się nowym końcem listy
        }
        size++; // Zwiększenie rozmiaru listy
        return true; // Zwracanie prawdy
    }

    // Zwracanie rozmiaru listy
    @Override
    public int size() {
        return size; // Zwracanie przechowywanego rozmiaru listy
    }

    // Pobieranie wartości w węźle o podanym indeksie
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) { // Sprawdzenie, czy indeks jest prawidłowy
            throw new IndexOutOfBoundsException("Indeks: " + index + ", Rozmiar: " + size);
        }
        Node<T> current = head; // Inicjalizacja wskaźnika na początek listy
        for (int i = 0; i < index; i++) { // Przesuwanie wskaźnika na odpowiedni węzeł
            current = current.next;
        }
        return current.value; // Zwracanie wartości w odpowiednim węźle
    }

    // Dodawanie elementu na koniec listy (z użyciem metody add)
    public void addLast(T value) {
        add(value); // Użycie metody add do dodania elementu na koniec listy
    }

    // Pobieranie ostatniego elementu listy
    public T getLast() {
        if (tail == null) { // Jeśli lista jest pusta
            throw new IllegalStateException("Lista jest pusta"); // Rzucenie wyjątku
        }
        return tail.value; // Zwracanie wartości ostatniego węzła
    }

    // Dodawanie elementu na początek listy
    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value, head); // Tworzenie nowego węzła z wartością
        if (head == null) { // Jeśli lista jest pusta
            tail = newNode; // Nowy węzeł staje się końcem listy
        }
        head = newNode; // Nowy węzeł staje się początkiem listy
        size++; // Zwiększenie rozmiaru listy
    }

    // Pobieranie pierwszego elementu listy
    public T getFirst() {
        if (head == null) { // Jeśli lista jest pusta
            throw new IllegalStateException("Lista jest pusta"); // Rzucenie wyjątku
        }
        return head.value; // Zwracanie wartości pierwszego węzła
    }

    // Usuwanie pierwszego elementu listy
    public T removeFirst() {
        if (head == null) { // Jeśli lista jest pusta
            throw new IllegalStateException("Lista jest pusta"); // Rzucenie wyjątku
        }
        T value = head.value; // Pobranie wartości pierwszego węzła
        head = head.next; // Ustawienie głowy na następny węzeł
        if (head == null) { // Jeśli lista staje się pusta
            tail = null; // Ustawienie tail na null
        }
        size--; // Zmniejszenie rozmiaru listy
        return value; // Zwracanie wartości usuniętego węzła
    }

    // Usuwanie ostatniego elementu listy
    public T removeLast() {
        if (tail == null) { // Jeśli lista jest pusta
            throw new IllegalStateException("Lista jest pusta"); // Rzucenie wyjątku
        }

        //tylko jeden element na liście
        if (head == tail) {
            T value = tail.value;
            head = tail = null;
            size--;
            return value;
        }

        //więcej niż jeden element na liście
        Node<T> current = head;
        while (current.next != tail){
            current = current.next;
        }

        T value = tail.value;
        tail = current;
        tail.next = null;
        size--;
        return value;
    }

    // Zwracanie iteratora do listy
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null; // Sprawdzenie, czy są jeszcze elementy
            }

            @Override
            public T next() {
                if (current == null) { // Sprawdzenie, czy są jeszcze elementy
                    throw new NoSuchElementException(); // Rzucenie wyjątku, jeśli brak elementów
                }
                T value = current.value; // Pobranie wartości obecnego węzła
                current = current.next; // Przesunięcie wskaźnika do następnego węzła
                return value; // Zwracanie wartości
            }
        };
    }

    // Zwracanie strumienia z zawartością listy
    @Override
    public Stream<T> stream() {
        // Użycie StreamSupport do stworzenia strumienia z iteratora
        // StreamSupport.stream() tworzy strumień na podstawie spliteratora.
        return StreamSupport.stream(
                // Spliterators.spliteratorUnknownSize() tworzy spliterator, który opiera się na iteratorze.
                // 'iterator()' zwraca iterator przechodzący przez elementy listy.
                // Drugi argument (0) oznacza brak specjalnych atrybutów dotyczących spliteratora.
                // 'Spliterator.ORDERED' jest wartością domyślną oznaczającą, że elementy są dostarczane w określonej kolejności.
                Spliterators.spliteratorUnknownSize(
                        iterator(),  // Metoda 'iterator()' zwraca iterator, który przechodzi przez elementy listy.
                        0            // 0 oznacza brak specjalnych atrybutów dla spliteratora.
                ),
                // Flaga 'false' oznacza, że strumień będzie sekwencyjny, a nie równoległy.
                // Równoległe strumienie przetwarzają elementy w kilku wątkach, podczas gdy sekwencyjne przetwarzają je jeden po drugim.
                false
        );
    }

    // Statyczna metoda szablonowa filtrująca obiekty z listy na podstawie klasy
    public static <T> List<T> filterByClass(List<T> list, Class<?> clazz) {
        return list.stream()
                .filter(clazz::isInstance) // Filtracja obiektów należących do wskazanej klasy lub jej podklas
                .collect(Collectors.toList()); // Zbieranie wyników do nowej listy
    }

    // Predykat sprawdzający, czy wartość znajduje się w otwartym przedziale
    public static <T extends Comparable<T>> Predicate<T> isWithinRange(T lowerBound, T upperBound) {
        return value -> value.compareTo(lowerBound) > 0 && value.compareTo(upperBound) < 0;
    }

    // Statyczna metoda, która zlicza elementy listy spełniające warunek predykatu
    public static <T extends Comparable<T>> long countElementsInRange(CustomList<T> list, T lowerBound, T upperBound) {
        return list.stream()
                .filter(isWithinRange(lowerBound, upperBound))
                .count();
    }
}
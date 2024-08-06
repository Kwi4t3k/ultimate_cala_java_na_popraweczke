package org.example;

public class CustomList_zad1<T> {
    // Wskaźniki na początek (head) i koniec (tail) listy
    private Node<T> head;
    private Node<T> tail;

    // Wewnętrzna klasa węzła, przechowująca wartość i wskaźnik na następny węzeł
    private class Node<T> {
        T value;
        Node<T> next;

        // Konstruktor węzła
        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    // Konstruktor inicjalizujący pustą listę
    public CustomList_zad1() {
        this.head = null; // Początkowo wskaźnik na początek listy jest pusty
        this.tail = null; // Początkowo wskaźnik na koniec listy jest pusty
    }

    // Dodawanie elementu na koniec listy
    public void addLast(T value) {
        Node<T> newNode = new Node<>(value, null); // Tworzenie nowego węzła z wartością
        if (tail == null) { // Jeśli lista jest pusta
            head = newNode; // Nowy węzeł staje się początkiem listy
            tail = newNode; // Nowy węzeł staje się końcem listy
        } else {
            tail.next = newNode; // Ustawienie wskaźnika next obecnego końca na nowy węzeł
            tail = newNode; // Nowy węzeł staje się nowym końcem listy
        }
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
    }

    // Pobieranie pierwszego elementu listy
    public T getFirst() {
        if (head == null) { // Jeśli lista jest pusta
            throw new IllegalStateException("Lista jest pusta"); // Rzucenie wyjątku
        }
        return head.value; // Zwracanie wartości pierwszego węzła
    }

    // Usuwanie i zwracanie pierwszego elementu listy
    public T removeFirst() {
        if (head == null) { // Jeśli lista jest pusta
            throw new IllegalStateException("Lista jest pusta"); // Rzucenie wyjątku
        }
        T value = head.value; // Zapisywanie wartości pierwszego węzła
        head = head.next; // Przesuwanie wskaźnika head na następny węzeł
        if (head == null) { // Jeśli lista staje się pusta
            tail = null; // Ustawienie wskaźnika tail na null
        }
        return value; // Zwracanie wartości usuniętego węzła
    }

    // Usuwanie i zwracanie ostatniego elementu listy
    public T removeLast() {
        if (tail == null) { // Jeśli lista jest pusta
            throw new IllegalStateException("Lista jest pusta"); // Rzucenie wyjątku
        }

        // Jeśli lista zawiera tylko jeden węzeł
        if (head == tail) {
            T value = tail.value; // Zapisywanie wartości jedynego węzła
            head = tail = null; // Ustawienie wskaźników head i tail na null
            return value; // Zwracanie wartości usuniętego węzła
        }

        // Jeśli lista zawiera więcej niż jeden węzeł
        Node<T> current = head; // Ustawienie wskaźnika current na początek listy
        while (current.next != tail) { // Przesuwanie wskaźnika current do przedostatniego węzła
            current = current.next;
        }

        T value = tail.value; // Zapisywanie wartości ostatniego węzła
        tail = current; // Ustawienie wskaźnika tail na przedostatni węzeł
        tail.next = null; // Usunięcie wskaźnika next obecnego końca listy
        return value; // Zwracanie wartości usuniętego węzła
    }
}

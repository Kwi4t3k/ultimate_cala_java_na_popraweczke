package org.example.Person;

public class Person {
    public String imię, nazwisko;

    @Override
    public String toString() {
        return "Person{" +
                "imię='" + imię + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                '}';
    }

    public Person(String imię, String nazwisko) {
        this.imię = imię;
        this.nazwisko = nazwisko;
    }
}

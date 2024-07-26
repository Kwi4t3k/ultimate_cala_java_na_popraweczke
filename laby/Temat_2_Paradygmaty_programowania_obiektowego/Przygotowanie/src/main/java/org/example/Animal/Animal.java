package org.example.Animal;

// Aby uzyskać dostęp do metod interfejsu, interfejs musi zostać „zaimplementowany” (coś w rodzaju odziedziczenia) przez inną klasę ze implements słowem kluczowym (zamiast extends).

//public interface Animal {
//    public String sound(); // interface method (does not have a body)
//}

//abstract class Animal{
//    protected String imię;
//
//    public Animal(String imię) {
//        this.imię = imię;
//    }
//}

abstract class Animal{
    private String imię;

    protected String getImię() {
        return imię;
    }

    public Animal(String imię) {
        this.imię = imię;
    }
}
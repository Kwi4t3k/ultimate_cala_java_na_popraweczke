package org.example.Animal;

public class Dog extends Animal {
    public Dog(String imię) {
        super(imię);
    }

    public String sound() {
        return getImię() + " szczeka: hau hau";
    }
}

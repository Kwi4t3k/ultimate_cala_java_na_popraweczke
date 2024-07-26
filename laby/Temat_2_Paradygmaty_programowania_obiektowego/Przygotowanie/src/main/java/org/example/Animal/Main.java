package org.example.Animal;

public class Main {
    public static void main(String[] args) {
//        Cat cat = new Cat();
//        System.out.println(cat.sound() + "\n");
//
//        Dog dog = new Dog();
//        System.out.println(dog.sound() + "\n");

        Cat cat = new Cat("Mruczek");
        System.out.println(cat.sound() + "\n");

        Dog dog = new Dog("Burek");
        System.out.println(dog.sound() + "\n");
    }
}


package org.example.Animal;

//public class Cat implements Animal{
//
//    @Override
//    public String sound() {
//        return "Miau miau";
//    }
//}

//public class Cat extends Animal {
//
//    public Cat(String imię) {
//        super(imię);
//    }
//
//    public String sound() {
//        return imię + " miauczy: miau miau";
//    }
//}

public class Cat extends Animal {

    public Cat(String imię) {
        super(imię);
    }

    public String sound() {
        return getImię() + " miauczy: miau miau";
    }
}
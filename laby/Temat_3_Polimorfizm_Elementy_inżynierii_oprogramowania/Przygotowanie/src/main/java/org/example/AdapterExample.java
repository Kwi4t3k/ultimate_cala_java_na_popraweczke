package org.example;

//1. Adapter
//Cel: Adapter wzorzec umożliwia współpracę obiektów z niezgodnymi interfejsami poprzez "owinięcie" jednego interfejsu drugim.
//
//Przykład:
//
//Załóżmy, że mamy starą bibliotekę OldLibrary z metodą oldMethod(), ale chcemy użyć nowej klasy NewLibrary z metodą newMethod(). Użyjemy adaptera, aby zintegrować starą bibliotekę.

    // Stara biblioteka
    class OldLibrary {
        public void oldMethod() {
            System.out.println("Old method called");
        }
    }

    // Nowa biblioteka
    interface NewLibrary {
        void newMethod();
    }

    // Adapter
    class Adapter implements NewLibrary {
        private OldLibrary oldLibrary;

        public Adapter(OldLibrary oldLibrary) {
            this.oldLibrary = oldLibrary;
        }

        @Override
        public void newMethod() {
            oldLibrary.oldMethod();
        }
    }

    // Klient
    public class AdapterExample {
        public static void main(String[] args) {
            OldLibrary oldLibrary = new OldLibrary();
            NewLibrary newLibrary = new Adapter(oldLibrary);
            newLibrary.newMethod(); // Wywoła oldMethod()
        }
    }
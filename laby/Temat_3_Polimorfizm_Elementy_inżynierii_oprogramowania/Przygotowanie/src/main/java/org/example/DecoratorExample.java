package org.example;

//3. Decorator (Dekorator)
//Cel: Wzorzec dekoratora pozwala na dynamiczne dodawanie nowych funkcjonalności do obiektów bez modyfikowania ich klasy.
//
//Przykład:
//
//Załóżmy, że mamy klasę Coffee, do której chcemy dynamicznie dodawać dodatki, takie jak mleko czy cukier.

    // Podstawowy interfejs
    interface Coffee {
        String getDescription();
        double getCost();
    }

    // Podstawowa implementacja
    class SimpleCoffee implements Coffee {
        @Override
        public String getDescription() {
            return "Simple coffee";
        }

        @Override
        public double getCost() {
            return 1.0;
        }
    }

    // Dekorator bazowy
    abstract class CoffeeDecorator implements Coffee {
        protected Coffee decoratedCoffee;

        public CoffeeDecorator(Coffee coffee) {
            this.decoratedCoffee = coffee;
        }

        @Override
        public String getDescription() {
            return decoratedCoffee.getDescription();
        }

        @Override
        public double getCost() {
            return decoratedCoffee.getCost();
        }
    }

    // Konkretne dekoratory
    class MilkDecorator extends CoffeeDecorator {
        public MilkDecorator(Coffee coffee) {
            super(coffee);
        }

        @Override
        public String getDescription() {
            return decoratedCoffee.getDescription() + ", milk";
        }

        @Override
        public double getCost() {
            return decoratedCoffee.getCost() + 0.5;
        }
    }

    class SugarDecorator extends CoffeeDecorator {
        public SugarDecorator(Coffee coffee) {
            super(coffee);
        }

        @Override
        public String getDescription() {
            return decoratedCoffee.getDescription() + ", sugar";
        }

        @Override
        public double getCost() {
            return decoratedCoffee.getCost() + 0.2;
        }
    }

    // Klient
    public class DecoratorExample {
        public static void main(String[] args) {
            Coffee coffee = new SimpleCoffee();
            System.out.println(coffee.getDescription() + " $" + coffee.getCost());

            coffee = new MilkDecorator(coffee);
            System.out.println(coffee.getDescription() + " $" + coffee.getCost());

            coffee = new SugarDecorator(coffee);
            System.out.println(coffee.getDescription() + " $" + coffee.getCost());
        }
    }
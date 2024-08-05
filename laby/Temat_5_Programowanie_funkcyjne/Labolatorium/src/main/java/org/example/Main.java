package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //zad1
//        try {
//            // Przykładowe ustawienie ścieżki do pliku jar PlantUML
//            PlantUMLRunner.setplantUMLJarPath("src/main/resources/plantuml.jar");
//
//            // Przykładowe dane UML
//            String umlData = "@startuml\nAlice -> Bob: test\n@enduml";
//
//            // Generowanie diagramu
//            PlantUMLRunner.generateDiagram(umlData, "src/main/resources", "diagram.png");
//
//            System.out.println("Diagram został wygenerowany pomyślnie.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        //zad2
//        Person parent1 = new Person("Jan Kowalski", LocalDate.of(1950, 1, 1), null);
//        Person parent2 = new Person("Anna Kowalska", LocalDate.of(1955, 2, 2), null);
//        Person child = new Person("Piotr Kowalski", LocalDate.of(1980, 3, 3), null);
//
//        child.getParents().add(parent1);
//        child.getParents().add(parent2);
//
//        // Generowanie diagramu UML
//        String plantUMLContent = child.toPlantUML();
//        System.out.println(plantUMLContent);

        //zad4
        Person parent1 = new Person("Anna Dąbrowska", LocalDate.parse("1930-02-07"), LocalDate.parse("1991-12-22"));
        Person parent2 = new Person("Andrzej Kowalski", LocalDate.parse("1936-09-12"), LocalDate.parse("1990-06-25"));
        Person child = new Person("Alicja Wiśniewska", LocalDate.of(1963, 10, 18), LocalDate.parse("2012-10-18"));
        Person child2 = new Person("Tomasz Dąbrowski",LocalDate.of(1996, 1, 24));
        Person child3 = new Person("Elżbieta Kowalska",LocalDate.of(1990, 12, 28));

        List<Person> personList = new ArrayList<>();
        personList.add(parent1);
        personList.add(parent2);
        personList.add(child);
        personList.add(child2);

        System.out.println("Zadanie 4 \n" + Person.personListWithSubstring(personList, "Dąb") + "\n");

        //zad 5
        System.out.println("Zadanie 5\n" + Person.listSortByYear(personList));

        List<Person> personList2 = new ArrayList<>();
        personList2.add(parent1);
        personList2.add(parent2);
        personList2.add(child);
        personList2.add(child2);
        personList2.add(child3);

        //zad 6
        System.out.println("\nZadanie 6\n" + Person.DeadListSortByLife(personList2));

        //zad 7
        System.out.println("\nZadanie 7\n" + Person.oldestPersonAlive(personList2));
    }
}
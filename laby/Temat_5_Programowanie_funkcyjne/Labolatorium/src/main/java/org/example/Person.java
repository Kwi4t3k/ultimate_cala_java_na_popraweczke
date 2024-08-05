package org.example;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Person {
    private String fullName;
    private LocalDate birthDate, deathDate;
    private List<Person> parents = new ArrayList<>();

    public List<Person> getParents() {
        return parents;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public Person(String fullName, LocalDate birthDate, LocalDate deathDate) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    public Person(String fullName, LocalDate birthDate) {
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public static Person fromCsvLine(String line) throws NegativeLifespanException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String[] parts = line.split(",");

        String name = parts[0];

        LocalDate birth = LocalDate.parse(parts[1], formatter);

        LocalDate death = null;
        if(!parts[2].isEmpty()){
            death = LocalDate.parse(parts[2], formatter);

            if(death.isBefore(birth)) {
                throw new NegativeLifespanException("Data śmierci nie może być przed urodzeniem");
            }
        }

        return new Person(name, birth, death);
    }

    //stare fromCsv

//    public static List<Person> fromCsv(Path path) throws AmbiguousPersonException {
//
//        List<Person> personList = new ArrayList<>();
//
//        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
//            String line;
//            br.readLine();
//
//            while ((line = br.readLine()) != null){
//                Person person = Person.fromCsvLine(line);
//
//                for (Person p : personList){
//                    if(p.fullName.equals(person.fullName)){
//                        throw new AmbiguousPersonException("jest więcej niż jedna osoba o tym imieniu i nazwisku");
//                    }
//                }
//                personList.add(person);
//            }
//
//        } catch (IOException | NegativeLifespanException e) {
//            throw new RuntimeException(e);
//        }
//
//        return personList;
//    }

    //do zad 5 i 6

    public static List<Person> fromCsv(Path path) throws AmbiguousPersonException, IOException, NegativeLifespanException {
        List<Person> personList = new ArrayList<>();
        Map<String, Person> personMap = new HashMap<>();

        // Pierwsze przejście: tworzenie obiektów Person
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            br.readLine(); // Pomijamy wiersz nagłówkowy

            // Odczyt każdej linii i tworzenie obiektu Person
            while ((line = br.readLine()) != null) {
                Person person = Person.fromCsvLine(line);

                // Sprawdzenie, czy osoba już istnieje
                if (personMap.containsKey(person.fullName)) {
                    throw new AmbiguousPersonException("Osoba o imieniu i nazwisku " + person.fullName + " występuje więcej niż raz.");
                }

                // Dodanie osoby do listy i mapy
                personList.add(person);
                personMap.put(person.fullName, person);
            }
        }

        // Drugie przejście: aktualizacja odniesień do rodziców
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            br.readLine(); // Pomijamy wiersz nagłówkowy ponownie

            // Odczyt każdej linii, aby zaktualizować odniesienia do rodziców
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Person child = personMap.get(parts[0]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate childBirthDate = LocalDate.parse(parts[1], formatter);

                // Iteracja przez potencjalnych rodziców
                for (int i = 3; i < parts.length; i++) {
                    if (!parts[i].isEmpty()) {
                        Person parent = personMap.get(parts[i]);
                        if (parent != null) {
                            try {
                                // Sprawdzenie, czy rodzic jest młodszy niż 15 lat w momencie narodzin dziecka
                                if (parent.birthDate != null && parent.birthDate.plusYears(15).isAfter(childBirthDate)) {
                                    throw new ParentingAgeException("Rodzic " + parent.fullName + " jest młodszy niż 15 lat w chwili narodzin dziecka " + child.fullName);
                                }

                                // Sprawdzenie, czy rodzic nie żyje w momencie narodzin dziecka
                                if (parent.deathDate != null && parent.deathDate.isBefore(childBirthDate)) {
                                    throw new ParentingAgeException("Rodzic " + parent.fullName + " nie żyje w chwili narodzin dziecka " + child.fullName);
                                }

                                // Dodanie rodzica do listy rodziców dziecka
                                child.parents.add(parent);
                            } catch (ParentingAgeException e) {
                                // Obsługa wyjątku poprzez zapytanie użytkownika o potwierdzenie
                                Scanner scanner = new Scanner(System.in);
                                System.out.println(e.getMessage() + ". Czy chcesz dodać tę relację mimo to? (Y/N)");
                                String response = scanner.nextLine();
                                if (response.equalsIgnoreCase("Y")) {
                                    child.parents.add(parent);
                                }
                            }
                        }
                    }
                }
            }
        }
        return personList;
    }

//    @Override
//    public String toString() {
//        String parentNames = parents.isEmpty() ? "brak" : String.join(", ", parents.stream().map(p -> p.fullName).toList());
//        return "imię i nazwisko: " + fullName + ", data urodzenia: " + birthDate + ", data śmierci: " + deathDate +
//                ", rodzice: " + parentNames;
//    }

//    Zadanie 7.
//    W klasie Person napisz statyczne metody toBinaryFile i fromBinaryFile, które zapiszą i odczytają listę osób do i z pliku binarnego.

    // Statyczna metoda toBinaryFile: zapisuje listę osób do pliku binarnego
    public static void toBinaryFile(Path csvPath, Path path) throws IOException, NegativeLifespanException, AmbiguousPersonException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(path.toFile()))) {
            List<Person> personList = fromCsv(csvPath);
            dataOutputStream.writeInt(personList.size()); // Zapis rozmiaru listy

            for (Person person : personList) {
                dataOutputStream.writeUTF(person.fullName); // Zapis imienia
                dataOutputStream.writeUTF(person.birthDate.toString()); // Zapis daty urodzenia
                dataOutputStream.writeUTF(person.deathDate != null ? person.deathDate.toString() : ""); // Zapis daty śmierci (pusty ciąg, jeśli null)

                dataOutputStream.writeInt(person.parents.size()); // Zapis liczby rodziców
                for (Person parent : person.parents) {
                    dataOutputStream.writeUTF(parent.fullName); // Zapis imienia rodzica
                }
            }
        }
        System.out.println("Zapis udany");
    }

    // Statyczna metoda fromBinaryFile: odczytuje listę osób z pliku binarnego
    public static List<Person> fromBinaryFile(Path binaryFile) throws IOException {
        List<Person> personList = new ArrayList<>();
        Map<String, Person> personMap = new HashMap<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(binaryFile.toFile()))) {
            int size = dis.readInt(); // Odczyt rozmiaru listy

            for (int i = 0; i < size; i++) {
                String fullName = dis.readUTF(); // Odczyt pełnego imienia i nazwiska
                LocalDate birthDate = LocalDate.parse(dis.readUTF()); // Odczyt daty urodzenia

                // Odczyt daty śmierci
                String deathDateStr = dis.readUTF();

                LocalDate deathDate = deathDateStr.isEmpty() ? null : LocalDate.parse(deathDateStr);

                Person person = new Person(fullName, birthDate, deathDate);
                personList.add(person);
                personMap.put(person.fullName, person);
            }

            // Aktualizacja odniesień do rodziców
            for (Person person : personList) {
                int parentCount = dis.readInt(); // Odczyt liczby rodziców
                for (int j = 0; j < parentCount; j++) {
                    String parentName = dis.readUTF(); // Odczyt pełnego imienia i nazwiska rodzica
                    Person parent = personMap.get(parentName);

                    if (parent != null) {
                        person.parents.add(parent);
                    }
                }
            }
        }
        return personList;
    }

    public String toPlantUML(){
        StringBuilder sb = new StringBuilder();

        // Definiowanie obiektu dziecka
        sb.append("@startuml\n");
        sb.append("object \"" + fullName + "\n as " + formatName(fullName) + "\n");

        // Definiowanie obiektów rodziców i relacji między dzieckiem a rodzicami
        for (Person parent : parents){
            sb.append("object \"" + parent.fullName + "\n as " + formatName(parent.fullName) + "\n");
            sb.append(formatName(fullName) + " --> " + formatName(parent.fullName) + "\n");
        }

        sb.append("@enduml");

        return sb.toString();
    }

    // Pomocnicza metoda do formatowania nazwy obiektu (usuwanie spacji itp.)
    private String formatName(String name) {
        return name.replaceAll("\\s+", "_");
    }

    @Override
    public String toString() {
        String parentNames = parents.isEmpty() ? "brak" : parents.stream()
                .map(p -> p.fullName)
                .collect(Collectors.joining(", "));
        return "imię i nazwisko: " + fullName + ", data urodzenia: " + birthDate + ", data śmierci: " + deathDate +
                ", rodzice: " + parentNames;
    }

//    Zadanie 4.
//
//W klasie Person napisz statyczną metodę, która przyjmie listę osób oraz napis substring. Metoda powinna zwrócić listę osób z listy wejściowej, ograniczoną do osób, których nazwa zawiera substring.

    public static List<Person> personListWithSubstring(List<Person> personList, String substring) {
        List<Person> personListModify = new ArrayList<>();

        for (Person person : personList){
            if (person.fullName.contains(substring)){
                personListModify.add(person);
            }
        }
        return personListModify;
    }

    //stream
//    public static List<Person> personListWithSubstring(List<Person> personList, String substring){
//        return personList.stream()
//                .filter(person -> person.fullName.contains(substring))
//                .collect(Collectors.toList());
//    }



//    Zadanie 5.
//
//W klasie Person napisz statyczną metodę, która przyjmie listę osób. Metoda powinna zwrócić listę osób z listy wejściowej, posortowanych według roku urodzenia.

    public static List<Person> listSortByYear (List<Person> personList){

        //sortowanie po dacie urodzenia
//        personList.sort(Comparator.comparing(Person::getBirthDate));

        //sortowanie po roku urodzenia
        personList.sort(Comparator.comparingInt(person -> person.getBirthDate().getYear()));

        return personList;
    }

//    Zadanie 6.
//
//W klasie Person napisz statyczną metodę, która przyjmie listę osób. Metoda powinna zwrócić listę zmarłych osób z listy wejściowej, posortowanych malejąco według długości życia.

    public static List<Person> DeadListSortByLife(List<Person> personList) {
        return personList.stream()
                .filter(person -> person.getDeathDate() != null)
//                .sorted(Comparator.comparingInt(person -> person.getDeathDate().getYear() - person.getBirthDate().getYear()).reversed()) //idk czemu reversed nie działa a powinno XDD
                .sorted((p1, p2) -> {
                    int lifeSpan1 = p1.getDeathDate().getYear() - p1.getBirthDate().getYear();
                    int lifeSpan2 = p2.getDeathDate().getYear() - p2.getBirthDate().getYear();
                    return Integer.compare(lifeSpan2, lifeSpan1); // Sortowanie malejące
                })
                .collect(Collectors.toList());
    }

//    Zadanie 7.
//
//    W klasie Person napisz statyczną metodę, która przyjmie listę osób. Metoda powinna zwrócić najstarszą żyjącą osobę.

    public static List<Person> oldestPersonAlive (List<Person> personList) {
        return personList.stream()
                .filter(person -> person.getDeathDate() == null)
                .sorted((p1, p2) -> {
                    int lifeSpan1 = LocalDate.now().getYear() - p1.getBirthDate().getYear();
                    int lifeSpan2 = LocalDate.now().getYear() - p2.getBirthDate().getYear();
                    return Integer.compare(lifeSpan2, lifeSpan1); // Sortowanie malejące
                })
                .limit(1)
                .collect(Collectors.toList());
    }
}
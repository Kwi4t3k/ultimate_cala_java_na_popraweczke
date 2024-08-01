package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private String fullName;
    private LocalDate birthDate, deathDate;
    private List<Person> parents = new ArrayList<>();

    public Person(String fullName, LocalDate birthDate, LocalDate deathDate) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
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

    public static List<Person> fromCsv(Path path) throws AmbiguousPersonException {

        List<Person> personList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null){
                Person person = Person.fromCsvLine(line);

                for (Person p : personList){
                    if(p.fullName.equals(person.fullName)){
                        throw new AmbiguousPersonException("jest więcej niż jedna osoba o tym imieniu i nazwisku");
                    }
                }

                personList.add(person);
            }

        } catch (IOException | NegativeLifespanException e) {
            throw new RuntimeException(e);
        }

        return personList;
    }

    @Override
    public String toString() {
        return "imię i nazwisko: " + fullName + ", data urodzenia: " + birthDate + ", data śmierci: " + deathDate;
    }
}

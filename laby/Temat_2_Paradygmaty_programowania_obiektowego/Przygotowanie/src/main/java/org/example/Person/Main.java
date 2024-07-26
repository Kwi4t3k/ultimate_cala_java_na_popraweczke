package org.example.Person;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Person person_test = new Person("Adam", "Nowak");
        System.out.println(person_test.toString() + "\n");

        Teacher teacher = new Teacher("Jakub", "Nowak");
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Anna", "Kowalska"));
        studentList.add(new Student("Michał", "Wiśniewski"));

        Subject subject = new Subject("Polski", teacher, studentList);
//        System.out.println(subject);


        Subject subject1 = (Subject) subject.clone();

        System.out.println("oryginał " + subject);
        System.out.println("kopia " + subject1);

        studentList.add(new Student("Aaa", "bbbb"));

        System.out.println("oryginał edit " + subject);
        System.out.println("kopia edit " + subject1);
    }
}
package org.example.Person;

import java.util.ArrayList;
import java.util.List;

public class Subject implements Cloneable{
    public String tytuł;
    public Teacher teacher;
    public List<Student> studentList;

    public Subject(String tytuł, Teacher teacher, List<Student> studentList) {
        this.tytuł = tytuł;
        this.teacher = teacher;
        this.studentList = studentList;
    }

    @Override
    public String toString() {
        String result = "";
        for (Student student : studentList) {
            result += "Prowadzący_imię: " + teacher.imię + ", Prowadzący_nazwisko: " + teacher.nazwisko + " ,Student_imię: " + student.imię + " ,Student_nazwisko: " + student.nazwisko + "\n";
        }
        return result;
    }

    //Zaimplementuj metodę clone() w klasie Subject z pierwszego zadania tak, aby wykonywała głęboką kopię obiektu.

//    @Override
//    protected Subject clone() throws CloneNotSupportedException{
//        Subject s1 = new Subject(tytuł, teacher, studentList);
//        Subject s2 = s1.clone();
//        return s2;
//    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Teacher clonedTeacher = new Teacher(this.teacher.imię, this.teacher.nazwisko);
        List<Student> clonedStudentList = new ArrayList<>();
        for (Student student : this.studentList) {
            clonedStudentList.add(new Student(student.imię, student.nazwisko));
        }
        return new Subject(this.tytuł, clonedTeacher, clonedStudentList);
    }

}
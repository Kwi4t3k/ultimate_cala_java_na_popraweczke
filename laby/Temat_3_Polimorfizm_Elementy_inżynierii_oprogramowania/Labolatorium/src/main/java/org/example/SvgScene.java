package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SvgScene {
//    Zmodyfikuj klasę SvgScene, aby posiadała tablicę obiektów klasy Shape i korzystając z polimorfizmu zapisz w niej obiekty typu Polygon i Ellipse.
    private List<Shape> shapeList = new ArrayList<>();

    public void add(Shape shape){
        shapeList.add(shape);
    }


//    Napisz funkcję save(String), która utworzy plik HTML w ścieżce danej argumentem i zapisze do niego reprezentacje wszystkich wielokątów znajdujących się na kanwie.


    public void save(String path) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));

        bufferedWriter.write("<svg height=\"500\" width=\"500\" xmlns=\"http://www.w3.org/2000/svg\">\n");

        for (Shape shape : shapeList){
            bufferedWriter.write(shape.toSvg("") + "\n");
        }

        bufferedWriter.write("</svg>");
        bufferedWriter.close();
    }
}
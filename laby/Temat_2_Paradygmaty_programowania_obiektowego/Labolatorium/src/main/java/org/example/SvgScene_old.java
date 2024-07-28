package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SvgScene_old {
    private List<Polygon> polygonList = new ArrayList<>();

    public void addPolygon(Polygon polygon){
        polygonList.add(polygon);
    }


//    Napisz funkcję save(String), która utworzy plik HTML w ścieżce danej argumentem i zapisze do niego reprezentacje wszystkich wielokątów znajdujących się na kanwie.


    public void save(String path) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));

        bufferedWriter.write("<svg height=\"500\" width=\"500\" xmlns=\"http://www.w3.org/2000/svg\">\n");

        for (Polygon polygon : polygonList){
            bufferedWriter.write(polygon.toSvg() + "\n");
        }

        bufferedWriter.write("</svg>");
        bufferedWriter.close();
    }
}

package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SvgScene {
    private List<Polygon> polygonList = new ArrayList<>();

    public void addPolygon(Polygon polygon){
        polygonList.add(polygon);
    }


//    Napisz funkcję save(String), która utworzy plik HTML w ścieżce danej argumentem i zapisze do niego reprezentacje wszystkich wielokątów znajdujących się na kanwie.


    public void save(String path) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));

        for (Polygon polygon : polygonList){
            bufferedWriter.write(polygon.toSvg());
        }

        bufferedWriter.close();
    }
}

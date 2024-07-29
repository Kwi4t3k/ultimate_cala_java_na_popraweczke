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

        bufferedWriter.write("<defs>");

//        for (String string : defs){
//            bufferedWriter.write(defs.toString() + "\n");
//        }

        for (String def : defs){
            bufferedWriter.write(def + "\n");
        }

        bufferedWriter.write("</defs>\n");

        for (Shape shape : shapeList){
            bufferedWriter.write(shape.toSvg("") + "\n");
        }

        bufferedWriter.write("</svg>");
        bufferedWriter.close();
    }

//    W klasie SvgScene utwórz prywatne, statyczne pole SvgScene instance, początkowo równe null. Napisz akcesor do tego pola. Jeżeli znajduje się tam null, należy je zainicjalizować.

    private static SvgScene instance = null;

    public static SvgScene getInstance() {
        if(instance == null){
            instance = new SvgScene();
        }
        return instance;
    }

//    Dodaj do klasy SvgScene tablicę String defs[] oraz metodę dodającą elementy do tej tablicy, wzorując się na tablicy shapes i metodzie addShape. W metodzie saveHtml uwzględnij dopisanie tagów <defs> do wynikowego pliku.
//
//Zdefiniuj klasę DropShadowDecorator dziedziczącą po ShapeDecorator. Jej zadaniem jest udekorowanie obiektu Shape rzucanym cieniem. Jest to realizowane przez umieszczenie w tagu <defs> sformatowanego kodu:
//
//\t<filter id=\"f%d\" x=\"-100%%\" y=\"-100%%\" width=\"300%%\" height=\"300%%\">\n" +
//"\t\t<feOffset result=\"offOut\" in=\"SourceAlpha\" dx=\"5\" dy=\"5\" />\n" +
//"\t\t<feGaussianBlur result=\"blurOut\" in=\"offOut\" stdDeviation=\"5\" />\n" +
//"\t\t<feBlend in=\"SourceGraphic\" in2=\"blurOut\" mode=\"normal\" />\n" +
//"\t</filter>", index
//
//oraz w metodzie toSvg:
//
//"filter=\"url(#f%d)\" ", index
//
//gdzie w obu przypadkach index jest liczbą całkowitą, unikalną dla tego filtra. Unikalność indeksu zagwarantuj przy użyciu prywatnego, statycznego pola klasy.

//    public String[] defs;
    List<String> defs = new ArrayList<>();

//    public void add(String elem){
//        for (int i = 0; i < defs.length; i++) {
//            defs[i] += elem;
//        }
//    }

    public void addDef(String def){
        defs.add(def);
    }
}
package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Style style = new Style("blue", "red", 4.0);
        System.out.println(style.toSvg());

//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/test.txt"));
//        bufferedWriter.write("dadda");
//        bufferedWriter.close();

        Point point1 = new Point(10, 40);
        Point point2 = new Point(100, 200);
        Point point3 = new Point(80, 30);

        List<Point> pointList = new ArrayList<>();
        pointList.add(point1);
        pointList.add(point2);
        pointList.add(point3);

        Polygon polygon = new Polygon(pointList, new Style("blue", "red", 2.0));
        Polygon polygon1 = new Polygon(pointList, new Style("red", "green", 6.0));

        SvgScene scene = new SvgScene();
        scene.addPolygon(polygon);
        scene.addPolygon(polygon1);
//        scene.save("src/main/resources/test.html");


        Point squareA = new Point(0, 0);
        Point squareD = new Point(100, 100);

        Polygon square = Polygon.square(new Line(squareA, squareD), style);
        scene.addPolygon(square);

        System.out.println("\n" + square.toSvg());

        scene.save("src/main/resources/test.html");
    }
}
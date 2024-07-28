package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Style style = new Style("blue", "red", 4.0);
        System.out.println(style.toSvg());

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
        scene.add(polygon);
        scene.add(polygon1);

        Point squareA = new Point(0, 0);
        Point squareD = new Point(100, 100);

        Polygon square = Polygon.square(new Line(squareA, squareD), style);
        scene.add(square);

        System.out.println("\n" + square.toSvg() + "\n");


        Ellipse ellipse = new Ellipse(new Point(120, 80), 100, 50, style);
        System.out.println(ellipse.toSvg());

        scene.add(ellipse);

        scene.save("src/main/resources/test.html");
    }
}

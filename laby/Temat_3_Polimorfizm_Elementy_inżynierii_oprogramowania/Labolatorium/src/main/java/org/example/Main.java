package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
//        Style style = new Style("blue", "red", 4.0);
//        System.out.println(style.toSvg());
//
//        Vec2 vec21 = new Vec2(10, 40);
//        Vec2 vec22 = new Vec2(100, 200);
//        Vec2 vec23 = new Vec2(80, 30);
//
//        List<Vec2> vec2List = new ArrayList<>();
//        vec2List.add(vec21);
//        vec2List.add(vec22);
//        vec2List.add(vec23);
//
//        Polygon polygon = new Polygon(vec2List, new Style("blue", "red", 2.0));
//        Polygon polygon1 = new Polygon(vec2List, new Style("red", "blue", 6.0));
//
//        SvgScene scene = new SvgScene();
////        scene.add(polygon);
////        scene.add(polygon1);
//
//        Vec2 squareA = new Vec2(0, 0);
//        Vec2 squareD = new Vec2(100, 100);
//
//        Polygon square = Polygon.square(new Line(squareA, squareD), style);
////        scene.add(square);
//
//        System.out.println("\n" + square.toSvg("") + "\n");
//
//
//        Ellipse ellipse = new Ellipse(new Vec2(120, 80), 100, 50, style);
//        System.out.println(ellipse.toSvg(""));
//
////        scene.add(ellipse);
//
//        SolidFilledPolygon solidFilledPolygon = new SolidFilledPolygon(vec2List, "yellow", style);
//        System.out.println(solidFilledPolygon.toSvg(""));
//
////        scene.add(solidFilledPolygon);
//
//
//        SolidFillShapeDecorator solidFillShapeDecorator1 = new SolidFillShapeDecorator(polygon1, "green");
//        StrokeShapeDecorator strokeShapeDecorator1 = new StrokeShapeDecorator(solidFillShapeDecorator1, "blue", 5);
//        System.out.println("\n" + solidFillShapeDecorator1.toSvg(""));
//        System.out.println(strokeShapeDecorator1.toSvg("") + "\n");
//
//        TransformationDecorator transformationDecorator = new TransformationDecorator.Builder(polygon)
//                .translateSet(new Vec2(50, 50))
//                .rotateSet(new Vec2(100, 100), 45)
//                .scaleSet(new Vec2(1.5, 1.5))
//                .build();
//
//        System.out.println("transformation decorator: " + transformationDecorator.toSvg("") + "\n");
//
//        scene.add(solidFillShapeDecorator1);
//        scene.add(strokeShapeDecorator1);
//
//        SolidFillShapeDecorator solidFillShapeDecorator2 = new SolidFillShapeDecorator(ellipse, "red");
//        StrokeShapeDecorator strokeShapeDecorator2 = new StrokeShapeDecorator(solidFillShapeDecorator2, "green", 15);
//        System.out.println("\n" + solidFillShapeDecorator2.toSvg(""));
//        System.out.println(strokeShapeDecorator2.toSvg("") + "\n");
//        scene.add(solidFillShapeDecorator2);
//        scene.add(strokeShapeDecorator2);
//
//
//        scene.save("src/main/resources/test.html");

//            /\
//            |
//        do zadań do 4

        SvgScene scene = SvgScene.getInstance();

        Style style = new Style("blue", "red", 4.0);
        Vec2 vec21 = new Vec2(10, 40);
        Vec2 vec22 = new Vec2(100, 200);
        Vec2 vec23 = new Vec2(80, 30);

        List<Vec2> vec2List = new ArrayList<>();
        vec2List.add(vec21);
        vec2List.add(vec22);
        vec2List.add(vec23);

        Shape ellipse = new Ellipse(new Vec2(120, 80), 100, 50, style);
        Shape polygon = new Polygon(vec2List, new Style("blue", "red", 2.0));

        Shape strokeEllipse = new StrokeShapeDecorator(ellipse, "blue", 4.0);
        Shape dropShadowPolygon = new DropShadowDecorator(polygon);

        scene.add(strokeEllipse);
        scene.add(dropShadowPolygon);

        scene.save("src/main/resources/output.svg");
    }
}

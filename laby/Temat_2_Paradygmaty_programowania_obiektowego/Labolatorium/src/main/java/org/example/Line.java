package org.example;

import java.util.Locale;

public class Line {
    public Point point1, point2;

    public Line(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public String toSvg(){
        return String.format(Locale.ENGLISH, "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" style=\"stroke:red;stroke-width:2\" />", point1.x, point1.y, point2.x, point2.y);
    }

    public double length(Point point1, Point point2){
        return Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2));
    }

//    Przeciąż metodę klasy Line zwracającą prostopadły odcinek tak, aby przyjmowała jako dodatkowy argument długość zwracanego odcinka.

    public static Line createPerpendicularSegments(Line line, Point point) {
        double dx = line.point2.x - line.point1.x;
        double dy = line.point2.y - line.point1.y;

        double length = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        double unitX = dx / length;
        double unitY = dy / length;

        // Wektory prostopadłe do (dx, dy) to (-dy, dx)
        double perpX1 = -unitY * length;
        double perpY1 = unitX * length;

        Point newPoint1 = new Point(point.x + perpX1, point.y + perpY1);

        Line perpendicularSegment1 = new Line(point, newPoint1);

        return perpendicularSegment1;
    }

    public Line createPerpendicularLine(Point point, double length) {
        double dx = point2.x - point1.x;
        double dy = point2.y - point1.y;

        double unitX = dx / Math.sqrt(dx * dx + dy * dy);
        double unitY = dy / Math.sqrt(dx * dx + dy * dy);

        // Wektor prostopadły do (dx, dy) to (-dy, dx)
        double perpX = -unitY * length;
        double perpY = unitX * length;

        Point newPoint = new Point(point.x + perpX, point.y + perpY);

        return new Line(point, newPoint);
    }
}
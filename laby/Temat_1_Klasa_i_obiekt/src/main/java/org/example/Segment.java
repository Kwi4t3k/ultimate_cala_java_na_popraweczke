package org.example;

import java.util.Locale;

public class Segment {
    //reprezentacja odcinka
    private Point point1, point2;

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public double length(Point point1, Point point2){
        return Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2));
    }

    public Segment(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public String toSvg(){
        String line = "<svg height=\"200\" width=\"300\" xmlns=\"http://www.w3.org/2000/svg\">\n";
        line += String.format(Locale.ENGLISH, "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" style=\"stroke:red;stroke-width:2\" />", point1.x, point1.y, point2.x, point2.y);
        line += "\n</svg>";

        return line;
    }
    // Statyczna metoda tworząca dwa prostopadłe segmenty
    public static Segment[] createPerpendicularSegments(Segment segment, Point point) {
        double dx = segment.getPoint2().x - segment.getPoint1().x;
        double dy = segment.getPoint2().y - segment.getPoint1().y;
        double length = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        double unitX = dx / length;
        double unitY = dy / length;

        // Wektory prostopadłe do (dx, dy) to (-dy, dx) i (dy, -dx)
        double perpX1 = -unitY * length;
        double perpY1 = unitX * length;
        double perpX2 = unitY * length;
        double perpY2 = -unitX * length;

        Point newPoint1 = new Point(point.x + perpX1, point.y + perpY1);
        Point newPoint2 = new Point(point.x + perpX2, point.y + perpY2);

        Segment perpendicularSegment1 = new Segment(point, newPoint1);
        Segment perpendicularSegment2 = new Segment(point, newPoint2);

        return new Segment[]{perpendicularSegment1, perpendicularSegment2};
    }
}

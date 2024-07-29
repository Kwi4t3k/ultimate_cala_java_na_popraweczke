package org.example;

import java.util.Locale;

public class Line {
    public Vec2 vec21, vec22;

    public Line(Vec2 vec21, Vec2 vec22) {
        this.vec21 = vec21;
        this.vec22 = vec22;
    }

    public String toSvg(){
        return String.format(Locale.ENGLISH, "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" style=\"stroke:red;stroke-width:2\" />", vec21.x, vec21.y, vec22.x, vec22.y);
    }

    public double length(Vec2 vec21, Vec2 vec22){
        return Math.sqrt(Math.pow(vec22.x - vec21.x, 2) + Math.pow(vec22.y - vec21.y, 2));
    }

//    Przeciąż metodę klasy Line zwracającą prostopadły odcinek tak, aby przyjmowała jako dodatkowy argument długość zwracanego odcinka.

    public static Line createPerpendicularSegments(Line line, Vec2 vec2) {
        double dx = line.vec22.x - line.vec21.x;
        double dy = line.vec22.y - line.vec21.y;

        double length = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        double unitX = dx / length;
        double unitY = dy / length;

        // Wektory prostopadłe do (dx, dy) to (-dy, dx)
        double perpX1 = -unitY * length;
        double perpY1 = unitX * length;

        Vec2 newVec21 = new Vec2(vec2.x + perpX1, vec2.y + perpY1);

        Line perpendicularSegment1 = new Line(vec2, newVec21);

        return perpendicularSegment1;
    }

    public Line createPerpendicularLine(Vec2 vec2, double length) {
        double dx = vec22.x - vec21.x;
        double dy = vec22.y - vec21.y;

        double unitX = dx / Math.sqrt(dx * dx + dy * dy);
        double unitY = dy / Math.sqrt(dx * dx + dy * dy);

        // Wektor prostopadły do (dx, dy) to (-dy, dx)
        double perpX = -unitY * length;
        double perpY = unitX * length;

        Vec2 newVec2 = new Vec2(vec2.x + perpX, vec2.y + perpY);

        return new Line(vec2, newVec2);
    }
}
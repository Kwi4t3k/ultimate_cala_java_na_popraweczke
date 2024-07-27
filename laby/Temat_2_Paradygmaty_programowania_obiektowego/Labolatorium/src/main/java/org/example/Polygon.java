package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//Zdefiniuj klasę Polygon posiadającą prywatną tablicę punktów. Konstruktor tej klasy powinien przyjmować tablicę punktów. Napisz publiczną metodę toSvg() działającą analogicznie jak w poprzednim zadaniu.
public class Polygon {
    private List<Point> pointList = new ArrayList<>();
    public Style style;

    public Polygon(List<Point> pointList, Style style) {
        this.pointList = pointList;
        this.style = style;
    }

    public Polygon(List<Point> pointList) {
        this.pointList = pointList;
        this.style = new Style("transparent", "black", 1.0);
    }

    public String toSvg(){
        String line = "<svg height=\"220\" width=\"500\" xmlns=\"http://www.w3.org/2000/svg\">\n";
        line += "<polygon points=\"";

        for (Point point : pointList){
            line += String.format(Locale.ENGLISH, "%f, %f " , point.x, point.y);
        }

        line += String.format(Locale.ENGLISH, "\"style=\"fill:%s;stroke:%s;stroke-width:%f\" />", style.fillColor, style.strokeColor, style.strokeWidth);
        line += "\n</svg>";

        return line;
    }


    // W klasie Polygon napisz konstruktor kopiujący, wykonujący głęboką kopię obiektu.

    public Polygon(Polygon polygon){
        this.pointList = new ArrayList<>();
        for (Point point : polygon.pointList){
            this.pointList.add(new Point(point.x, point.y));
        }
    }
}

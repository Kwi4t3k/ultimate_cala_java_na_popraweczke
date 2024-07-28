package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//Zdefiniuj klasę Polygon posiadającą prywatną tablicę punktów. Konstruktor tej klasy powinien przyjmować tablicę punktów. Napisz publiczną metodę toSvg() działającą analogicznie jak w poprzednim zadaniu.
public class Polygon_old {
    private List<Point> pointList = new ArrayList<>();
    public Style style;

    public Polygon_old(List<Point> pointList, Style style) {
        this.pointList = pointList;
        this.style = style;
    }

    public Polygon_old(List<Point> pointList) {
        this.pointList = pointList;
        this.style = new Style("transparent", "black", 1.0);
    }

    public String toSvg(){
        String line = "<svg height=\"500\" width=\"500\" xmlns=\"http://www.w3.org/2000/svg\">\n";
        line += "<polygon points=\"";

        for (Point point : pointList){
            line += String.format(Locale.ENGLISH, "%f, %f " , point.x, point.y);
        }

        line += String.format(Locale.ENGLISH, "\"style=\"fill:%s;stroke:%s;stroke-width:%f\" />", style.fillColor, style.strokeColor, style.strokeWidth);
        line += "\n</svg>";

        return line;
    }


    // W klasie Polygon napisz konstruktor kopiujący, wykonujący głęboką kopię obiektu.

    public Polygon_old(Polygon_old polygon){
        this.pointList = new ArrayList<>();
        for (Point point : polygon.pointList){
            this.pointList.add(new Point(point.x, point.y));
        }
    }

    // Napisz publiczną, statyczną metodę wytwórczą klasy Polygon o nazwie square. Funkcja powinna przyjąć jako argumenty: obiekt Line, obiekt Style i zwrócić wielokąt będący kwadratem, którego przekątną jest dany odcinek.

    public static Polygon_old square(Line odcinek, Style style){
        double ax = odcinek.point1.x;
        double ay = odcinek.point1.y;
        double dx = odcinek.point2.x;
        double dy = odcinek.point2.y;

        Point a = new Point(ax, ay);
        Point d = new Point(dx, dy);

        double dlugosc_przekatnej = odcinek.length(a, d);
//        System.out.println("\n" + dlugosc_przekatnej);

        double bok_kwadratu = (dlugosc_przekatnej / Math.sqrt(2));
//        System.out.println("\n" + bok_kwadratu);

        double bx = ax + bok_kwadratu;
//        double bx = ax + 100;
        double by = ay;
        Point b = new Point(bx, by);

        double cx = ax;
        double cy = ay + bok_kwadratu;
//        double cy = ay + 100;
        Point c = new Point(cx, cy);

        //do listy potrzeba 4 punktów na rogach kwadratu
        List<Point> pointList1 = new ArrayList<>();
        pointList1.add(a);
        pointList1.add(b);
        pointList1.add(d);
        pointList1.add(c);

//        a ------------ b
//        |              |
//        |              |
//        |              |
//        |              |
//        c--------------d

        //svg do prostokąta
//        <svg width="300" height="130" xmlns="http://www.w3.org/2000/svg">
//            <rect width="200" height="100" x="10" y="10" rx="20" ry="20" fill="blue" />
//        </svg>

        return new Polygon_old(pointList1, style);
    }
}

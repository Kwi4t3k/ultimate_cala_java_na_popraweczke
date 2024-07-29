package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//Zdefiniuj klasę Polygon posiadającą prywatną tablicę punktów. Konstruktor tej klasy powinien przyjmować tablicę punktów. Napisz publiczną metodę toSvg() działającą analogicznie jak w poprzednim zadaniu.
public class Polygon implements Shape {
    private List<Vec2> vec2List = new ArrayList<>();
    private Style style;

    public Polygon(List<Vec2> vec2List, Style style) {
        this.vec2List = vec2List;
        this.style = style;
    }

    public Polygon(List<Vec2> vec2List) {
        this.vec2List = vec2List;
        this.style = new Style("transparent", "black", 1.0);
    }

    @Override
    public String toSvg(String param) {
        StringBuilder points = new StringBuilder();

        for (Vec2 vec2 : vec2List) {
            points.append(String.format(Locale.ENGLISH, "%f,%f ", vec2.x, vec2.y));
        }

        return String.format(Locale.ENGLISH, "<polygon points=\"%s\" %s style=\"%s\" />", points.toString().trim(), param, style.toSvg());
    }


    // W klasie Polygon napisz konstruktor kopiujący, wykonujący głęboką kopię obiektu.

    public Polygon(Polygon polygon){
        this.style = new Style(polygon.style);
        this.vec2List = new ArrayList<>();
        for (Vec2 vec2 : polygon.vec2List){
            this.vec2List.add(new Vec2(vec2.x, vec2.y));
        }
    }

    // Napisz publiczną, statyczną metodę wytwórczą klasy Polygon o nazwie square. Funkcja powinna przyjąć jako argumenty: obiekt Line, obiekt Style i zwrócić wielokąt będący kwadratem, którego przekątną jest dany odcinek.

    public static Polygon square(Line odcinek, Style style){
        double ax = odcinek.vec21.x;
        double ay = odcinek.vec21.y;
        double dx = odcinek.vec22.x;
        double dy = odcinek.vec22.y;

        Vec2 a = new Vec2(ax, ay);
        Vec2 d = new Vec2(dx, dy);

        double dlugosc_przekatnej = odcinek.length(a, d);
//        System.out.println("\n" + dlugosc_przekatnej);

        double bok_kwadratu = (dlugosc_przekatnej / Math.sqrt(2));
//        System.out.println("\n" + bok_kwadratu);

        double bx = ax + bok_kwadratu;
//        double bx = ax + 100;
        double by = ay;
        Vec2 b = new Vec2(bx, by);

        double cx = ax;
        double cy = ay + bok_kwadratu;
//        double cy = ay + 100;
        Vec2 c = new Vec2(cx, cy);

        //do listy potrzeba 4 punktów na rogach kwadratu
        List<Vec2> vec2List1 = new ArrayList<>();
        vec2List1.add(a);
        vec2List1.add(b);
        vec2List1.add(d);
        vec2List1.add(c);

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

        return new Polygon(vec2List1, style);
    }
}

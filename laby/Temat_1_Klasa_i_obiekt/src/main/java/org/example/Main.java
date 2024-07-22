package org.example;

public class Main {
    public static void main(String[] args) {
        Point point1 = new Point(10, 20);
        Point point2 = new Point(50, 60);

        Segment segment = new Segment(point1, point2);

        double length = segment.length(point1, point2);
        System.out.println("długość: " + length + "\n");

  /*      //Napisz funkcję (metodę klasy głównej), która przyjmie: obiekt segment klasy Segment oraz obiekt point klasy Point. Funkcja powinna zwrócić odcinek prostopadły do segment, rozpoczynający się w punkcie point o długości równej odcinkowi segment.

        // Współrzędne wektora segmentu
        double dx = point2.x - point1.x;
        double dy = point2.y - point1.y;

        // Normalizacja wektora i obrót o 90 stopni, aby uzyskać wektor prostopadły
        double unitX = dx / length; // Normalizuje wektor w osi x.
        double unitY = dy / length; // Normalizuje wektor w osi y.

        // Wektor prostopadły do (dx, dy) to (-dy, dx) lub (dy, -dx)
        double perpX1  = -unitY * length; // Oblicza współrzędną x wektora prostopadłego.
        double perpY1  = unitX * length; // Oblicza współrzędną y wektora prostopadłego.

        double perpX2 = unitY * length;
        double perpY2 = -unitX * length;

        // Tworzenie punktu początkowego nowego segmentu
        Point point = new Point(30, 40);

        // Obliczanie współrzędnych końcowego punktu nowego segmentu
        Point newPoint1 = new Point(point.x + perpX1, point.y + perpY1);
        Point newPoint2 = new Point(point.x + perpX2, point.y + perpY2);

        // Tworzenie nowego segmentu prostopadłego
        Segment perpendicularSegment1 = new Segment(point, newPoint1);
        Segment perpendicularSegment2 = new Segment(point, newPoint2);

        // Wyświetlanie segmentu w formacie SVG

        //wersja wcześniejsza
//        System.out.println("Oryginalny segment: " + segment.toSvg());
//        System.out.println("Prostopadły segment: " + perpendicularSegment.toSvg());

        System.out.println("Oryginalny segment: " + segment.toSvg());
        System.out.println("Prostopadły segment 1: " + perpendicularSegment1.toSvg());
        System.out.println("Prostopadły segment 2: " + perpendicularSegment2.toSvg());

        // Wywołanie metody zwracającej tablicę dwóch prostopadłych segmentów
        Segment[] perpendicularSegments = createPerpendicularSegments(segment, point);
        System.out.println("Prostopadłe segmenty z metody:");
        for (Segment seg : perpendicularSegments) {
            System.out.println(seg.toSvg());
        }
    }
    public static Segment[] createPerpendicularSegments(Segment segment, Point point) {
        double dx = segment.getPoint2().x - segment.getPoint1().x;
        double dy = segment.getPoint2().y - segment.getPoint1().y;
        double length = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        double unitX = dx / length;
        double unitY = dy / length;

        double perpX1 = -unitY * length;
        double perpY1 = unitX * length;
        double perpX2 = unitY * length;
        double perpY2 = -unitX * length;

        Point newPoint1 = new Point(point.x + perpX1, point.y + perpY1);
        Point newPoint2 = new Point(point.x + perpX2, point.y + perpY2);

        Segment perpendicularSegment1 = new Segment(point, newPoint1);
        Segment perpendicularSegment2 = new Segment(point, newPoint2);

        return new Segment[]{perpendicularSegment1, perpendicularSegment2}; */


        //przenieś tę metodę jako statyczną do klasy Segment. Szczególne przypadki należy zignorować.

       // Tworzenie punktu początkowego nowego segmentu
    Point point = new Point(30, 40);

    // Wywołanie metody zwracającej tablicę dwóch prostopadłych segmentów
    Segment[] perpendicularSegments = Segment.createPerpendicularSegments(segment, point);

    // Wyświetlanie segmentów w formacie SVG
        System.out.println("Oryginalny segment: " + segment.toSvg());
        System.out.println("Prostopadłe segmenty z metody:");
        for (Segment seg : perpendicularSegments) {
            System.out.println(seg.toSvg());
        }
    }
}
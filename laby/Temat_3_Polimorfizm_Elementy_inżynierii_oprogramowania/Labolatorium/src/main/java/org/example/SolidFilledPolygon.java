package org.example;

import java.util.List;
import java.util.Locale;

public class SolidFilledPolygon extends Polygon{

    private String color;

    public SolidFilledPolygon(List<Vec2> vec2List, String color, Style style) {
        super(vec2List, style);
        this.color = color;
    }

    public SolidFilledPolygon(List<Vec2> vec2List) {
        super(vec2List);
    }

    public SolidFilledPolygon(Polygon polygon) {
        super(polygon);
    }

    @Override
    public String toSvg(String param) {
        String newParam = String.format(Locale.ENGLISH, "fill=\"%s\" %s", color, param);
        return super.toSvg(newParam);
    }

//    SolidFilledPolygon nadpisuje metodę toSvg z klasy Polygon.
//Tworzony jest nowy parametr newParam, który zawiera atrybut fill ustawiony na wartość koloru oraz istniejące parametry SVG.
//Wywoływana jest metoda super.toSvg(newParam), aby wykorzystać logikę klasy bazowej z nowymi parametrami.
}
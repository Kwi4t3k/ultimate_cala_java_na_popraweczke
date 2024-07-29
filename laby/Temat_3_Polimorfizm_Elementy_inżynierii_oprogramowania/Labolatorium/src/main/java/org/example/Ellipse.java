package org.example;

import java.util.Locale;

public class Ellipse implements Shape {
    private Vec2 center;
    private double rx, ry;
    private Style style;

    public Ellipse(Vec2 center, double rx, double ry, Style style) {
        this.center = center;
        this.rx = rx;
        this.ry = ry;
        this.style = style;
    }

//    @Override
//    public String toSvg(String param) {
//        return String.format(Locale.ENGLISH,"<ellipse rx=\"%f\" ry=\"%f\" cx=\"%f\" cy=\"%f\" %s/>", rx, ry, center.x, center.y, param);
//    }
    @Override
    public String toSvg(String param) {
        return String.format(Locale.ENGLISH, "<ellipse rx=\"%f\" ry=\"%f\" cx=\"%f\" cy=\"%f\" %s style=\"%s\"/>", rx, ry, center.x, center.y, param, style.toSvg());
    }
}

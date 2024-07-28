package org.example;

import java.util.Locale;

public class Ellipse extends Shape{
    private Point center;
    private double rx, ry;
    private Style style;

    public Ellipse(Point center, double rx, double ry, Style style) {
        super(style);
        this.center = center;
        this.rx = rx;
        this.ry = ry;
        this.style = style;
    }

    @Override
    public String toSvg() {
        return String.format(Locale.ENGLISH,"<ellipse rx=\"%f\" ry=\"%f\" cx=\"%f\" cy=\"%f\" style=\"%s\" />", rx, ry, center.x, center.y, style.toSvg());
    }
}

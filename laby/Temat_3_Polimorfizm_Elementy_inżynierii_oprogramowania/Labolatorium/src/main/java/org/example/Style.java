package org.example;

import java.util.Locale;

public class Style {
    public final String fillColor, strokeColor;
    public final double strokeWidth;

    public Style(String fillColor, String strokeColor, Double strokeWidth) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }

    public Style(Style other) {
        this.fillColor = other.fillColor;
        this.strokeColor = other.strokeColor;
        this.strokeWidth = other.strokeWidth;
    }

    public String toSvg(){
        String result = String.format(Locale.ENGLISH,"fill:%s;stroke:%s;stroke-width:%f", fillColor, strokeColor, strokeWidth);
        return result;
    }
}

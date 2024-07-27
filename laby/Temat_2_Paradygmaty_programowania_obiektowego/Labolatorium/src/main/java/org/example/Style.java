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

    public String toSvg(){
        String result = "<svg height=\"220\" width=\"500\" xmlns=\"http://www.w3.org/2000/svg\">\n";
        result += String.format(Locale.ENGLISH,"<polygon points=\"100,10 150,190 50,190\" style=\"fill:%s;stroke:%s;stroke-width:%f\" />\n", fillColor, strokeColor, strokeWidth);
        result += "</svg>";
        return result;
    }
}

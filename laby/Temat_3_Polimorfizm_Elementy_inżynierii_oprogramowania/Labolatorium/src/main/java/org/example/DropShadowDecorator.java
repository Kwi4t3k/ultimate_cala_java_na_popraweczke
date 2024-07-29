package org.example;

import java.util.Locale;

public class DropShadowDecorator extends ShapeDecorator {
    private static int uniqueIndex = 0;
    private int index;
    public DropShadowDecorator(Shape decoratedShape) {
        super(decoratedShape);
        this.index = uniqueIndex++;
        addShadowDef();
    }

    public void addShadowDef(){
        String def = String.format("\t<filter id=\"f%d\" x=\"-100%%\" y=\"-100%%\" width=\"300%%\" height=\"300%%\">\n" +
                "\t\t<feOffset result=\"offOut\" in=\"SourceAlpha\" dx=\"5\" dy=\"5\" />\n" +
                "\t\t<feGaussianBlur result=\"blurOut\" in=\"offOut\" stdDeviation=\"5\" />\n" +
                "\t\t<feBlend in=\"SourceGraphic\" in2=\"blurOut\" mode=\"normal\" />\n" +
                "\t</filter>", index);
        SvgScene.getInstance().addDef(def);
    }

    @Override
    public String toSvg(String param) {
        String newParam = String.format(Locale.ENGLISH, "filter=\"url(#f%d)\" ", index) + param;
        return decoratedShape.toSvg(newParam);
    }
}

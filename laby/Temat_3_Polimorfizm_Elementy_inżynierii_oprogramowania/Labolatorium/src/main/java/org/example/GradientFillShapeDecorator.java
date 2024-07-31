package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GradientFillShapeDecorator extends ShapeDecorator {
    private List<Double> offsets;
    private List<String> colors;
    private static int gradientIndex = 0;
    private int currentGradientIndex;

    public GradientFillShapeDecorator(Shape decoratedShape, List<Double> offsets, List<String> colors) {
        super(decoratedShape);
        this.offsets = offsets;
        this.colors = colors;
        this.currentGradientIndex = gradientIndex++;
        addGradientDef();
    }

    private void addGradientDef() {
        StringBuilder result = new StringBuilder();
        result.append(String.format(Locale.ENGLISH, "\t<linearGradient id=\"g%d\" >\n", currentGradientIndex));
        for (int i = 0; i < offsets.size(); i++) {
            result.append(String.format(Locale.ENGLISH, "\t\t<stop offset=\"%f\" style=\"stop-color:%s\" />\n", offsets.get(i), colors.get(i)));
        }
        result.append("\t</linearGradient>");
        SvgScene.getInstance().addDef(result.toString());
    }

    @Override
    public String toSvg(String param) {
        String newParam = String.format(Locale.ENGLISH, "fill=\"url(#g%d)\" ", currentGradientIndex) + param;
        return decoratedShape.toSvg(newParam);
    }

    public static class Builder {
        private Shape shape;
        private List<Double> offsets = new ArrayList<>();
        private List<String> colors = new ArrayList<>();

        public Builder addShape(Shape shape) {
            this.shape = shape;
            return this;
        }
        public Builder addOffset(double offset){
            offsets.add(offset);
            return this;
        }

        public Builder addColors(String color){
            colors.add(color);
            return this;
        }

        public GradientFillShapeDecorator build(){
            return new GradientFillShapeDecorator(shape, offsets, colors);
        }
    }
}

package org.example;

import java.util.Locale;

public class TransformationDecorator extends ShapeDecorator{

    private boolean translate, rotate, scale;
    private double rotateAngle;
    private Vec2 translateVector, rotateCenter, scaleVector;

    public TransformationDecorator(Shape decoratedShape, boolean translate, boolean rotate, boolean scale, double rotateAngle, Vec2 translateVector, Vec2 rotateCenter, Vec2 scaleVector) {
        super(decoratedShape);
        this.translate = translate;
        this.rotate = rotate;
        this.scale = scale;
        this.rotateAngle = rotateAngle;
        this.translateVector = translateVector;
        this.rotateCenter = rotateCenter;
        this.scaleVector = scaleVector;
    }

    public static class Builder {
        private Shape shape;
        private boolean translate = false, rotate = false, scale = false;
        private double rotateAngle;
        private Vec2 translateVector, rotateCenter, scaleVector;

        public Builder(Shape shape) {
            this.shape = shape;
        }

        public Builder translateSet(Vec2 translateVector){
            this.translateVector = translateVector;
            translate = true;
            return this;
        }

        public Builder rotateSet(Vec2 rotateCenter, double rotateAngle){
            this.rotateCenter = rotateCenter;
            rotate = true;
            return this;
        }

        public Builder scaleSet(Vec2 scaleVector){
            this.scaleVector = scaleVector;
            scale = true;
            return this;
        }

        public TransformationDecorator build() {
            return new TransformationDecorator(shape, translate, rotate, scale, rotateAngle, translateVector, rotateCenter, scaleVector);
        }
    }

    @Override
    public String toSvg(String param) {
        StringBuilder result = new StringBuilder();

        result.append(String.format("translate(%f %f) ", translateVector.x, translateVector.y));
        result.append(String.format("rotate(%f %f %f) ", rotateAngle, rotateCenter.x, rotateCenter.y));
        result.append(String.format("scale(%f %f) ", scaleVector.x, scaleVector.y));

        String newParam = String.format(Locale.ENGLISH, "transform=\"%s\" %s", result, param);
        return super.toSvg(newParam);
    }
}

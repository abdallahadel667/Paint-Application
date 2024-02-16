package com.oop.assignment3;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Circle extends Shape {

    private int radius;


    public Circle(String stroke, String strokeWidth, boolean draggable, String key, String scaleX, String scaleY,
                  String rotation, String fill, Position position, String opacity, int radius) {
        super(stroke, strokeWidth, draggable, key, scaleX, scaleY, rotation, fill, position, opacity);
        this.radius = radius;
    }



    public Circle(){}


    public int getRadius() {
        return radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Shape copy(){
        return new Circle(this.getStroke(),this.getStrokeWidth(),this.isDraggable(),this.getKey(),this.getScaleX(),
                this.getScaleY(),this.getRotation(),this.getFill(),this.getPosition(),this.getOpacity(),this.getRadius());
    }
}

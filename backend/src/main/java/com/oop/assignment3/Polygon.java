package com.oop.assignment3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Polygon extends Shape {

    private int radius;

    private  int numberOfSides;

    public Polygon(String stroke, String strokeWidth, boolean draggable, String key, String scaleX, String scaleY
            , String rotation, String fill, Position position, String opacity, int radius, int numberOfSides) {
        super(stroke, strokeWidth, draggable, key, scaleX, scaleY, rotation, fill, position, opacity);
        this.radius = radius;
        this.numberOfSides = numberOfSides;
    }

    public Polygon() {
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setNumberOfSides(int numberOfSides) {
        this.numberOfSides = numberOfSides;
    }

    public int getNumberOfSides() {
        return numberOfSides;
    }

    public Shape copy(){
        return new Polygon(this.getStroke(),this.getStrokeWidth(),this.isDraggable(),this.getKey(),this.getScaleX()
                ,this.getScaleY(),this.getRotation(),this.getFill(),this.getPosition(),this.getOpacity(),this.radius,this.numberOfSides);
    }


}

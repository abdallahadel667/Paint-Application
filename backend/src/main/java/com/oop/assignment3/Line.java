package com.oop.assignment3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Line extends Element {
    private int[] points;

    @Autowired
    public Line(String stroke, String strokeWidth,
                boolean draggable, String key, String scaleX, String scaleY, String rotation, int[] points) {
        super( stroke, strokeWidth, draggable, key,scaleX,scaleY,rotation);
        this.points = points;
    }

    public int[] getPoints() {
        return points;
    }
    public void setPoints(int[] points) {
        this.points = points;
    }
    public Element copy(){
        return new Line(this.getStroke(),this.getStrokeWidth(),this.isDraggable(),this.getKey(),
                getScaleX(),this.getScaleY(),this.getRotation(), this.getPoints());
    }
}

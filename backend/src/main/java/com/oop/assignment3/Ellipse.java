package com.oop.assignment3;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Ellipse extends Shape {
    private int width;
    private int height;

    @Autowired
    public Ellipse(String fill, Position position, String opacity, String stroke,
                   String strokeWidth, boolean draggable, String key,String scaleX,String scaleY,String rotation, int width, int height) {
        super( stroke, strokeWidth, draggable, key,scaleX,scaleY,rotation,fill, position, opacity);
        this.width = width;
        this.height = height;
    }


    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }


    public Shape copy(){
        return new Ellipse(this.getFill(),this.getPosition(),this.getOpacity(),this.getStroke(),
                this.getStrokeWidth(),this.isDraggable(),this.getKey(),this.getScaleX(),this.getScaleY()
                ,this.getRotation(),this.width,this.height);
    }
}

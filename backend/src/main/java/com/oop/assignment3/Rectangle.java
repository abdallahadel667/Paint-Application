package com.oop.assignment3;

import org.springframework.beans.factory.annotation.Autowired;

public class Rectangle extends Shape{
    private int height;
    private int width;
@Autowired
    public Rectangle(String stroke, String strokeWidth, boolean draggable, String key, String scaleX,String scaleY,
                     String rotation,String fill, Position position, String opacity, int height, int width) {
        super(stroke, strokeWidth, draggable, key, scaleX,scaleY,rotation,fill, position, opacity);
        this.height = height;
        this.width = width;
    }



    public Element copy(){
    return new Rectangle(this.getStroke(),this.getStrokeWidth(),this.isDraggable(),this.getKey(),
            this.getScaleX(),this.getScaleY(),this.getRotation(),this.getFill(),
            this.getPosition(),this.getOpacity(),this.height,this.width);
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
}

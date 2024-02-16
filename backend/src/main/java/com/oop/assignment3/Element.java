package com.oop.assignment3;

public class Element {
    private String stroke;
    private String strokeWidth;
    private boolean draggable;
    private String key;
    private  String scaleX;
    private String scaleY;
    private String rotation;


    public Element(String stroke, String strokeWidth, boolean draggable,
                   String key, String scaleX, String scaleY, String rotation) {
        this.stroke = stroke;
        this.strokeWidth = strokeWidth;
        this.draggable = draggable;
        this.key = key;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
    }

    public String getScaleX() {
        return scaleX;
    }

    public void setScaleX(String scaleX) {
        this.scaleX = scaleX;
    }

    public String getScaleY() {
        return scaleY;
    }

    public void setScaleY(String scaleY) {
        this.scaleY = scaleY;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public  Element copy(){
        return new Element();

    }
    public  Element(){}

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public String getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(String strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

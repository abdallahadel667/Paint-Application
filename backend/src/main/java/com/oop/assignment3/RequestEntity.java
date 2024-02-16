package com.oop.assignment3;

import org.springframework.beans.factory.annotation.Autowired;

public class RequestEntity {

    private String type;
    private int height;
    private int width;
    private int radius;
    private int numberOfSides;
    private String fill;
    private Position position;
    private String opacity;
    private String stroke;
    private String strokeWidth;
    private boolean draggable;
    private String key;
    private  String scaleX;
    private String scaleY;
    private String rotation;
    private int[] points;

    public RequestEntity() {
    }

    @Autowired
    public RequestEntity(String type, int height, int width, int radius, int numberOfSides, String fill,
                         Position position, String opacity, String stroke, String strokeWidth, boolean draggable,
                         String key, int[] points,String scaleX,String scaleY) {
        this.type = type;
        this.height = height;
        this.width = width;
        this.radius = radius;
        this.numberOfSides = numberOfSides;
        this.fill = fill;
        this.position = position;
        this.opacity = opacity;
        this.stroke = stroke;
        this.strokeWidth = strokeWidth;
        this.draggable = draggable;
        this.key = key;
        this.points = points;
        this.scaleX=scaleX;
        this.scaleY=scaleY;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
    public int getRadius() {
        return radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
    public int getNumberOfSides() {
        return numberOfSides;
    }
    public void setNumberOfSides(int numberOfSides) {
        this.numberOfSides = numberOfSides;
    }
    public String getFill() {
        return fill;
    }
    public void setFill(String fill) {
        this.fill = fill;
    }
    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
    public String getOpacity() {
        return opacity;
    }
    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }
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
    public int[] getPoints() {
        return points;
    }
    public void setPoints(int[] points) {
        this.points = points;
    }
}

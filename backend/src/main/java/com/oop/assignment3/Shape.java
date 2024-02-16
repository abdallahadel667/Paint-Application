package com.oop.assignment3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Shape extends Element{

    private String fill;
    private Position position;
    private String opacity;


    @Autowired
    public Shape(String stroke, String strokeWidth, boolean draggable, String key, String scaleX, String scaleY,
                 String rotation, String fill, Position position, String opacity) {
        super(stroke, strokeWidth, draggable, key, scaleX, scaleY, rotation);
        this.fill = fill;
        this.position = position;
        this.opacity = opacity;
    }



    public Shape(){}




    public String getFill() {
        return fill;
    }
    public void setPosition (Position position) {
        this.position = position;
    }
    public void setFill(String fill) {
    this.fill = fill;
    }
    public Position getPosition() {
        return position;
    }
    public String getOpacity() {
        return opacity;
    }
    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }


}

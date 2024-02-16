package com.oop.assignment3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

//Factory
@Service
public class ShapeService {


    Element shape;

    @Autowired
    public ShapeService(Element shape) {
        this.shape = shape;
    }

    public ShapeService() {
    }

    //Factory
    public Element draw(RequestEntity entity) {
        String type = entity.getType();

        if (type.equalsIgnoreCase("circle"))
            shape = new Circle(entity.getStroke(), entity.getStrokeWidth(), entity.isDraggable(), entity.getKey()
                    , entity.getScaleX(), entity.getScaleY(), entity.getRotation(), entity.getFill(), entity.getPosition(),
                    entity.getOpacity(), entity.getRadius());

        else if (type.equalsIgnoreCase("Ellipse"))
            shape = new Ellipse((entity.getFill()), entity.getPosition(), entity.getOpacity(), entity.getStroke()
                    , entity.getStrokeWidth(), entity.isDraggable(), entity.getKey(), entity.getScaleX(), entity.getScaleY()
                    , entity.getRotation(), entity.getWidth(), entity.getHeight());

        else if (type.equalsIgnoreCase("Polygon"))
            shape = new Polygon(entity.getStroke(), entity.getStrokeWidth(), entity.isDraggable(), entity.getKey(),
                    entity.getScaleX(), entity.getScaleY(), entity.getRotation(), entity.getFill(),
                    entity.getPosition(), entity.getOpacity(), entity.getRadius(), entity.getNumberOfSides());

        else if (type.equalsIgnoreCase("Rectangle"))
            shape = new Rectangle(entity.getStroke(), entity.getStrokeWidth(), entity.isDraggable(), entity.getKey()
                    , entity.getScaleX(), entity.getScaleY(), entity.getRotation(), entity.getFill(), entity.getPosition(),
                    entity.getOpacity(), entity.getHeight(), entity.getWidth());

        else if (type.equalsIgnoreCase("line")) {
            shape = new Line(entity.getStroke(), entity.getStrokeWidth(), entity.isDraggable(), entity.getKey(), entity.getScaleX(),
                    entity.getScaleY(), entity.getRotation(), entity.getPoints());
        }
        return shape;
    }

    public HashMap<String, RequestEntity> transformToRequestEntity(HashMap<String, Element> map) {
        HashMap<String, RequestEntity> Response = new HashMap<>();
        for (Map.Entry<String, Element> m :
                map.entrySet()) {
            Element t = m.getValue();
            if (t.getClass().getSimpleName().equals("Circle")) {
                Response.put(m.getKey(), new RequestEntity(
                        "Circle", 0, 0, ((Circle) t).getRadius(), 0, ((Circle) t).getFill(), ((Circle) t).getPosition(),
                        ((Circle) t).getOpacity(), t.getStroke(), t.getStrokeWidth(), t.isDraggable(), t.getKey(), null, t.getScaleX(), t.getScaleY()
                ));
            } else if (t.getClass().getSimpleName().equals("Rectangle")) {
                Response.put(m.getKey(), new RequestEntity(
                        "Rectangle", ((Rectangle) t).getHeight(), ((Rectangle) t).getWidth(), 0, 0, ((Rectangle) t).getFill(), ((Rectangle) t).getPosition(),
                        ((Rectangle) t).getOpacity(), t.getStroke(), t.getStrokeWidth(), t.isDraggable(), t.getKey(), null, t.getScaleX(), t.getScaleY()
                ));
            } else if (t.getClass().getSimpleName().equals("Polygon")) {
                Response.put(m.getKey(), new RequestEntity(
                        "Polygon", 0, 0, ((Polygon) t).getRadius(), ((Polygon) t).getNumberOfSides(), ((Polygon) t).getFill(), ((Polygon) t).getPosition(),
                        ((Polygon) t).getOpacity(), t.getStroke(), t.getStrokeWidth(), t.isDraggable(), t.getKey(), null, t.getScaleX(), t.getScaleY()
                ));
            } else if (t.getClass().getSimpleName().equals("Ellipse")) {
                Response.put(m.getKey(), new RequestEntity(
                        "Ellipse", ((Ellipse) t).getHeight(), ((Ellipse) t).getWidth(), 0, 0, ((Ellipse) t).getFill(), ((Ellipse) t).getPosition(),
                        ((Ellipse) t).getOpacity(), t.getStroke(), t.getStrokeWidth(), t.isDraggable(), t.getKey(), null, t.getScaleX(), t.getScaleY()
                ));

            } else if (t.getClass().getSimpleName().equals("Line")) {
                Response.put(m.getKey(), new RequestEntity(
                        "Line", 0, 0, 0, 0, null, null,
                        null, t.getStroke(), t.getStrokeWidth(), t.isDraggable(), t.getKey(), ((Line) t).getPoints(), t.getScaleX(), t.getScaleY()
                ));

            }
        }
        return Response;
    }
}









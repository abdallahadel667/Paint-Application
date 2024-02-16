package com.oop.assignment3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class SaveAndLoad {
    ShapeDto shapeDto;
    ShapeService shapeService;

    @Autowired
    public SaveAndLoad(ShapeDto shapeDto, ShapeService shapeService) {
        this.shapeDto = shapeDto;
        this.shapeService = shapeService;
    }


    public void saveToJson(String path) {
        ObjectMapper mapper = new ObjectMapper();
        LinkedList<RequestEntity> list=new LinkedList<>();
        try (FileWriter writer = new FileWriter(path)) {

            for (Map.Entry<String,Element> a :
                    shapeDto.shapes.entrySet()) {

                Element s =a.getValue();
                RequestEntity r=new RequestEntity();
                String type=s.getClass().getSimpleName();
                r.setType(type);

                r.setDraggable(s.isDraggable());

                r.setStroke(s.getStroke());
                r.setStrokeWidth(s.getStrokeWidth());
                r.setKey(s.getKey());

                if (type.equalsIgnoreCase("rectangle")){
                    r.setHeight((((Rectangle) s).getHeight()));
                    r.setWidth(((Rectangle) s).getWidth());
                    r.setFill(((Rectangle)s).getFill());
                    r.setOpacity(((Rectangle )s).getOpacity());
                    r.setPosition(((Rectangle)s).getPosition());
                } else if (type.equalsIgnoreCase("circle")){
                    r.setRadius(((Circle) s).getRadius());
                    r.setFill(((Circle)s).getFill());
                    r.setOpacity(((Circle )s).getOpacity());
                    r.setPosition(((Circle)s).getPosition());
                } else if (type.equalsIgnoreCase("polygon")){
                    r.setNumberOfSides(((Polygon)s).getNumberOfSides());
                    r.setRadius(((Polygon) s).getRadius());
                    r.setFill(((Polygon)s).getFill());
                    r.setOpacity(((Polygon )s).getOpacity());
                    r.setPosition(((Polygon)s).getPosition());                }
                else if (type.equalsIgnoreCase("ellipse")) {
                    r.setHeight((((Ellipse) s).getHeight()));
                    r.setWidth(((Ellipse) s).getWidth());
                    r.setFill(((Ellipse) s).getFill());
                    r.setOpacity(((Ellipse )s).getOpacity());
                    r.setPosition(((Ellipse)s).getPosition());
                } else if (type.equalsIgnoreCase("line")) {
                    r.setPoints(((Line)s).getPoints());
                }
                list.add(r);
            }

            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error while saving to JSON", e);
        }
    }

    public HashMap<String, Element> convertJsonToHashMap(String path) {
    ObjectMapper objectMapper = new ObjectMapper();
    HashMap<String, Element> resultMap = new HashMap<>();
    List<RequestEntity> list = null;

    try {
        File file = new File(path);
        list = objectMapper.readValue(file, new TypeReference<List<RequestEntity>>() {});
    } catch (IOException e) {
        e.printStackTrace();
    }

    if (list != null) {
        for (RequestEntity entity : list) {
            resultMap.put(entity.getKey(), shapeService.draw(entity));
        }
    }
    shapeDto.UndoStack.clear();
    shapeDto.RedoStack.clear();
    return resultMap;

    }

    public void saveToXml(String path) {
        XmlMapper xmlMapper = new XmlMapper();
        LinkedList<RequestEntity> list = new LinkedList<>();

        try (FileWriter fileWriter = new FileWriter(path);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            for (Map.Entry<String, Element> entry : shapeDto.shapes.entrySet()) {
                Element s = entry.getValue();
                RequestEntity r = new RequestEntity();
                String type = s.getClass().getSimpleName();
                r.setType(type);

                // Set common properties
                r.setDraggable(s.isDraggable());
                r.setStroke(s.getStroke());
                r.setStrokeWidth(s.getStrokeWidth());
                r.setKey(s.getKey());
                r.setRotation(s.getRotation());
                r.setScaleX(s.getScaleX());
                r.setScaleY(s.getScaleY());

                // Set shape-specific properties
                if (type.equalsIgnoreCase("rectangle")) {
                    // Set properties for Rectangle
                    r.setHeight((((Rectangle) s).getHeight()));
                    r.setWidth(((Rectangle) s).getWidth());
                    r.setFill(((Rectangle)s).getFill());
                    r.setOpacity(((Rectangle )s).getOpacity());
                    r.setPosition(((Rectangle)s).getPosition());
                } else if (type.equalsIgnoreCase("circle")) {
                    // Set properties for Circle
                    r.setRadius(((Circle) s).getRadius());
                    r.setFill(((Circle)s).getFill());
                    r.setOpacity(((Circle )s).getOpacity());
                    r.setPosition(((Circle)s).getPosition());
                } else if (type.equalsIgnoreCase("polygon")) {
                    // Set properties for Polygon
                    r.setNumberOfSides(((Polygon)s).getNumberOfSides());
                    r.setRadius(((Polygon) s).getRadius());
                    r.setFill(((Polygon)s).getFill());
                    r.setOpacity(((Polygon )s).getOpacity());
                    r.setPosition(((Polygon)s).getPosition());
                } else if (type.equalsIgnoreCase("ellipse")) {
                    // Set properties for Ellipse
                    r.setHeight((((Ellipse) s).getHeight()));
                    r.setWidth(((Ellipse) s).getWidth());
                    r.setFill(((Ellipse) s).getFill());
                    r.setOpacity(((Ellipse )s).getOpacity());
                    r.setPosition(((Ellipse)s).getPosition());
                } else if (type.equalsIgnoreCase("line")) {
                    // Set properties for Line
                    r.setPoints(((Line)s).getPoints());
                } else {
                    throw new IllegalArgumentException("Unsupported shape type: " + type);
                }

                list.add(r);
            }

            String xml = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
            writer.write(xml);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error while saving to XML", e);
        }
    }

    public HashMap<String, Element> convertXmlToHashMap(String path) {
        XmlMapper xmlMapper = new XmlMapper();
        HashMap<String, Element> resultMap = new HashMap<>();
        List<RequestEntity> list = null;
        try {
            File file = new File(path);
            // Use TypeReference to specify the type of the List<RequestEntity>
            list = xmlMapper.readValue(file, new TypeReference<List<RequestEntity>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null) {
            for (RequestEntity entity : list) {
                // Assuming shapeService.draw(entity) returns an Element
                Element element = shapeService.draw(entity);

                // Assuming entity.getKey() returns the key for the HashMap
                resultMap.put(entity.getKey(), element);
            }
        }
        return resultMap;
    }





}

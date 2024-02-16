package com.oop.assignment3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController

@CrossOrigin
public class ShapeController {
    @Autowired
    ShapeDto shapeDto;
    ShapeService shapeService;
    SaveAndLoad saveAndLoad;

    @Autowired
    public ShapeController(ShapeDto shapeDto, ShapeService shapeService, SaveAndLoad saveAndLoad) {
        this.shapeDto = shapeDto;
        this.shapeService = shapeService;
        this.saveAndLoad = saveAndLoad;
    }

    //Creating a new shape object
    @CrossOrigin(origins = "http://localhost:8080/")
    @PostMapping("/create")
    public Element newShape(@RequestBody RequestEntity shape){
        // Factory
        Element s=shapeService.draw(shape);
        shapeDto.addShape(s);
        System.out.println(s.getClass().getSimpleName());
        return s;
    }
    //copy element
    @GetMapping("/clone/{sourceKey}/{destinationKey}")
    public void cloneShape(@PathVariable String sourceKey,@PathVariable String destinationKey){
        Element shape=shapeDto.getShape(sourceKey).copy();
        shape.setKey(destinationKey);
        shapeDto.addShape(shape);
    }
    //saving in json format
    @PostMapping("/save/json")
    public void saveJson(@RequestBody Path path){
        saveAndLoad.saveToJson(path.getPath());
    }
    //saving in xml format
    @PostMapping("/save/xml")
    public void saveXml(@RequestBody Path path) {
        saveAndLoad.saveToXml(path.getPath());
    }
    //loading from a json file
    @PostMapping("/load/json")
    public HashMap<String, RequestEntity> loadJson(@RequestBody Path path){
        HashMap<String,Element> map=saveAndLoad.convertJsonToHashMap(path.getPath());
        shapeDto.shapes= new HashMap<>(map);
        shapeDto.UndoStack.push(new HashMap<>(map));
        return shapeService.transformToRequestEntity(shapeDto.shapes);
    }

    // Loading from xml file
    @PostMapping("/load/xml")
    public HashMap<String, RequestEntity> loadXml(@RequestBody Path path){
        System.out.println(path.getPath());
        HashMap<String,Element> map=saveAndLoad.convertXmlToHashMap(path.getPath());
        shapeDto.shapes= new HashMap<>(map);
        return shapeService.transformToRequestEntity(shapeDto.shapes);
    }

    //redo
    @GetMapping("/redo")
    public HashMap<String, RequestEntity> redo() {
        shapeDto.shapes=new HashMap<>(shapeDto.redo());
       return shapeService.transformToRequestEntity(shapeDto.shapes);
    }
    //undo
    @GetMapping("/undo")
    public HashMap<String, RequestEntity> undo() {
        shapeDto.shapes=new HashMap<>(shapeDto.undo());
        return shapeService.transformToRequestEntity(shapeDto.shapes);
    }

    //delete element at a given key
    @GetMapping("/delete/{key}")
    public void deleteShape(@PathVariable String key){
        shapeDto.delete(key);
    }


    //clearing the whole layer
    @GetMapping("/clear")
    public void clear(){
        shapeDto.UndoStack.clear();
        shapeDto.RedoStack.clear();
         shapeDto.shapes.clear();
    }

///------------------------------------------------------------------------------------------------------------------///
    //test functions

    //retrieve an object by its key
    @GetMapping("/get/{key}")
    public Element retrieve(@PathVariable String key){
        System.out.println(shapeDto.shapes.size());
        System.out.println(shapeDto.shapes.get(key).getClass().getSimpleName());
        return shapeDto.getShape(key);
    }
    //get Size of map
    @GetMapping("/size")
    public int size(){
        return shapeDto.dbSize();
    }
}

package com.oop.assignment3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ShapeDto {

     Stack<HashMap<String, Element>> UndoStack=new Stack<>();
     Stack<HashMap<String, Element>> RedoStack=new Stack<>();
     HashMap<String, Element> shapes;

    @Autowired
    public ShapeDto(HashMap<String, Element> shapes) {
        this.shapes = shapes;
    }

    public ShapeDto() {}

    public HashMap<String, Element> getShapes() {
        return shapes;
    }

    public void setShapes(HashMap<String, Element> shapes) {
        this.shapes = shapes;
    }

    public void addShape(Element shape) {
        shapes.put(shape.getKey(), shape);
        UndoStack.push(deepCopy(shapes));
    }

    public Element getShape(String key) {
        return shapes.get(key);
    }

    public void delete(String key) {
        shapes.remove(key);
        UndoStack.push(deepCopy(shapes));
    }

    public int dbSize() {
        return shapes.size();
    }



    public HashMap<String, Element> undo() {
        try {
            if (!UndoStack.isEmpty()) {
                System.out.println("Before Undo: " + UndoStack.toString());

                // Push a deep copy of the current state onto RedoStack
                RedoStack.push(new HashMap<>(deepCopy(UndoStack.pop())));

                System.out.println("After Undo: " + UndoStack.toString());
                System.out.println("Redo Stack: " + RedoStack.toString());

                // Return a deep copy of the current state
                if (!UndoStack.isEmpty()) {
                    HashMap<String, Element> currentState = deepCopy(UndoStack.peek());
                    System.out.println("Current State: " + currentState.toString());
                    return currentState;
                }
            } else {
                System.out.println("UndoStack is empty. Cannot perform undo.");
            }
        } catch (EmptyStackException e) {
            return new HashMap<>();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
        }

        // Return an empty HashMap if undo fails or an exception occurs
        return new HashMap<>();
    }



    public HashMap<String, Element> redo() {
        if (!RedoStack.isEmpty()) {
            System.out.println("Before Redo: " + RedoStack.toString());

            // Pop an element from RedoStack and make a deep copy
            HashMap<String, Element> t = deepCopy(RedoStack.pop());

            // Print for debugging
            System.out.println("Popped Element from Redo: " + t.toString());

            // Push a deep copy of the popped element onto UndoStack
            UndoStack.push(deepCopy(t));


            System.out.println("After Redo: " + UndoStack.toString());
            System.out.println("Redo Stack: " + RedoStack.toString());

            // Return a deep copy of the current state
            HashMap<String, Element> currentState = deepCopy(UndoStack.peek());
            System.out.println("Current State after Redo: " + currentState.toString());
            return currentState;
        } else {
            return shapes;
        }
    }

    // Deep copy hashmap <String, Element>
    private HashMap<String, Element> deepCopy(HashMap<String, Element> original) {
        HashMap<String, Element> copy = new HashMap<>();

        for (Map.Entry<String, Element> entry : original.entrySet()) {
            String key = entry.getKey();
            Element value = entry.getValue().copy();
            copy.put(key, value);
        }
        return copy;
    }

}

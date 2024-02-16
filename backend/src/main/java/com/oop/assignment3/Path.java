package com.oop.assignment3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Path {
    private String path;

    @Autowired
    public Path(String path) {
        this.path = path;
    }
    public Path(){}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

package com.oop.assignment3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.*;

@Configuration
public class AppConfig {
    @Bean
    public String string() {
        return "";
    }
    @Bean
    public Integer integer(){return 0;}
    @Bean
    public Element element(){
        return new Element() ;
    }
    @Bean
    public Stack<HashMap<String,Element>> shapes(){
        return new Stack<>();
    }
    @Bean
    public boolean bool(){
        return false;
    }
    @Bean
    public RequestEntity requestEntity(){return new RequestEntity();}
    @Bean
    public Position position(){
        return new Position();
    }
    @Bean
    public Path path(){
        return new Path();
    }
    @Bean
    public HashMap<String,Element> elementHashMap(){
        return new HashMap<>();
    }
}

package org.example;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//można ale nie trzeba
//@RequestMapping("rectangle")
public class RectangleController {

    @GetMapping("single")
    public Rectangle rectangle(){
        return new Rectangle(5, 10, 15, 10, "blue");
    }

    private List<Rectangle> rectangleList = new ArrayList<>();

    @GetMapping("add")
    public void addRectangle(){
        rectangleList.add(new Rectangle(5, 8, 3, 7, "yellow"));
        System.out.println("Rectangle added");
        System.out.println("Current list size: " + rectangleList.size());
    }

    @GetMapping("rectangles")
    public List<Rectangle> showRectangleList(){
        return rectangleList;
    }

    @GetMapping("rectanglesToSvg")
    public String rectanglesToSvg(){
        StringBuilder sb = new StringBuilder();
        sb.append("<svg width=\"300\" height=\"130\" xmlns=\"http://www.w3.org/2000/svg\">\n");

        for (Rectangle rectangle : rectangleList){
            sb.append(String.format("<rect width=\"%d\" height=\"%d\" x=\"%d\" y=\"%d\" fill=\"%s\" />", rectangle.getWidth(), rectangle.getHeight(), rectangle.getX(), rectangle.getY(), rectangle.getColor()) + "\n");
        }

        sb.append("</svg>");
        return String.valueOf(sb);
    }

    @PostMapping("addPost")
    public void addRectanglePost(@RequestBody Rectangle rectangle){
        rectangleList.add(rectangle);
        System.out.println("Rectangle added: " + rectangle);
        System.out.println("Current list size: " + rectangleList.size());
    }

    //komenda do sprawdzenia: curl -X POST -H 'Content-Type:application/json' -d '{"x": 50, "y": 70, "width": 30, "height": 60, "color": "blue"}' localhost:8080/addPost
    // ◦ x, y, width, height, color - muszą się zgadzać z konstruktorem w klasie rectangle bo nie zadziała
    // ◦ trzeba dodać pusty konstruktor w Rectangle: public Rectangle(){}
    // ◦ localhost:8080/addPost - (/addPost) musi się zgadzać z tym co jest przy @PostMapping


    @GetMapping("get/{index}")
    public Rectangle GET(@PathVariable("index") int index){
        return rectangleList.get(index);
    }

    @PutMapping("put/{index}")
    public Rectangle PUT(@PathVariable("index") int index, @RequestBody Rectangle rectangle){
        return rectangleList.set(index, rectangle);
    }

//    curl -X PUT -H 'Content-Type:application/json' -d '{"x": 50, "y": 70, "width": 30, "height": 60, "color": "blue"}' localhost:8080/put/{index}

    @DeleteMapping("delete/{index}")
    public Rectangle DELETE(@PathVariable("index") int index){
        return rectangleList.remove(index);
    }
//    curl -X DELETE localhost:8080/delete/{index}
}
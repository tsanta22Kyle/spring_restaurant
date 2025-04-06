package org.accdatabase.stockmanager_spring.RestController;

import org.accdatabase.stockmanager_spring.Service.DishService;
import org.accdatabase.stockmanager_spring.entities.Dish;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DishController {


    private DishService dishService;

    public DishController() {
        this.dishService = new DishService();
    }


    @GetMapping("")
    public String Test(){
        return "hello world";
    }
    @GetMapping("")
    public String est(){
        return "hello world";
    }
    @GetMapping("dish")
    public List<Dish> dishs(){
        return dishService. getAll();
    }

}

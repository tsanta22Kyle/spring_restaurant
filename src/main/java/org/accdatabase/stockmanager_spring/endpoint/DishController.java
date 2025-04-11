package org.accdatabase.stockmanager_spring.endpoint;

import org.accdatabase.stockmanager_spring.Service.DishService;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DishController {

    @Autowired
    private DishService dishService;




    @GetMapping("")
    public String Test(){
        return "hello world";
    }

    @GetMapping("dish")
    public ResponseEntity<Object> allDish(@RequestParam(defaultValue = "1",required = false) int page, @RequestParam(defaultValue = "5",required = false) int size ){
        return ResponseEntity.ok(dishService.getAll(page,size));
    }

}

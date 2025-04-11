package org.accdatabase.stockmanager_spring.endpoint;

import org.accdatabase.stockmanager_spring.Service.DishService;
import org.accdatabase.stockmanager_spring.endpoint.rest.CreateDishIngredient;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.accdatabase.stockmanager_spring.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("")
    public ResponseEntity<Object> allDish(@RequestParam(defaultValue = "1",required = false) int page, @RequestParam(defaultValue = "5",required = false) int size ){
        return ResponseEntity.ok(dishService.getAll(page,size));
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<Object> addIngredients(@PathVariable String id, @RequestBody List<CreateDishIngredient> ingredients){
        return ResponseEntity.ok(dishService.addDishIngredient(id,ingredients));
    }
}

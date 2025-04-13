package org.accdatabase.stockmanager_spring.endpoint;

import org.accdatabase.stockmanager_spring.Service.DishService;
import org.accdatabase.stockmanager_spring.Service.exception.ClientException;
import org.accdatabase.stockmanager_spring.Service.exception.NotFoundException;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.endpoint.rest.CreateDishIngredient;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.accdatabase.stockmanager_spring.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("")
    public ResponseEntity<Object> allDish(@RequestParam(defaultValue = "1",required = false) int page, @RequestParam(defaultValue = "5",required = false) int size ){
        try {
        return ResponseEntity.ok(dishService.getAll(page,size));
        }catch (ClientException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (ServerException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }catch (NotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<Object> addIngredients(@PathVariable String id, @RequestBody List<CreateDishIngredient> ingredients){
        try {
        return ResponseEntity.ok(dishService.addDishIngredient(id,ingredients));
        }catch (ClientException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (ServerException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }catch (NotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }


}

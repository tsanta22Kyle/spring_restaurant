package org.accdatabase.stockmanager_spring.endpoint;


import org.accdatabase.stockmanager_spring.Service.IngredientService;
import org.accdatabase.stockmanager_spring.Service.exception.ClientException;
import org.accdatabase.stockmanager_spring.Service.exception.NotFoundException;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.endpoint.rest.CreateOrUpdateIngredient;
import org.accdatabase.stockmanager_spring.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("ingredients")
public class IngredientRestController {
    @Autowired
    private IngredientService ingredientService;


    @GetMapping("")
    public ResponseEntity<Object> getIngredients(@RequestParam(required = false,defaultValue = "1") int page, @RequestParam(required = false,defaultValue = "5") int size) {
        try {
        return ResponseEntity.of(ingredientService.getAll(page, size));
        }catch (ClientException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }catch (ServerException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getIngredientByPrice(@RequestParam(required = false) double minPrice, @RequestParam(required = false) double maxPrice, @RequestParam(required = false,defaultValue = "1") int page, @RequestParam(required = false , defaultValue = "5") int size) {
       try {

        return ResponseEntity.ok(ingredientService.getAllIngredientsByPrice(page, size, maxPrice, minPrice));
       }catch (ClientException e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }catch (NotFoundException e){
           return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
       }catch (ServerException e){
           return ResponseEntity.internalServerError().body(e.getMessage());
       }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getIngredientById(@PathVariable String id) {
    try {

        return ResponseEntity.of(ingredientService.findById(id));
    }catch (ClientException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }catch (NotFoundException e){
        return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
    }catch (ServerException e){
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    }

    @PostMapping("")
    public ResponseEntity<Object> addIngredients(@RequestBody List<CreateOrUpdateIngredient> ingredientToAdd) {
        Optional<Object> ingredients = ingredientService.addAll(ingredientToAdd);
        return ResponseEntity.ok(ingredients);

    }

    @PutMapping("")
    public ResponseEntity<Object> updateIngredients(@RequestBody List<CreateOrUpdateIngredient> ingredientToUpdate) {

        return ResponseEntity.of(ingredientService.addAll(ingredientToUpdate));

    }

}

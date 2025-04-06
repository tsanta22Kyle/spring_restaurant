package org.accdatabase.stockmanager_spring.RestController;


import org.accdatabase.stockmanager_spring.Service.IngredientService;
import org.accdatabase.stockmanager_spring.entities.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("ingredients")
public class IngredientRestController {
    @Autowired
    private IngredientService ingredientService;



    /*@GetMapping("")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        return ResponseEntity.ok(ingredientService.);
    }*/

    @GetMapping("/filter")
    public ResponseEntity<Object> getIngredientByPrice(@RequestParam(required = false) double minPrice ,@RequestParam(required = false) double maxPrice ,@RequestParam(required = false) int page, @RequestParam(required = false) int size ) {
        return ResponseEntity.ok(ingredientService.getAllIngredientsByPrice(page,size,maxPrice,minPrice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getIngredientById(@PathVariable int id) {
        Ingredient ingredient = Ingredients().stream().filter(ingredient1 -> ingredient1.getId()==id).findFirst().orElse(null);
        if(ingredient == null) {
            return new ResponseEntity<>("Ingredient"+id+" not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ingredient);
    }

    @PostMapping("")
    public ResponseEntity<Object> addIngredient(@RequestBody List<Ingredient> ingredientToAdd) {
        ingredients.addAll(ingredientToAdd);
        return ResponseEntity.ok(ingredients);

    }
    @PutMapping("")
    public ResponseEntity<Object> updateIngredient(@RequestBody List<Ingredient> ingredientToUpdate) {
        ingredients.stream().filter(ingredient -> ingredient.getId()==ingredientToUpdate.get(0).getId())
                .forEach(ingredient -> {
                    ingredient.setName(ingredientToUpdate.get(0).getName());
                    ingredient.setUnitPrice(ingredientToUpdate.get(0).getUnitPrice());
                    ingredient.setUpdatedAt(Instant.now());

                });
        return ResponseEntity.ok(ingredients);
    }

}

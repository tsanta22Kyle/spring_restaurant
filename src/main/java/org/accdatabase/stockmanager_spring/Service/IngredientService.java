package org.accdatabase.stockmanager_spring.Service;

import org.accdatabase.stockmanager_spring.Repository.DAO.IngredientCrudRequests;
import org.accdatabase.stockmanager_spring.entities.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientService {

    private IngredientCrudRequests ingredientCrudRequests;

    public IngredientService(IngredientCrudRequests ingredientCrudRequests) {
        this.ingredientCrudRequests = ingredientCrudRequests;
    }

    public List<Ingredient> getAllIngredientsByPrice(int page, int size, double maxPrice, double minPrice) {
        List<Ingredient> ingredientList = ingredientCrudRequests.findAll(page, size);

        return (ingredientList.stream().filter(ingredient -> ingredient.getPrices().stream().filter((price) -> price.getValue() >= minPrice && price.getValue() <= maxPrice).findAny().isPresent()).toList());


    }
    public List<Ingredient> getAllIngredients(int page, int size) {
      return ingredientCrudRequests.findAll(page, size);
    }

    public List<Ingredient> addAllIngredients(List<Ingredient> ingredients) {
        return
    }

}

package org.accdatabase.stockmanager_spring.Service;


import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.operations.DishCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.DishIngredientCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.IngredientCrudRequests;
import org.accdatabase.stockmanager_spring.Service.exception.ClientException;
import org.accdatabase.stockmanager_spring.endpoint.mapper.DishIngredientRestMapper;
import org.accdatabase.stockmanager_spring.endpoint.mapper.DishRestMapper;
import org.accdatabase.stockmanager_spring.endpoint.rest.CreateDishIngredient;
import org.accdatabase.stockmanager_spring.endpoint.rest.DishIngredientRest;
import org.accdatabase.stockmanager_spring.endpoint.rest.DishRest;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.accdatabase.stockmanager_spring.model.Ingredient;
import org.accdatabase.stockmanager_spring.model.IngredientQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DishService {


    @Autowired
    private DishCrudRequests dishCrudRequests;
    @Autowired
    private DishRestMapper dishRestMapper;
    @Autowired
    private IngredientCrudRequests ingredientCrudRequests;
    @Autowired
    private DishIngredientRestMapper dishIngredientRestMapper;
    @Autowired
    private DishIngredientCrudRequests dishIngredientCrudRequests;


    public Optional<Object> getAll(int page, int size) {
        if(page<0 || size <0){
            throw new ClientException("page and size should be positive");
        }

    List<DishRest> dishRests = dishCrudRequests.findAll(page, size).stream().map(dish -> dishRestMapper.toRest(dish) ).toList() ;

    return Optional.of(dishRests);
    }

    public Optional<Object> addDishIngredient(String dishId, List<CreateDishIngredient> ingredients) {

        List<Ingredient> ingredientsToSave = ingredients.stream().map(createDishIngredient -> {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(createDishIngredient.getName());
            return ingredient;
        }).toList();

      List<Ingredient> savedIngredients =  ingredientCrudRequests.saveAll(ingredientsToSave);
        List<IngredientQuantity> dishIngredientsToSave = new ArrayList<>();
      ingredients.forEach(createDishIngredient -> {
          dishIngredientsToSave.addAll(savedIngredients.stream().map(ingredient -> {
          IngredientQuantity ingredientQuantity = new IngredientQuantity();
          ingredientQuantity.setIngredient(ingredient);
          ingredientQuantity.setQuantity(createDishIngredient.getQuantity());
          ingredientQuantity.setUnit(createDishIngredient.getUnit());
          return ingredientQuantity;
      }).toList());
      });
      List<DishIngredientRest> dishIngredientRestList = dishIngredientCrudRequests.saveAll(dishId,dishIngredientsToSave).stream().map(ingredientQuantity -> dishIngredientRestMapper.toRest(ingredientQuantity)).toList();
      return Optional.of(  dishIngredientRestList);
    }
}

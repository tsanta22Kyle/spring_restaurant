package org.accdatabase.stockmanager_spring.endpoint.mapper;


import org.accdatabase.stockmanager_spring.DAO.operations.IngredientCrudRequests;
import org.accdatabase.stockmanager_spring.endpoint.rest.DishIngredientRest;
import org.accdatabase.stockmanager_spring.model.IngredientQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DishIngredientRestMapper {

    @Autowired
    private StockMoveRestMapper stockMoveRestMapper;

    @Autowired
    private IngredientCrudRequests ingredientCrudRequests;

    public DishIngredientRest toRest(IngredientQuantity dishIngredient){
        System.out.println("price at date : "+dishIngredient.getIngredient().getPriceAtDate());
        Double recentPrice = dishIngredient.getIngredient().getPriceAtDate() ==  null ?0.0:dishIngredient.getIngredient().getPriceAtDate().getValue() ;
        DishIngredientRest dishIngredientRest = new DishIngredientRest(dishIngredient.getIngredient().getIngredientId(),dishIngredient.getIngredient().getName(),recentPrice , dishIngredient.getIngredient().getAvailableQuantityAt(LocalDateTime.now()),dishIngredient.getQuantity(),dishIngredient.getUnit());
        return dishIngredientRest;
    }

    public IngredientQuantity toModel(DishIngredientRest dishIngredientRest){
        IngredientQuantity ingredientQuantity = new IngredientQuantity();
        ingredientQuantity.setQuantity(dishIngredientRest.getRequiredQuantity());
        ingredientQuantity.setIngredient(ingredientCrudRequests.findById(dishIngredientRest.getIngredientId()));
        ingredientQuantity.setUnit(dishIngredientRest.getUnit());
        return ingredientQuantity;

    }

}

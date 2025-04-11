package org.accdatabase.stockmanager_spring.endpoint.mapper;


import org.accdatabase.stockmanager_spring.endpoint.rest.DishIngredientRest;
import org.accdatabase.stockmanager_spring.model.IngredientQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DishIngredientRestMapper {

    @Autowired
    private StockMoveRestMapper stockMoveRestMapper;

    public DishIngredientRest toRest(IngredientQuantity dishIngredient){
        DishIngredientRest dishIngredientRest = new DishIngredientRest(dishIngredient.getIngredient().getName(), dishIngredient.getIngredient().getPriceAtDate().getValue(), dishIngredient.getIngredient().getAvailableQuantityAt(LocalDateTime.now()),dishIngredient.getQuantity());
        return dishIngredientRest;
    }
}

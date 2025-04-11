package org.accdatabase.stockmanager_spring.endpoint.mapper;

import org.accdatabase.stockmanager_spring.endpoint.rest.DishIngredientRest;
import org.accdatabase.stockmanager_spring.endpoint.rest.DishRest;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DishRestMapper {

    @Autowired
    private DishIngredientRestMapper dishIngredientRestMapper;


    public DishRest toRest(Dish dish) {

        List<DishIngredientRest> ingredientRestList = dish.getIngredientList().stream().map(ingredientQuantity -> dishIngredientRestMapper.toRest(ingredientQuantity)).toList();

        DishRest dishRest = new DishRest(dish.getName(),  dish.getUnitPrice(),ingredientRestList,dish.getAvailableQuantity());
        return dishRest;
    }

}

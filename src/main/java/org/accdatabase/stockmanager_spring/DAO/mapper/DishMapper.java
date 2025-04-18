package org.accdatabase.stockmanager_spring.DAO.mapper;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.operations.IngredientCrudRequests;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;
@Component
public class DishMapper implements Function<ResultSet, Dish> {

    @Autowired
    private IngredientCrudRequests ingredientCrudRequests;

    @SneakyThrows
    @Override
    public Dish apply(ResultSet rs) {
        Dish dish = new Dish();

        dish.setDishId(rs.getString("dish_id"));
        dish.setName(rs.getString(2));
        dish.setUnitPrice(rs.getInt("unit_price"));
        dish.setIngredientList(ingredientCrudRequests.findIngredientByDishId(dish.getDishId()));
        return dish;
    }
}

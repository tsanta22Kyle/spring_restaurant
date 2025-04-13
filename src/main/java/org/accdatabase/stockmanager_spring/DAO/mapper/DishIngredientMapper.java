package org.accdatabase.stockmanager_spring.DAO.mapper;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.operations.IngredientCrudRequests;
import org.accdatabase.stockmanager_spring.model.IngredientQuantity;
import org.accdatabase.stockmanager_spring.model.unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;
@Component
public class DishIngredientMapper implements Function<ResultSet, IngredientQuantity> {

    @Autowired
    private IngredientCrudRequests ingredientCrudRequests;

    @SneakyThrows
    @Override
    public IngredientQuantity apply(ResultSet resultSet) {
        IngredientQuantity ingredientQuantity = new IngredientQuantity();
        String ingredientId = resultSet.getString("ingredient_id");
        ingredientQuantity.setIngredient(ingredientCrudRequests.findById(ingredientId));
        ingredientQuantity.setQuantity(resultSet.getInt("required_quantity"));
        ingredientQuantity.setUnit(unit.valueOf(resultSet.getString("unit")));
        return ingredientQuantity;
    }
}

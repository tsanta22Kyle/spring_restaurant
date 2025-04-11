package org.accdatabase.stockmanager_spring.DAO.mapper;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.operations.IngredientCrudRequests;
import org.accdatabase.stockmanager_spring.model.Ingredient;
import org.accdatabase.stockmanager_spring.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;
@Component
public class PriceMapper implements Function<ResultSet,Price> {

    @SneakyThrows
    @Override
    public Price apply(ResultSet resultSet) {
        String ingredientId = resultSet.getString("ingredient_id");


        Price price = new Price();
        price.setId(resultSet.getString("price_id"));
        price.setValue(resultSet.getDouble("value"));
        price.setDate(resultSet.getDate("date").toLocalDate());
        //System.out.println("returned price: " + price.getValue());
        return price;
    }
}

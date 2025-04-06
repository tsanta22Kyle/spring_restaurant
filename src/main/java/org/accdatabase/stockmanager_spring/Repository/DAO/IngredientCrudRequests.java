package org.accdatabase.stockmanager_spring.Repository.DAO;

import lombok.RequiredArgsConstructor;
import org.accdatabase.stockmanager_spring.Mapper.UnitMapper;
import org.accdatabase.stockmanager_spring.entities.Ingredient;
import org.accdatabase.stockmanager_spring.entities.IngredientQuantity;
import org.accdatabase.stockmanager_spring.entities.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientCrudRequests {
    private UnitMapper unitMapper ;
    private DataSource dataSource ;

    @Autowired
    public IngredientCrudRequests(UnitMapper unitMapper, DataSource dataSource) {
        this.unitMapper = unitMapper;
        this.dataSource = dataSource;
    }

    public IngredientCrudRequests(DataSource dataSource) {
        this.dataSource = new DataSource();
        this.unitMapper = new UnitMapper();
    }

    public List<Price> findPriceByIngredientId(String id) {
        List<Price> prices = new ArrayList<>();
        try (
                PreparedStatement statement = dataSource.getConnection().prepareStatement("SELECT price.price_id,value,date,unit from price where ingredient_id = ? ORDER BY date ASC");
        ) {
            statement.setString(1, id);
            try (
                    ResultSet rs = statement.executeQuery()
            ) {
                Price price = new Price();
                while (rs.next()) {
                    //price.setDate(LocalDate.of(rs.getDate("date").getYear(),rs.getDate("date").getMonth(),rs.getDate("date").getDay()));
                    price.setDate(rs.getDate("date").toLocalDate());
                    price.setId(rs.getString("price_id"));
                    price.setUnit(unitMapper.mapFromResultSet(rs.getObject("unit").toString()));
                    price.setValue(rs.getInt("value"));

                    prices.add(price);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prices;
    }

    public List<IngredientQuantity> findIngredientByDishId(String DishId) {
        List<IngredientQuantity> ingredientQuantities = new ArrayList<>();
        try (
                PreparedStatement statement = dataSource.getConnection().prepareStatement("SELECT dish.dish_id,dish.unit_price,i.ingredient_id,i.name,update_datetime,required_quantity,unit from dish join dish_ingredient di on dish.dish_id = di.dish_id join ingredient i on di.ingredient_id = i.ingredient_id WHERE di.dish_id = ? ORDER BY ingredient_id ASC")
        ) {
            statement.setString(1, DishId);
            try (
                    ResultSet rs = statement.executeQuery()
            ) {
                while (rs.next()) {

                    Ingredient ingredient = new Ingredient();
                    //  LocalDateTime updateDateTime = LocalDateTime.of(rs.getTimestamp("update_datetime").getYear(),rs.getTimestamp("update_datetime").getDate(),rs.getTimestamp("update_datetime").getDay(),rs.getTimestamp("update_datetime").getHours(),rs.getTimestamp("update_datetime").getMinutes());
                    //LocalDateTime up = rs.getTimestamp().toLocalDateTime()
                    ingredient.setIngredientId(rs.getString("ingredient_id"));
                    ingredient.setName(rs.getString("name"));
                    ingredient.setPrices(findPriceByIngredientId(ingredient.getIngredientId()));
                    ingredient.setUpdateDatetime(rs.getTimestamp("update_datetime").toLocalDateTime());
                    ingredientQuantities.add(new IngredientQuantity(ingredient, rs.getDouble("required_quantity"), unitMapper.mapFromResultSet(rs.getObject("unit").toString())));
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientQuantities;
    }

    public List findAll(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();

        try (
                PreparedStatement statement = dataSource.getConnection().prepareStatement("SELECT ingredient_id, name, update_datetime FROM Ingredient")
        ) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredientId(rs.getString(1));
                    ingredient.setName(rs.getString(2));
                    ingredient.setUpdateDatetime(rs.getTimestamp(3).toLocalDateTime());
                    ingredient.setPrices(findPriceByIngredientId(ingredient.getIngredientId()));
                    ingredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return ingredients;
    }
}

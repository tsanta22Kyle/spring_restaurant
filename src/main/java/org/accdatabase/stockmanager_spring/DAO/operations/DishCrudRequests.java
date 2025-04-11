package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.RequiredArgsConstructor;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishCrudRequests {

    @Autowired
    private IngredientCrudRequests ingredientCrudRequests;
    @Autowired
    private DataSource dataSource;

    public List<Dish> findAll(int page, int size) {
        List<Dish> dishes = new ArrayList<>();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("SELECT dish.dish_id,dish.name,dish.unit_price from dish join dish_ingredient di on dish.dish_id = di.dish_id join ingredient i on di.ingredient_id = i.ingredient_id limit ? offset ?")
        ) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (
                    ResultSet rs = statement.executeQuery()
            ) {
                Dish dish = new Dish();
                while (rs.next()) {
                    dish.setDishId(rs.getString("dish_id"));
                    dish.setName(rs.getString(2));
                    dish.setUnitPrice(rs.getInt("unit_price"));
                    dish.setIngredientList(ingredientCrudRequests.findIngredientByDishId(dish.getDishId()));

                }
                dishes.add(dish);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return dishes;
    }

}

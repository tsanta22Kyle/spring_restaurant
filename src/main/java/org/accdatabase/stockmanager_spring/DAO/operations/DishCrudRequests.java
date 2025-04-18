package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.mapper.DishIngredientMapper;
import org.accdatabase.stockmanager_spring.DAO.mapper.DishMapper;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.accdatabase.stockmanager_spring.model.IngredientQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishCrudRequests {

    @Autowired
    private IngredientCrudRequests ingredientCrudRequests;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DishMapper dishMapper;

    public List<Dish> findAll(int page, int size) {
        List<Dish> dishes = new ArrayList<>();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("SELECT dish.dish_id,dish.name,dish.unit_price from dish limit ? offset ?")
        ) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (
                    ResultSet rs = statement.executeQuery()
            ) {

                while (rs.next()) {

                dishes.add(dishMapper.apply(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return dishes;
    }

    public Dish findById(String dishId){
        Dish dish = null;

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("SELECT dish.dish_id,dish.name,dish.unit_price from dish where dish_id = ?")
        ) {
            statement.setString(1,dishId);
            try (
                    ResultSet rs = statement.executeQuery()
            ) {

                if(rs.next()) {
                    dish =  dishMapper.apply(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dish;
    }

}

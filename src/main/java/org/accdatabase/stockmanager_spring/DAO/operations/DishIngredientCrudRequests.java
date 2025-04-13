package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.mapper.DishIngredientMapper;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.model.IngredientQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class DishIngredientCrudRequests {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DishIngredientMapper dishIngredientMapper;


    public IngredientQuantity findById(String id) {
        throw new RuntimeException("Not implemented");
    }


    public List<IngredientQuantity> findAll(int page, int size) {
        throw new RuntimeException("Not implemented");
    }
    @SneakyThrows
    public List<IngredientQuantity> saveAll(String DishId,List<IngredientQuantity> ingredientsToSave) {
        List<IngredientQuantity> ingredients = new ArrayList<>();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("INSERT INTO dish_ingredient(dish_id, ingredient_id, required_quantity, unit) VALUES (?,?,?,?)  RETURNING dish_id,ingredient_id,unit,required_quantity")
        ) {

            ingredientsToSave.forEach(entityToSave -> {
                try {
                    statement.setString(1,DishId);
                    statement.setString(2,entityToSave.getIngredient().getIngredientId());
                    statement.setDouble(3,entityToSave.getQuantity());
                    statement.setObject(4,entityToSave.getUnit().toString(), Types.OTHER);

                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            ingredients.add(dishIngredientMapper.apply(rs));
                        }
                    }

                }catch (SQLException e) {
                    throw new ServerException(e);
                }


            });

        }
        return ingredients;
    }

}

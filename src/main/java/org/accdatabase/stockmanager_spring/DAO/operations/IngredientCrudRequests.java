package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.PostgresNextReference;
import org.accdatabase.stockmanager_spring.DAO.mapper.IngredientMapper;
import org.accdatabase.stockmanager_spring.Service.exception.NotFoundException;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IngredientCrudRequests {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PriceCrudRequests priceCrudRequests;
    @Autowired
    private StockCrudRequests stockCrudRequests;
    @Autowired
    private IngredientMapper ingredientMapper;
    final PostgresNextReference postgresNextReference = new PostgresNextReference();



    public Ingredient findById(String id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select i.ingredient_id, i.name, di.dish_id as dish_ingredient_id, di.required_quantity, di.unit from ingredient i"
                             + " left join dish_ingredient di on i.ingredient_id = di.ingredient_id"
                             + " where i.ingredient_id = ?")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return ingredientMapper.apply(resultSet);
                }
                throw new NotFoundException("Ingredient.id=" + id + " not found");
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
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
                    ingredient.setPrices(priceCrudRequests.findByIdIngredient(ingredient.getIngredientId()));
                    ingredient.setUpdateDatetime(rs.getTimestamp("update_datetime").toLocalDateTime());
                    ingredientQuantities.add(new IngredientQuantity(ingredient, rs.getDouble("required_quantity"), unit.valueOf(rs.getString("unit").toString())));
                }

            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
        return ingredientQuantities;
    }

    public List<Ingredient> findAll(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("SELECT ingredient_id, name, update_datetime FROM Ingredient limit ? offset ?")
        ) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(ingredientMapper.apply(rs));
                }
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }


        return ingredients;
    }
    @SneakyThrows
    public List<Ingredient> saveAll(List<Ingredient> toSave) {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            toSave.forEach(entityToSave -> {
                try (PreparedStatement statement =
                             connection.prepareStatement("insert into ingredient (ingredient_id, name) values (?, ?)"
                                     + " on conflict (ingredient_id) do update set name=excluded.name"
                                     + " returning ingredient_id, name")) {


                    String id = entityToSave.getIngredientId() == null
                            ? postgresNextReference.generateUUID()
                            : entityToSave.getIngredientId();
                    entityToSave.setIngredientId(id);
                    System.out.println("ingredient id : "+id);

                    statement.setString(1, id);
                    statement.setString(2, entityToSave.getName());
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        Ingredient savedIngredient = ingredientMapper.apply(resultSet);


                        if (entityToSave.getPrices() != null) {
                            entityToSave.getPrices().forEach(price -> {
                                price.setIngredient(entityToSave);
                            });
                        }


                        List<Price> prices = priceCrudRequests.saveAll(entityToSave.getPrices());
                        List<StockMove> stockMovements = stockCrudRequests.saveAll(entityToSave.getStockMoves());
                        System.out.println("entity stockMove : "+entityToSave.getStockMoves());

                        savedIngredient.addPrices(prices);
                        savedIngredient.addStockMovements(stockMovements);

                        ingredients.add(savedIngredient);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }catch (SQLException e){
            throw new ServerException(e.getMessage());
        }
        return ingredients;
    }

}

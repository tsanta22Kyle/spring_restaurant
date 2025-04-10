package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.mapper.PriceMapper;
import org.accdatabase.stockmanager_spring.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.rmi.ServerException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PriceCrudRequests implements CrudRequests<Price> {

    @Autowired private PriceMapper priceMapper;
    @Autowired private DataSource dataSource;

    @Override
    public Price findById(String id) {
     throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Price> findAll(int page, int size) {
        return List.of();
    }

    @SneakyThrows
    @Override
    public List<Price> saveAll(List<Price> entities) {
        if(entities != null && !entities.isEmpty()) {

        List<Price> prices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("insert into price (price_id, value, date, ingredient_id) values (?, ?, ?, ?)"
                             + " on conflict (price_id) do nothing"
                             + " returning price_id, value, date, ingredient_id")) {
            entities.forEach(entityToSave -> {
                if(entityToSave.getIngredient() != null) {

                try {
                    System.out.println(entityToSave);
                    statement.setString(1, entityToSave.getId());
                    statement.setDouble(2, entityToSave.getValue());
                    statement.setDate(3, Date.valueOf(entityToSave.getDate()));
                    statement.setString(4, entityToSave.getIngredient().getIngredientId());
                    statement.addBatch(); // group by batch so executed as one query in database
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                }else {
                    throw new IllegalArgumentException("l'ingredient de price n'existe pas");
                }
            });
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    prices.add(priceMapper.apply(resultSet));
                }
            }
            return prices;

        }
        }
        return List.of();
    }

    @SneakyThrows
    public List<Price> findByIdIngredient(String idIngredient) {
        List<Price> prices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select p.price_id, p.value, p.date , p.ingredient_id from price p"
                     + " join ingredient i on p.ingredient_id = i.ingredient_id"
                     + " where p.ingredient_id = ?")) {
            statement.setString( 1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Price price = priceMapper.apply(resultSet);
                    prices.add(price);
                }
                return prices;
            }
        } catch (SQLException e) {
            throw new ServerException(e.getMessage());
        }
    }
}

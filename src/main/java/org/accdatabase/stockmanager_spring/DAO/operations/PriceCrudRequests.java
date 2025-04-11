package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.PostgresNextReference;
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
    final PostgresNextReference postgresNextReference = new PostgresNextReference();

    @Override
    public Price findById(String id) {
     throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Price> findAll(int page, int size) {
        return List.of();
    }

    @Override
    @SneakyThrows
    public List<Price> saveAll(List<Price> entities) {
        List<Price> prices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            for (Price entityToSave : entities) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO price (price_id, value, date, ingredient_id) " +
                                "VALUES (?, ?, ?, ?) " +
                                "ON CONFLICT (ingredient_id, date) DO UPDATE SET " +
                                "value = EXCLUDED.value, price_id = EXCLUDED.price_id " +
                                "RETURNING price_id, value, date, ingredient_id")) {

                    String id = entityToSave.getId() == null ? postgresNextReference.generateUUID() : entityToSave.getId();
                    statement.setString(1, id);
                    statement.setDouble(2, entityToSave.getValue());
                    statement.setDate(3, Date.valueOf(entityToSave.getDate()));
                    statement.setString(4, entityToSave.getIngredient().getIngredientId());

                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                            prices.add(priceMapper.apply(rs));
                        }
                    }
                }
            }
        }
        return prices;
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

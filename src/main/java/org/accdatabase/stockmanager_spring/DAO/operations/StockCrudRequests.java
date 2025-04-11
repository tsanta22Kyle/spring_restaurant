package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.PostgresNextReference;
import org.accdatabase.stockmanager_spring.DAO.mapper.StockMoveMapper;
import org.accdatabase.stockmanager_spring.model.StockMove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.rmi.ServerException;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StockCrudRequests implements CrudRequests<StockMove> {

    @Autowired
    DataSource dataSource;
    @Autowired
    StockMoveMapper stockMoveMapper;
    final PostgresNextReference postgresNextReference = new PostgresNextReference();

    @Override
    public StockMove findById(String id) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public List<StockMove> findAll(int page, int size) {
        throw new UnsupportedOperationException("not implemented yet");

    }

    @SneakyThrows
    @Override
    public List<StockMove> saveAll(List<StockMove> entityToSave) {
        if(entityToSave.isEmpty()){
            System.out.println("empty");
            return List.of();
        }
        List<StockMove> stockMoves = new ArrayList<StockMove>();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("INSERT INTO stock_move(id,ingredient_id, movetype, quantity, unit,move_date) VALUES (?,?,?,?,?,?) ON CONFLICT (id) DO NOTHING RETURNING id, ingredient_id, movetype,quantity,move_date");
        ) {
            entityToSave.forEach(stockMove -> {
                try {
                    String id = stockMove.getId()==null ?postgresNextReference.generateUUID():stockMove.getId();
                    System.out.println("stockmove : "+stockMove);
                    statement.setString(1, stockMove.getId());
                    statement.setString(2, stockMove.getIngredient().getIngredientId());
                    statement.setObject(3, stockMove.getMoveType().name(),Types.OTHER);
                    statement.setDouble(4, stockMove.getQuantity());
                    statement.setObject(5, stockMove.getUnit().name(),Types.OTHER);
                    statement.setTimestamp(6, Timestamp.from(Instant.now()));
                    statement.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMoves.add(stockMoveMapper.apply(resultSet));
                }
            }catch (SQLException e) {
                throw new ServerException(e.getMessage());
            }

        }
        return stockMoves;
    }
    @SneakyThrows
    public List<StockMove> findByIdIngredient(String idIngredient) {
        List<StockMove> stockMovements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select s.id, s.quantity, s.unit, s.movetype, s.move_date from stock_move s"
                             + " join ingredient i on s.ingredient_id = i.ingredient_id"
                             + " where s.ingredient_id = ?")) {
            statement.setString(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMovements.add(stockMoveMapper.apply(resultSet));
                }
                return stockMovements;
            }
        } catch (SQLException e) {
            throw new ServerException(e.getMessage());
        }
    }
}

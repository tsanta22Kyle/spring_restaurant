package org.accdatabase.stockmanager_spring.DAO.mapper;

import lombok.SneakyThrows;

import org.accdatabase.stockmanager_spring.model.MoveType;
import org.accdatabase.stockmanager_spring.model.StockMove;
import org.accdatabase.stockmanager_spring.model.unit;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;
@Component
public class StockMoveMapper implements Function<ResultSet, StockMove> {




    @SneakyThrows
    @Override
    public StockMove apply(ResultSet resultSet) {
        StockMove stockMove = new StockMove();
        System.out.println("resultset : " + resultSet);
        stockMove.setId(resultSet.getString("id"));
        stockMove.setQuantity(resultSet.getDouble("quantity"));
        stockMove.setMoveDate(resultSet.getTimestamp("move_date").toLocalDateTime());
        stockMove.setUnit(unit.valueOf(resultSet.getString("unit")));
        stockMove.setMoveType(MoveType.valueOf(resultSet.getObject("movetype").toString()));
        return stockMove;
    }
}

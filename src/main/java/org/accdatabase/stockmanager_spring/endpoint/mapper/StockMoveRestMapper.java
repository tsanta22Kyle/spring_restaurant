package org.accdatabase.stockmanager_spring.endpoint.mapper;

import org.accdatabase.stockmanager_spring.endpoint.rest.StockMoveRest;
import org.accdatabase.stockmanager_spring.model.StockMove;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class StockMoveRestMapper implements Function<StockMove, StockMoveRest> {
    @Override
    public StockMoveRest apply(StockMove stockMove) {
        return new StockMoveRest(stockMove.getId(),stockMove.getMoveType(),stockMove.getQuantity(),stockMove.getUnit(),stockMove.getMoveDate());
    }
}

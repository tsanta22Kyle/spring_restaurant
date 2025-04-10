package org.accdatabase.stockmanager_spring.DAO.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.operations.PriceCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.StockCrudRequests;
import org.accdatabase.stockmanager_spring.model.Ingredient;
import org.accdatabase.stockmanager_spring.model.Price;
import org.accdatabase.stockmanager_spring.model.StockMove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class IngredientMapper implements Function<ResultSet, Ingredient> {

    private final PriceCrudRequests priceCrudRequests;
    private final StockCrudRequests stockCrudRequests;

    @SneakyThrows
    @Override
    public Ingredient apply(ResultSet resultSet) {
        String idIngredient = resultSet.getString(1);
        List<Price> prices = priceCrudRequests.findByIdIngredient(idIngredient);
        List<StockMove> stockMoves = stockCrudRequests.findByIdIngredient(idIngredient);
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(idIngredient);
        ingredient.setName(resultSet.getString(2));
        ingredient.setPrices(prices);
        ingredient.setStockMoves(stockMoves);
        System.out.println("ingrediend by id :"+ingredient);
        return ingredient;
    }
}

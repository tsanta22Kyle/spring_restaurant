package org.accdatabase.stockmanager_spring.endpoint.mapper;

import org.accdatabase.stockmanager_spring.DAO.operations.IngredientCrudRequests;
import org.accdatabase.stockmanager_spring.endpoint.rest.CreateOrUpdateIngredient;
import org.accdatabase.stockmanager_spring.endpoint.rest.IngredientRest;
import org.accdatabase.stockmanager_spring.endpoint.rest.PriceRest;
import org.accdatabase.stockmanager_spring.endpoint.rest.StockMoveRest;
import org.accdatabase.stockmanager_spring.Service.exception.NotFoundException;
import org.accdatabase.stockmanager_spring.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IngredientRestMapper {

    @Autowired
    PriceRestMapper priceRestMapper;
    @Autowired
    StockMoveRestMapper stockMoveRestMapper;
    @Autowired
    IngredientCrudRequests ingredientCrudRequests;

    public Ingredient toModel(CreateOrUpdateIngredient newIngredient) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(newIngredient.getName());
        ingredient.setIngredientId(newIngredient.getId());
        try {
            Ingredient existingIngredient = ingredientCrudRequests.findById(newIngredient.getId());
            /*if (existingIngredient != null) {
                ingredient.setPrices(new ArrayList<>());
                ingredient.setStockMoves(new ArrayList<>()); alternative de noob
            }*/
            Ingredient unPricedIngredient = new Ingredient();
            unPricedIngredient.setName(existingIngredient.getName());
            unPricedIngredient.setIngredientId(existingIngredient.getIngredientId());
            ingredient.setPrices(existingIngredient.getPrices().stream().map(price -> {
                price.setIngredient(unPricedIngredient);
                return price;
            }).collect(Collectors.toList()));
            ingredient.setStockMoves(existingIngredient.getStockMoves().stream().map(stockMove -> {
                stockMove.setIngredient(unPricedIngredient);
                return stockMove;
            }).collect(Collectors.toList()));

        } catch (NotFoundException e) {
            ingredient.setPrices(List.of());
            ingredient.setStockMoves(List.of());

        }
        return ingredient;

    }

    public IngredientRest toRest(Ingredient ingredient) {
        List<PriceRest> prices = ingredient.getPrices().stream().map(price ->
                priceRestMapper.apply(price)
        ).toList();
        List<StockMoveRest> stockMoves = ingredient.getStockMoves().stream().map(stockMove -> stockMoveRestMapper.apply(stockMove)).toList();

        return new IngredientRest(ingredient.getIngredientId(), ingredient.getName(), prices, stockMoves);

    }


}

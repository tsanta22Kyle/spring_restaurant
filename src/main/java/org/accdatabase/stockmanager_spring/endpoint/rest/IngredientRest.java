package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor@Getter
public class IngredientRest {
    private String id;
    private String name;
    private List<PriceRest> prices;
    private List<StockMoveRest> stockMovements;


}

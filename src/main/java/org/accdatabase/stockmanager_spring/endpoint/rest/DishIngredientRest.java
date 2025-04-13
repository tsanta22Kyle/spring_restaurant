package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.accdatabase.stockmanager_spring.model.unit;

@AllArgsConstructor@Getter
public class DishIngredientRest {
    private String ingredientId;
    private String name;
    private Double actualPrice;
    private Double actualStock;
    private Double requiredQuantity;
    private unit unit;

}

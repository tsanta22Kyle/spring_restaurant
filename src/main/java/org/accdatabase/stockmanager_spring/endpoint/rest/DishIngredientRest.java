package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public class DishIngredientRest {
    private String name;
    private Double actualPrice;
    private Double actualStock;
    private Double requiredQuantity;

}

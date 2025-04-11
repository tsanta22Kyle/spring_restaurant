package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor@Getter
public class DishRest {
    private String name;
    private int price;
    private List<DishIngredientRest> ingredients;
    private double availableQuantity;
    

}

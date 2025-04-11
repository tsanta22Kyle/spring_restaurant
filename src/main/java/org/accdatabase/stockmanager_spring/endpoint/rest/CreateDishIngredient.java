package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.accdatabase.stockmanager_spring.model.unit;

@AllArgsConstructor@Getter
public class CreateDishIngredient {
    private String name;
    private Double quantity;
    private unit unit;
}

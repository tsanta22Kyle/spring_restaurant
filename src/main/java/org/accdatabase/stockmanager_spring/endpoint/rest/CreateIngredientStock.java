package org.accdatabase.stockmanager_spring.endpoint.rest;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.accdatabase.stockmanager_spring.model.MoveType;
import org.accdatabase.stockmanager_spring.model.unit;

@Getter@AllArgsConstructor
public class CreateIngredientStock {
    private MoveType type;
    private Double quantity;
    private unit unit;
}

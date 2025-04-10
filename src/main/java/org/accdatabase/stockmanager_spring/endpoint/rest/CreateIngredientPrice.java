package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor@Getter@NoArgsConstructor
public class CreateIngredientPrice {
    private Double value;
    private LocalDate dateValue;

    public Double getValue() {
        return value;
    }

    public LocalDate getDateValue() {
        return dateValue;
    }
}

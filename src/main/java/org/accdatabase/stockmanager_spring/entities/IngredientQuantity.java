package org.accdatabase.stockmanager_spring.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter@Setter@NoArgsConstructor
public class IngredientQuantity {
    private Ingredient ingredient;
    private double quantity;
    private unit unit;

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public org.accdatabase.stockmanager_spring.entities.unit getUnit() {
        return unit;
    }

    public IngredientQuantity(Ingredient ingredient, double quantity, org.accdatabase.stockmanager_spring.entities.unit unit) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    public double getTotalCost(LocalDate date){
        return quantity*ingredient.getPriceAtDate(date).getValue();
    }
    public double getTotalCost(){
        return quantity*ingredient.getPriceAtDate().getValue();
    }


    @Override
    public String toString() {
        return "\n " + ingredient +
                "\n quantity : " + this.quantity +
                " " + unit ;
    }
}

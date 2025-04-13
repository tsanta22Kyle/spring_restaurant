package org.accdatabase.stockmanager_spring.model;

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

    public org.accdatabase.stockmanager_spring.model.unit getUnit() {
        return unit;
    }

    public IngredientQuantity(Ingredient ingredient, double quantity, org.accdatabase.stockmanager_spring.model.unit unit) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    public double getTotalCost(LocalDate date){
        return quantity*ingredient.getPriceAtDate(date).getValue();
    }
    public double getTotalCost(){
        if(ingredient.getPriceAtDate() != null){

        return quantity*ingredient.getPriceAtDate().getValue();
        }
        return 0.0;
    }


    @Override
    public String toString() {
        return "\n " + ingredient +
                "\n quantity : " + this.quantity +
                " " + unit ;
    }
}

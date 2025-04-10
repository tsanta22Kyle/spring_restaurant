package org.accdatabase.stockmanager_spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor@AllArgsConstructor@Setter@Getter
public class Price {
    private String id;
    private double value;
    private LocalDate date;

    private Ingredient ingredient;

    @Override
    public String toString() {
        return "Price{" +
                "id='" + id + '\'' +
                ", value=" + value +
                ", date=" + date +
                //", ingredient=id" + this.getIngredient().getIngredientId() +
                '}';
    }
}

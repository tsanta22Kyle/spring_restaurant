package org.accdatabase.stockmanager_spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor@AllArgsConstructor@Setter@Getter@ToString@EqualsAndHashCode
public class Price {
    private String id;
    private double value;
    private LocalDate date;

    private Ingredient ingredient;



}

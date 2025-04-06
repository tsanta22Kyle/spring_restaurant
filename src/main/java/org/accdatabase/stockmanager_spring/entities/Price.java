package org.accdatabase.stockmanager_spring.entities;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor@AllArgsConstructor@Setter@Getter
public class Price {
    private String id;
    private double value;
    private LocalDate date;
    private unit unit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public org.accdatabase.stockmanager_spring.entities.unit getUnit() {
        return unit;
    }

    public void setUnit(org.accdatabase.stockmanager_spring.entities.unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return
                " price : " + value +
                        " at " + date +
                        "unit : " + unit ;
    }
}

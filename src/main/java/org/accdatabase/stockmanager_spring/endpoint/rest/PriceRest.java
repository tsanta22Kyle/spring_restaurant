package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@AllArgsConstructor@Getter
public class PriceRest {
    private String id;
    private Double price;
    private LocalDate dateValue;
}

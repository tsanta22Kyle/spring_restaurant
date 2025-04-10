package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.accdatabase.stockmanager_spring.model.MoveType;

import java.time.LocalDateTime;
@AllArgsConstructor@Getter
public class StockMoveRest {

    private String id;
    private MoveType type;
    private double quantity;
    private org.accdatabase.stockmanager_spring.model.unit unit;
    private LocalDateTime creationDatetime;
}

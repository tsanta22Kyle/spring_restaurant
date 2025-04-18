package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.accdatabase.stockmanager_spring.model.OrderStatus;

import java.util.List;

@AllArgsConstructor@Getter
public class CreateOrUpdateDishOrder {
    private String dishId;
    private int quantity;
}

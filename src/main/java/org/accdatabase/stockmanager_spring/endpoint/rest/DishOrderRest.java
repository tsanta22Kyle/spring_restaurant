package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.accdatabase.stockmanager_spring.model.OrderProcess;
import org.accdatabase.stockmanager_spring.model.OrderStatus;


@AllArgsConstructor@Getter
public class DishOrderRest {
    private String name;
    private int price;
    private Integer quantity;
    private OrderProcess actualStatus;


}

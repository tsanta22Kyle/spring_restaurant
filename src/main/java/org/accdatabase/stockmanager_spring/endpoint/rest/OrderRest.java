package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.accdatabase.stockmanager_spring.model.OrderProcess;

import java.util.List;
@AllArgsConstructor@Getter
public class OrderRest {
    private String reference;
    private List<DishOrderRest> dishOrderList;
    private OrderProcess status;
}

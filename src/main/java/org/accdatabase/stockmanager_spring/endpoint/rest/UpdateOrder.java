package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.accdatabase.stockmanager_spring.model.OrderProcess;

import java.util.ArrayList;
import java.util.List;
@Getter@AllArgsConstructor
public class UpdateOrder {
    private List<CreateOrUpdateDishOrder> DishOrderList = new ArrayList<>();
    private OrderProcess orderStatus ;
}

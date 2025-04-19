package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.accdatabase.stockmanager_spring.model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor@Getter@Setter@NoArgsConstructor
public class SaleRest {
    private String dishOrderId;
    private String dishId;
    private String dishName;
    private int quantitySold;
    private List<OrderStatus> statusHistory = new ArrayList<OrderStatus>();
}

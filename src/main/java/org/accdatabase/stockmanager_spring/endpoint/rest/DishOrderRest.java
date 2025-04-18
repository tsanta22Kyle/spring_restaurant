package org.accdatabase.stockmanager_spring.endpoint.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.accdatabase.stockmanager_spring.model.OrderProcess;
import org.accdatabase.stockmanager_spring.model.OrderStatus;


@AllArgsConstructor@Getter
public class DishOrderRest {
    private String id;
    private String name;
    @JsonIgnore
    private int price;
    @JsonProperty("quantityOrdered")
    private Integer quantity;
    @JsonProperty("actualOrderStatus")
    private OrderProcess actualStatus;


}

package org.accdatabase.stockmanager_spring.endpoint.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.accdatabase.stockmanager_spring.model.OrderProcess;

import java.util.List;
@AllArgsConstructor@Getter
public class OrderRest {
    @JsonProperty("id")
    private String reference;
    @JsonProperty("dishes")
    private List<DishOrderRest> dishOrderList;
    @JsonProperty("actualStatus")
    private OrderProcess status;
}

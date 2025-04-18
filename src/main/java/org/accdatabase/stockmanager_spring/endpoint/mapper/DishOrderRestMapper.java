package org.accdatabase.stockmanager_spring.endpoint.mapper;

import org.accdatabase.stockmanager_spring.endpoint.rest.DishOrderRest;
import org.accdatabase.stockmanager_spring.model.DishOrder;
import org.springframework.stereotype.Component;

@Component
public class DishOrderRestMapper {

    public DishOrderRest toRest(DishOrder dishOrder) {
        DishOrderRest dishOrderRest = new DishOrderRest(dishOrder.getDish().getName(),dishOrder.getDish().getUnitPrice(),dishOrder.getQuantity(),dishOrder.getActualStatus().getOrderProcess());
        return dishOrderRest;
    }

}

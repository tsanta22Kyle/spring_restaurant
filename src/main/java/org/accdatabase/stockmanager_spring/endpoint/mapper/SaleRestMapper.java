package org.accdatabase.stockmanager_spring.endpoint.mapper;

import org.accdatabase.stockmanager_spring.endpoint.rest.SaleRest;
import org.accdatabase.stockmanager_spring.model.DishOrder;
import org.springframework.stereotype.Component;

@Component
public class SaleRestMapper {
    public SaleRest toRest(DishOrder dishOrder){
        SaleRest saleRest = new SaleRest();
        saleRest.setDishOrderId(dishOrder.getDishOrderId());
        saleRest.setDishId(dishOrder.getDish().getDishId());
        saleRest.setDishName(dishOrder.getDish().getName());
        saleRest.setQuantitySold(dishOrder.getQuantity());
        saleRest.setStatusHistory(dishOrder.getStatusList());
        return saleRest;
    }
}

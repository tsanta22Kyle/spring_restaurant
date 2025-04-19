package org.accdatabase.stockmanager_spring.endpoint.mapper;

import lombok.RequiredArgsConstructor;
import org.accdatabase.stockmanager_spring.endpoint.rest.DishOrderRest;
import org.accdatabase.stockmanager_spring.endpoint.rest.OrderRest;
import org.accdatabase.stockmanager_spring.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class OrderRestMapper {
    private final DishOrderRestMapper dishOrderRestMapper;

    public OrderRest toRest(Order order) {
        List<DishOrderRest> orderRests = order.getDishOrderList().stream().map(dishOrder -> dishOrderRestMapper.toRest(dishOrder)).toList();
    OrderRest orderRest = new OrderRest(order.getId(),orderRests,order.getActualStatus());
      // System.out.println("dish orders : "+order.getDishOrders());
    return orderRest;
    }

}

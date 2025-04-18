package org.accdatabase.stockmanager_spring.Service;

import org.accdatabase.stockmanager_spring.DAO.operations.DishCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.DishOrderCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.OrderCrudRequests;
import org.accdatabase.stockmanager_spring.endpoint.mapper.OrderRestMapper;
import org.accdatabase.stockmanager_spring.endpoint.rest.OrderRest;
import org.accdatabase.stockmanager_spring.endpoint.rest.UpdateOrder;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.accdatabase.stockmanager_spring.model.DishOrder;
import org.accdatabase.stockmanager_spring.model.Order;
import org.accdatabase.stockmanager_spring.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderCrudRequests orderCrudRequests;
    @Autowired
    private OrderRestMapper orderRestMapper;
    @Autowired
    private DishOrderCrudRequests dishOrderCrudRequests;
    @Autowired
    private DishCrudRequests dishCrudRequests;

    public Optional<Object> getOrderByRef(String ref) {
        OrderRest orderRest = orderRestMapper.toRest(orderCrudRequests.findById(ref));
        return Optional.ofNullable(orderRest);
    }

    public Optional<Object> updateOrderDishes(String reference, UpdateOrder order) {
        Order order1 = orderCrudRequests.findById(reference);

        List<DishOrder> dishOrders = order.getDishOrderList().stream().map(createOrUpdateDishOrder -> {
            Dish dish = dishCrudRequests.findById(createOrUpdateDishOrder.getDishId());
            DishOrder dishOrder = new DishOrder();
            dishOrder.setQuantity(createOrUpdateDishOrder.getQuantity());
           // System.out.println("dish : "+dish);
            dishOrder.setDish(dish);
            return dishOrder;
        }).toList();
        order1.addDishOrder(dishOrders);
        return Optional.of(dishOrderCrudRequests.saveAll(dishOrders));
    }
}

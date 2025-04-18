package org.accdatabase.stockmanager_spring.Service;

import org.accdatabase.stockmanager_spring.DAO.operations.DishCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.DishOrderCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.OrderCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.OrderStatusCrudRequests;
import org.accdatabase.stockmanager_spring.endpoint.mapper.OrderRestMapper;
import org.accdatabase.stockmanager_spring.endpoint.rest.OrderRest;
import org.accdatabase.stockmanager_spring.endpoint.rest.UpdateOrder;
import org.accdatabase.stockmanager_spring.model.*;
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
    @Autowired
    private OrderStatusCrudRequests orderStatusCrudRequests;

    public Optional<Object> getOrderByRef(String ref) {
        OrderRest orderRest = orderRestMapper.toRest(orderCrudRequests.findById(ref));
        return Optional.ofNullable(orderRest);
    }

    public Optional<Object> updateOrderDishes(String reference, UpdateOrder order) {
        Order order1 = orderCrudRequests.findById(reference);
        OrderProcess orderStatus = order.getOrderStatus();

        if(orderStatus==OrderProcess.CONFIRMED){
            List<DishOrder> dishOrderList =  order1.getDishOrders();
         boolean isConfirmed =  dishOrderList.stream().filter(dishOrder -> dishOrder.getActualStatus().getOrderProcess() == OrderProcess.CONFIRMED  ).toList().size() ==  dishOrderList.size() ;
            System.out.println("isConfirmed : "+isConfirmed);
         if(isConfirmed){
             OrderStatus newOrderStatus = new OrderStatus(orderStatus);
            order1.updateStatus(newOrderStatus);
            orderStatusCrudRequests.saveAllOrderStatus(order1.getId(),List.of(newOrderStatus));
         }
        }

        List<DishOrder> dishOrders = order.getDishOrderList().stream().map(createOrUpdateDishOrder -> {
            Dish dish = dishCrudRequests.findById(createOrUpdateDishOrder.getDishId());
            DishOrder dishOrder = new DishOrder();
            dishOrder.setQuantity(createOrUpdateDishOrder.getQuantity());
           // System.out.println("dish : "+dish);
            dishOrder.setDish(dish);
            return dishOrder;
        }).toList();
        order1.addDishOrder(dishOrders);
        dishOrderCrudRequests.saveAll(dishOrders);
        Order updatedOrder = orderCrudRequests.findById(reference);
       // System.out.println(updatedOrder.getDishOrders());
        return Optional.of(orderRestMapper.toRest(updatedOrder));
    }
}

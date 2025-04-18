package org.accdatabase.stockmanager_spring.DAO.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.operations.DishOrderCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.OrderStatusCrudRequests;
import org.accdatabase.stockmanager_spring.model.Order;
import org.accdatabase.stockmanager_spring.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class OrderMapper implements Function<ResultSet, Order> {
    private final DishOrderCrudRequests dishOrderCrudRequests;
    private final OrderStatusCrudRequests orderStatusCrudRequests;



    @SneakyThrows
    @Override
    public Order apply(ResultSet resultSet) {
        String orderId = resultSet.getString("order_id");
      //  System.out.println("orderid: " + orderId);
        Order order = new Order();
       List<OrderStatus> statusList = orderStatusCrudRequests.getOrderStatusByOrderId(orderId);
        order.setOrderDatetime(resultSet.getTimestamp("order_time").toLocalDateTime());
        order.setId(orderId);
        order.setReference(resultSet.getString("reference"));
        order.setDishOrderList(dishOrderCrudRequests.getDishOrdersByOrderId(orderId));
       order.setStatusList(statusList);

        return order;
    }
}

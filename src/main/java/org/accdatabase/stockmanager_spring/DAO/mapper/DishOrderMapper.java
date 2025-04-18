package org.accdatabase.stockmanager_spring.DAO.mapper;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.operations.DishCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.OrderCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.OrderStatusCrudRequests;
import org.accdatabase.stockmanager_spring.model.DishOrder;
import org.accdatabase.stockmanager_spring.model.Order;
import org.accdatabase.stockmanager_spring.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;
@Component
public class DishOrderMapper implements Function<ResultSet, DishOrder> {

    @Autowired
    private DishCrudRequests dishCrudRequests;
    //@Autowired
  //  private OrderCrudRequests orderCrudRequests;
    @Autowired
    private OrderStatusCrudRequests orderStatusCrudRequests;

    @SneakyThrows
    @Override
    public DishOrder apply(ResultSet resultSet) {
        DishOrder dishOrder = new DishOrder();
        String id = resultSet.getString("id");
      // Order returnedOrder =  orderCrudRequests.findByOrderId(resultSet.getString("order_id"));
        List<OrderStatus> orderStatusList = orderStatusCrudRequests.getOrderStatusByDishOrderId(id);
        dishOrder.setDishOrderId(id);
        dishOrder.setQuantity(resultSet.getInt("quantity"));

        dishOrder.setDish(dishCrudRequests.findById(resultSet.getString("dish_id")));
      //  System.out.println(resultSet.getString("dish_id"));
       // dishOrder.setOrder();
        dishOrder.setStatusList(orderStatusList);
        return dishOrder;
    }
}

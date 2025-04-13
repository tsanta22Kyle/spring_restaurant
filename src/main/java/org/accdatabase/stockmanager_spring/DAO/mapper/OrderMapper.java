package org.accdatabase.stockmanager_spring.DAO.mapper;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.model.Order;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;


@Component
public class OrderMapper implements Function<ResultSet, Order> {
    @SneakyThrows
    @Override
    public Order apply(ResultSet resultSet) {
        Order order = new Order();
        order.setOrderDatetime(resultSet.getTimestamp("order_time").toLocalDateTime());
        order.setId(resultSet.getString("order_id"));
        order.setReference(resultSet.getString("reference"));
        //order.setDishOrderList();

        return order;
    }
}

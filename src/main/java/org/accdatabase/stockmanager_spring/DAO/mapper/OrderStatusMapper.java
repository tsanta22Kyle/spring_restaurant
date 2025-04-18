package org.accdatabase.stockmanager_spring.DAO.mapper;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.model.OrderProcess;
import org.accdatabase.stockmanager_spring.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class OrderStatusMapper implements Function<ResultSet, OrderStatus> {
    @SneakyThrows
    @Override
    public OrderStatus apply(ResultSet resultSet) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderProcess(OrderProcess.valueOf(resultSet.getObject("order_status").toString()));
        orderStatus.setId(resultSet.getString("id"));
        orderStatus.setDishOrderStatusDatetime(resultSet.getTimestamp("order_status_datetime").toLocalDateTime());
        return orderStatus;
    }
}

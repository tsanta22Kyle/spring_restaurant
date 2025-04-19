package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.mapper.DishOrderStatusMapper;
import org.accdatabase.stockmanager_spring.DAO.mapper.OrderStatusMapper;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class OrderStatusCrudRequests {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private DishOrderStatusMapper dishOrderStatusMapper;

    @SneakyThrows
    public List<OrderStatus> getOrderStatusByOrderId(String orderId) {
        List<OrderStatus> orderStatuses = new ArrayList<OrderStatus>();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("SELECT id, order_id, order_status, order_status_datetime FROM order_status WHERE order_id=?");
        ) {
            statement.setString(1, orderId);
            try (
                    ResultSet rs = statement.executeQuery();
            ) {
                while (rs.next()) {

                    orderStatuses.add(orderStatusMapper.apply(rs));
                }
            }

        }
        return orderStatuses;
    }

    @SneakyThrows
    public List<OrderStatus> getOrderStatusByDishOrderId(String dishOrderId) {
       // System.out.println();
        List<OrderStatus> orderStatuses = new ArrayList<OrderStatus>();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("SELECT id, dish_order_id, order_status, do_datetime FROM dish_order_status WHERE dish_order_id=?");
        ) {
            statement.setString(1, dishOrderId);
            try (
                    ResultSet rs = statement.executeQuery();
            ) {

                while (rs.next()) {

                    orderStatuses.add(dishOrderStatusMapper.apply(rs));
                }
            }

        }
        return orderStatuses;
    }

    @SneakyThrows
    public List<OrderStatus> saveAllOrderStatus(String orderId, List<OrderStatus> orderStatusToSave) {
        List<OrderStatus> createdOrderStatusList = new ArrayList<>();
        try (
                Connection conn = dataSource.getConnection()
        ) {
            orderStatusToSave.forEach(orderStatus -> {
                try (
                        PreparedStatement statement = conn.prepareStatement("INSERT INTO order_status (id, order_id, order_status, order_status_datetime) VALUES (?,?,?,?) RETURNING id,order_id,order_status,order_status_datetime")
                ) {
                    String id = orderStatus.getId() == null ? UUID.randomUUID().toString() : orderStatus.getId();
                    LocalDateTime dateTime = orderStatus.getDishOrderStatusDatetime()==null ?LocalDateTime.now():orderStatus.getDishOrderStatusDatetime();
                    //System.out.println(id);
                    statement.setString(1, id);
                    statement.setString(2, orderId);
                    System.out.println("order process invalid : "+orderStatus.getOrderProcess());
                    statement.setObject(3, orderStatus.getOrderProcess(), Types.OTHER);
                    statement.setTimestamp(4, Timestamp.valueOf(dateTime));

                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()){

                        createdOrderStatusList.add(orderStatusMapper.apply(rs));
                        }
                    } catch (SQLException e) {
                        throw new ServerException(e);
                    }
                } catch (SQLException e) {
                    throw new ServerException(e);
                }
            });

        }
        return createdOrderStatusList;
    }

    @SneakyThrows
    public List<OrderStatus> saveAllDishOrderStatus(String dishOrderId, List<OrderStatus> dishOrderStatusToSave) {
        List<OrderStatus> createdOrderStatusList = new ArrayList<>();
        try (
                Connection conn = dataSource.getConnection()
        ) {
            dishOrderStatusToSave.forEach(orderStatus -> {
                try (
                        PreparedStatement statement = conn.prepareStatement("INSERT INTO dish_order_status (id, dish_order_id, order_status, do_datetime) VALUES (?,?,?,?) RETURNING id,dish_order_id,order_status,do_datetime")
                ) {
                    String id = orderStatus.getId() == null ? UUID.randomUUID().toString() : orderStatus.getId();
                    LocalDateTime dateTime = orderStatus.getDishOrderStatusDatetime()==null?LocalDateTime.now():orderStatus.getDishOrderStatusDatetime();
                    // System.out.println(id);
                    statement.setString(1, id);
                    statement.setString(2, dishOrderId);
                    //System.out.println("process : "+orderStatus.getOrderProcess());
                    System.out.println("order process invalid : "+orderStatus.getOrderProcess());
                    statement.setObject(3, orderStatus.getOrderProcess(), Types.OTHER);
                    statement.setTimestamp(4, Timestamp.valueOf(dateTime));

                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            createdOrderStatusList.add(dishOrderStatusMapper.apply(rs));
                        }
                    } catch (SQLException e) {
                        throw new ServerException(e);
                    }
                } catch (SQLException e) {
                    throw new ServerException(e);
                }
            });

        }
        return createdOrderStatusList;
    }

}

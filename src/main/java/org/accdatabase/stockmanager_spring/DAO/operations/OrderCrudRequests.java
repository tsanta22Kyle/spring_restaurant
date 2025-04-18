package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.mapper.OrderMapper;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class OrderCrudRequests implements CrudRequests<Order>{

    @Autowired
    private DataSource dataSource;
    @Autowired
    private OrderMapper orderMapper;


    @SneakyThrows
    @Override
    public Order findById(String reference) {
        Order order = null;
        try(
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("select order_id,reference,order_time from \"order\" where reference = ?");
                )
        {
            System.out.println("ref :"+reference);
            statement.setString(1, reference);
            try (ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                order = orderMapper.apply(rs);
                }
            }catch (SQLException e){
                throw new ServerException(e);
            }
        }
    return order;
    }@SneakyThrows

    public Order findByOrderId(String orderId) {
        Order order = null;
        try(
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("select order_id,reference,order_time from \"order\" where order_id = ?");
                )
        {
            statement.setString(1, orderId);
            try (ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                order = orderMapper.apply(rs);
                }
            }catch (SQLException e){
                throw new ServerException(e);
            }
        }
    return order;
    }

    @Override
    public List<Order> findAll(int page, int size) {
    throw new RuntimeException("not implemented yet");
    }
    @SneakyThrows
    @Override
    public List<Order> saveAll(List<Order> entityToSave) {
        List<Order> savedOrders = new ArrayList<>();
        try (
                Connection conn = dataSource.getConnection()
                ){
            entityToSave.forEach(orderToSave -> {
                try (
                        PreparedStatement statement = conn.prepareStatement("INSERT INTO \"order\" (reference, order_time, order_id) VALUES (?,?,?,?) ON CONFLICT (reference) DO UPDATE SET order_time=excluded.order_time RETURNING order_time,reference,order_id")
                        ){
                    statement.setString(1, orderToSave.getReference());
                    statement.setTimestamp(2, Timestamp.valueOf(orderToSave.getOrderDatetime()));
                    statement.setString(3,orderToSave.getId());

                    try (ResultSet rs = statement.executeQuery()){
                        while (rs.next()) {
                            savedOrders.add(orderMapper.apply(rs));
                        }
                    }catch (SQLException e){
                        throw new ServerException(e);
                    }
                }catch (SQLException e){
                    throw new ServerException(e);
                }
            });


        }
        return savedOrders;
    }
}

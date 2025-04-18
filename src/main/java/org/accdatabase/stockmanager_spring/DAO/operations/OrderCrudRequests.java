package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.mapper.OrderMapper;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.endpoint.rest.UpdateDishOrderStatus;
import org.accdatabase.stockmanager_spring.model.Order;
import org.accdatabase.stockmanager_spring.model.OrderProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
           // System.out.println("ref :"+reference);
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
                        PreparedStatement statement = conn.prepareStatement("INSERT INTO \"order\" (reference, order_time, order_id) VALUES (?,?,?) ON CONFLICT (reference) DO UPDATE SET order_time=excluded.order_time RETURNING order_time,reference,order_id")
                        ){

                    LocalDateTime timestamp = orderToSave.getOrderDatetime()==null?LocalDateTime.now():orderToSave.getOrderDatetime();
                    String id = orderToSave.getId()==null? UUID.randomUUID().toString():orderToSave.getId();
                    statement.setString(1, orderToSave.getReference());
                    statement.setTimestamp(2, Timestamp.valueOf(timestamp));
                    statement.setString(3,id);


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

    public List<Order> findSales() {
        List<Order> orders = new ArrayList<>();
        try(
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("select o.order_id,reference,order_time from \"order\" o join public.order_status os on o.order_id = os.order_id where os.order_status = ?  ")
        )
        {
         statement.setObject(1, OrderProcess.DELIVERED.toString(),Types.OTHER);
            try (ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                     orders.add(orderMapper.apply(rs));
                }
            }catch (SQLException e){
                throw new ServerException(e);
            }
        }catch (SQLException e){
            throw new ServerException(e);
        }
        return orders;
    }
}

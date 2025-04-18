package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.mapper.OrderMapper;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return List.of();
    }

    @Override
    public List<Order> saveAll(List<Order> entityToSave) {
        return List.of();
    }
}

package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.mapper.OrderMapper;
import org.accdatabase.stockmanager_spring.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        try(
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("select (order_id,reference,order_time) from order where reference = ?");
                )
        {
            statement.setString(1, reference);
            try (ResultSet rs = statement.executeQuery()){
                return orderMapper.apply(rs);
            }
        }

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

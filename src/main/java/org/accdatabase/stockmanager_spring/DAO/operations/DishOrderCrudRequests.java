package org.accdatabase.stockmanager_spring.DAO.operations;

import lombok.SneakyThrows;
import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.mapper.DishOrderMapper;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.model.DishOrder;
import org.accdatabase.stockmanager_spring.model.OrderProcess;
import org.accdatabase.stockmanager_spring.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class DishOrderCrudRequests {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DishOrderMapper dishOrderMapper;
    @Autowired
    private OrderStatusCrudRequests orderStatusCrudRequests;


    @SneakyThrows
    public List<DishOrder> getDishOrdersByRef(String reference){
            List<DishOrder> dishOrders = new ArrayList<>();
            try(
                    Connection conn = dataSource.getConnection();
                    PreparedStatement stmt = conn.prepareStatement("SELECT dish_id,o.order_id,quantity,dish_order.id FROM dish_order join public.\"order\" o on o.order_id = dish_order.order_id  WHERE o.reference= ?")
                    ){
                //System.out.println(orderId);
                stmt.setString(1,reference);
                try(ResultSet rs = stmt.executeQuery()){
                    while(rs.next()){
                        dishOrders.add(dishOrderMapper.apply(rs));
                    }
                }catch (SQLException e){
                    throw new ServerException(e);
                }

            }
            return dishOrders;
    } @SneakyThrows
    public List<DishOrder> getDishOrdersByOrderId(String id){
            List<DishOrder> dishOrders = new ArrayList<>();
            try(
                    Connection conn = dataSource.getConnection();
                    PreparedStatement stmt = conn.prepareStatement("SELECT dish_id,o.order_id,quantity,dish_order.id FROM dish_order join public.\"order\" o on o.order_id = dish_order.order_id  WHERE o.order_id= ?")
                    ){
                //System.out.println(orderId);
                stmt.setString(1,id);
                try(ResultSet rs = stmt.executeQuery()){
                    while(rs.next()){
                        dishOrders.add(dishOrderMapper.apply(rs));
                    }
                }catch (SQLException e){
                    throw new ServerException(e);
                }

            }
            return dishOrders;
    }


    @SneakyThrows
    public List<DishOrder> getDishOrdersByDishId(String dish_id){
        List<DishOrder> dishOrders = new ArrayList<>();
        try(
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement("select dish_id,order_id,quantity,id from dish_order where dish_id=?");
        ){
            stmt.setString(1,dish_id);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    dishOrders.add(dishOrderMapper.apply(rs));
                }
            }catch (SQLException e){
                throw new ServerException(e);
            }

        }
        return dishOrders;
    }

    @SneakyThrows
    public List<DishOrder> saveAll(List<DishOrder> dishOrderToSave){
        List<DishOrder> dishOrders = new ArrayList<>();
        try(
                Connection conn = dataSource.getConnection()
                )
        {
            dishOrderToSave.forEach(dishOrder -> {
                try(
                        PreparedStatement statement = conn.prepareStatement("INSERT INTO dish_order(id, order_id, dish_id, quantity) VALUES (?,?,?,?) ON CONFLICT (id) DO UPDATE SET quantity=excluded.quantity RETURNING id,quantity,order_id,dish_id")
                ){
                    String id = dishOrder.getDishOrderId()==null? UUID.randomUUID().toString() :dishOrder.getDishOrderId();

                    statement.setString(1,id);
                    statement.setString(2,dishOrder.getOrder().getId());
                    statement.setString(3,dishOrder.getDish().getDishId());
                    statement.setInt(4,dishOrder.getQuantity());
                    OrderStatus created =  new OrderStatus();
                    created.setOrderProcess(OrderProcess.CREATED);
                    created.setDishOrderStatusDatetime(LocalDateTime.now());
                    try(ResultSet rs = statement.executeQuery()){
                        if(rs.next()){
                            dishOrders.add(dishOrderMapper.apply(rs));
                        }
                    orderStatusCrudRequests.saveAllDishOrderStatus(id,List.of(created));
                    }catch (SQLException e){
                        throw new ServerException(e);
                    }

                }catch (SQLException e){
                    throw new ServerException(e);
                }
            });
        }
        return dishOrders;


    }


}

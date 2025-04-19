package org.accdatabase.stockmanager_spring.Service;

import org.accdatabase.stockmanager_spring.DAO.operations.DishCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.DishOrderCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.OrderCrudRequests;
import org.accdatabase.stockmanager_spring.DAO.operations.OrderStatusCrudRequests;
import org.accdatabase.stockmanager_spring.Service.exception.ClientException;
import org.accdatabase.stockmanager_spring.Service.exception.NotFoundException;
import org.accdatabase.stockmanager_spring.endpoint.mapper.OrderRestMapper;
import org.accdatabase.stockmanager_spring.endpoint.mapper.SaleRestMapper;
import org.accdatabase.stockmanager_spring.endpoint.rest.*;
import org.accdatabase.stockmanager_spring.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderCrudRequests orderCrudRequests;
    @Autowired
    private OrderRestMapper orderRestMapper;
    @Autowired
    private DishOrderCrudRequests dishOrderCrudRequests;
    @Autowired
    private DishCrudRequests dishCrudRequests;
    @Autowired
    private OrderStatusCrudRequests orderStatusCrudRequests;
    @Autowired
    private SaleRestMapper saleRestMapper;

    public Optional<Object> getOrderByRef(String ref) {
        OrderRest orderRest = orderRestMapper.toRest(orderCrudRequests.findById(ref));
        return Optional.ofNullable(orderRest);
    }

    public Optional<Object> updateOrderDishes(String reference, UpdateOrder order) {
        Order order1 = orderCrudRequests.findById(reference);
        OrderProcess orderStatus = order.getOrderStatus();

        try {
            switch (orderStatus) {
                case CONFIRMED -> {
                    boolean isConfirmed = order1.getDishOrders().stream()
                            .allMatch(dishOrder -> dishOrder.getActualStatus().getOrderProcess() == OrderProcess.CONFIRMED);
                    if (isConfirmed) {
                        OrderStatus newOrderStatus = new OrderStatus(orderStatus);
                        order1.updateStatus(newOrderStatus);
                        orderStatusCrudRequests.saveAllOrderStatus(order1.getId(), List.of(newOrderStatus));
                    }
                }
                case IN_PREPARATION -> {
                    boolean isPrepared = order1.getDishOrders().stream()
                            .allMatch(dishOrder -> dishOrder.getActualStatus().getOrderProcess() == OrderProcess.IN_PREPARATION);
                    if (isPrepared) {
                        OrderStatus newOrderStatus = new OrderStatus(orderStatus);
                        order1.updateStatus(newOrderStatus);
                        orderStatusCrudRequests.saveAllOrderStatus(order1.getId(), List.of(newOrderStatus));
                    }
                }
                case FINISHED -> {
                    boolean isFinished = order1.getDishOrders().stream()
                            .allMatch(dishOrder -> dishOrder.getActualStatus().getOrderProcess() == OrderProcess.FINISHED);
                    if (isFinished) {
                        OrderStatus newOrderStatus = new OrderStatus(orderStatus);
                        order1.updateStatus(newOrderStatus);
                        orderStatusCrudRequests.saveAllOrderStatus(order1.getId(), List.of(newOrderStatus));
                    }
                }
                case DELIVERED -> {
                    boolean isDelivered = order1.getDishOrders().stream()
                            .allMatch(dishOrder -> dishOrder.getActualStatus().getOrderProcess() == OrderProcess.DELIVERED);
                    if (isDelivered) {
                        OrderStatus newOrderStatus = new OrderStatus(orderStatus);
                        order1.updateStatus(newOrderStatus);
                        orderStatusCrudRequests.saveAllOrderStatus(order1.getId(), List.of(newOrderStatus));
                    }
                }
                default -> System.out.println("Unhandled order status: " + orderStatus);
            }
        } catch (ClientException e) {
            System.out.println("Erreur lors de la mise à jour du status de la commande : " + e.getMessage());
        }

        // On essaie d'ajouter les plats même si le statut a échoué
        List<DishOrder> dishOrders = new ArrayList<>();
        for (CreateOrUpdateDishOrder createOrUpdateDishOrder : order.getDishOrderList()) {
            try {
                Dish dish = dishCrudRequests.findById(createOrUpdateDishOrder.getDishId());
                DishOrder dishOrder = new DishOrder();
                dishOrder.setQuantity(createOrUpdateDishOrder.getQuantity());
                dishOrder.setDish(dish);
                dishOrders.add(dishOrder);
            } catch (Exception e) {
                System.out.println("Erreur lors de la récupération du plat : " + createOrUpdateDishOrder.getDishId() + " -> " + e.getMessage());
            }
        }

        if (!dishOrders.isEmpty()) {
            try {
                order1.addDishOrder(dishOrders);
                dishOrderCrudRequests.saveAll(dishOrders);
            } catch (Exception e) {
                System.out.println("Erreur lors de l'enregistrement des DishOrders : " + e.getMessage());
            }
        }

        Order updatedOrder = orderCrudRequests.findById(reference);
        return Optional.of(orderRestMapper.toRest(updatedOrder));
    }


    public Optional<Object> createOrder(String reference) {

        Order orderExists = orderCrudRequests.findById(reference);
        if(orderExists!=null){
            throw new ClientException("this order already exists "+orderExists);
        }
        Order orderToSave = new Order();
        orderToSave.setReference(reference);
        List<Order> savedOrders = orderCrudRequests.saveAll(List.of(orderToSave));
        return Optional.ofNullable (  orderRestMapper.toRest(savedOrders.get(0)));
    }

    public Optional<Object> updateDishesStatus(String reference, String dishId, UpdateDishOrderStatus entity) {
        if (reference == null || dishId == null) {
            throw new ClientException("Reference and dish id must be defined");
        }
    Order order = orderCrudRequests.findById(reference);
        if(order==null){
            throw new NotFoundException("order"+reference+" not found");
        }

       List<DishOrder> dishOrdersToUpdate =  dishOrderCrudRequests.getDishOrdersByRef(reference).stream().filter(dishOrder -> dishOrder.getDish().getDishId().equals(dishId)).toList();
        //System.out.println("dish Orders to update"+dishOrdersToUpdate);
        dishOrdersToUpdate.forEach(dishOrder -> {
            try {

          //  System.out.println("io le boucle");
            System.out.println("dish order id : "+dishOrder.getDishOrderId());
            dishOrder.updateActualStatus(new OrderStatus(entity.getOrderStatus()));
         orderStatusCrudRequests.saveAllDishOrderStatus(dishOrder.getDishOrderId(),List.of(new OrderStatus(entity.getOrderStatus())));
           // System.out.println("actualStatus : "+dishOrder.getActualStatus());
            }catch (ClientException e){
                System.out.println((" error : "+e.getMessage()));
            }
        });
      //  System.out.println("order : "+order);
    updateOrderDishes(order.getReference(),new UpdateOrder(List.of(),entity.getOrderStatus()));

        OrderRest orderRest = orderRestMapper.toRest(order);
        return Optional.of(orderRest);
    }

    public Optional<Object> getSalesDetails() {
        List<Order> orderSales = orderCrudRequests.findSales();
        List<SaleRest> saleRests = new ArrayList<>() ;
        orderSales.forEach(order -> {
            order.getDishOrders().forEach(dishOrder -> {
            saleRests.add(saleRestMapper.toRest(dishOrder));
            });
        });
        return Optional.ofNullable(saleRests);
    }
}

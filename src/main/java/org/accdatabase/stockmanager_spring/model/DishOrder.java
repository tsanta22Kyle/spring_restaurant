package org.accdatabase.stockmanager_spring.model;

import lombok.*;
import org.accdatabase.stockmanager_spring.Service.exception.ClientException;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter@ToString
public class DishOrder {
    private String dishOrderId;
    private int quantity;
    private List<OrderStatus> statusList = new ArrayList<>();
    private Dish dish;
    private Order order;


    //une commande de plat zany ty
    public DishOrder(String dishOrderId, int quantity, List<OrderStatus> statusList, Dish dish) {
        if (quantity > dish.getAvailableQuantity()) {

            List<IngredientQuantity> desiredIngredients = this.dish.getIngredientList();
            List<IngredientQuantity> MissingIngredients = this.dish.getIngredientList();

            desiredIngredients.stream().forEach(ingredientQuantity -> {

                double availableQuantity = ingredientQuantity.getIngredient().getStockQuantityAt().getQuantity();
                double necessaryQuantity = ingredientQuantity.getQuantity() * quantity;
                double missingQuantity = necessaryQuantity - availableQuantity;
                MissingIngredients.add(new IngredientQuantity(ingredientQuantity.getIngredient(), missingQuantity, ingredientQuantity.getUnit()));
            });

            throw new ClientException(" not available quantity "+ MissingIngredients);
        }
       else {

            this.dishOrderId = dishOrderId;
            this.quantity = quantity;
            this.statusList = statusList;
            this.dish = dish;
            //this.orderId = orderId;
        }
    }

    public OrderStatus getActualStatus() {
        if(statusList.isEmpty()) {
            return new OrderStatus(OrderProcess.CREATED);
        }
        return statusList.stream().sorted((o1, o2) -> o1.getDishOrderStatusDatetime().compareTo(o2.getDishOrderStatusDatetime())).toList().getLast();
    }
    public OrderStatus updateActualStatus(OrderStatus newStatus) {
        OrderProcess oldStatus = this.getActualStatus().getOrderProcess();
        OrderProcess newOrderStatus = newStatus.getOrderProcess();

        switch (oldStatus) {
            case CREATED -> {
                switch (newOrderStatus) {
                    case CREATED -> throw new ClientException("DishOrder status already created");
                    case CONFIRMED -> this.statusList.add(newStatus);
                    case IN_PREPARATION, FINISHED, DELIVERED ->
                            throw new ClientException("DishOrder not confirmed yet");
                }
            }

            case CONFIRMED -> {
                switch (newOrderStatus) {
                    case CREATED, CONFIRMED ->
                            throw new ClientException("DishOrder status already confirmed");
                    case IN_PREPARATION -> this.statusList.add(newStatus);
                    case FINISHED, DELIVERED ->
                            throw new ClientException("DishOrder not in preparation yet");
                }
            }

            case IN_PREPARATION -> {
                switch (newOrderStatus) {
                    case CREATED, CONFIRMED, IN_PREPARATION ->
                            throw new ClientException("DishOrder already in preparation");
                    case FINISHED -> this.statusList.add(newStatus);
                    case DELIVERED ->
                            throw new ClientException("DishOrder not finished yet");
                }
            }

            case FINISHED -> {
                switch (newOrderStatus) {
                    case CREATED, CONFIRMED, IN_PREPARATION, FINISHED ->
                            throw new ClientException("DishOrder already finished");
                    case DELIVERED -> this.statusList.add(newStatus);
                }
            }

            case DELIVERED -> {
                switch (newOrderStatus) {
                    case CREATED, CONFIRMED, IN_PREPARATION, FINISHED, DELIVERED ->
                            throw new ClientException("DishOrder already delivered");
                }
            }
        }

        return newStatus;
    }





}

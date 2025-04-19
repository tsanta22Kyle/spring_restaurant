package org.accdatabase.stockmanager_spring.endpoint;


import org.accdatabase.stockmanager_spring.Service.OrderService;
import org.accdatabase.stockmanager_spring.Service.exception.ClientException;
import org.accdatabase.stockmanager_spring.Service.exception.NotFoundException;
import org.accdatabase.stockmanager_spring.Service.exception.ServerException;
import org.accdatabase.stockmanager_spring.endpoint.rest.CreateOrder;
import org.accdatabase.stockmanager_spring.endpoint.rest.UpdateDishOrderStatus;
import org.accdatabase.stockmanager_spring.endpoint.rest.UpdateOrder;
import org.accdatabase.stockmanager_spring.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{reference}")
    public ResponseEntity<Object> getOrder(@PathVariable String reference) {
    try {
        return ResponseEntity.ok(orderService.getOrderByRef(reference));
    }catch (ClientException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }catch (NotFoundException e){
        return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
    }catch (ServerException e){
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
    }




    @PutMapping("/{reference}/dishes")
    public ResponseEntity<Object> updateOrderDishes(@PathVariable String reference, @RequestBody UpdateOrder order) {
        try{
        return ResponseEntity.ok(orderService.updateOrderDishes(reference,order));
        }catch (ClientException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (NotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }catch (ServerException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{reference}")
    public ResponseEntity<Object> createOrder(@PathVariable String reference) {
        try {
        return ResponseEntity.ok(orderService.createOrder(reference));
        }catch (ClientException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (NotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }catch (ServerException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{reference}/dishes/{dishId}")
    public ResponseEntity<Object> updateDishStatus(@PathVariable(name = "reference") String reference, @PathVariable(name = "dishId") String dishId, @RequestBody UpdateDishOrderStatus entity) {
        try{
        return ResponseEntity.ok(orderService.updateDishesStatus(reference, dishId, entity));
        }catch (ClientException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (NotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }catch (ServerException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/sales")
    public ResponseEntity<Object> getSalesDetails(){

        try {
            return ResponseEntity.ok(orderService.getSalesDetails());
        }catch (ClientException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (NotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }catch (ServerException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}

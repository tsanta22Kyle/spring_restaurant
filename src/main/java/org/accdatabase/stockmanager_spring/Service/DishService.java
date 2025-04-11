package org.accdatabase.stockmanager_spring.Service;


import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.operations.DishCrudRequests;
import org.accdatabase.stockmanager_spring.Service.exception.ClientException;
import org.accdatabase.stockmanager_spring.endpoint.mapper.DishRestMapper;
import org.accdatabase.stockmanager_spring.endpoint.rest.DishRest;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {


    @Autowired
    private DishCrudRequests dishCrudRequests;
    @Autowired
    private DishRestMapper dishRestMapper;


    public Optional<Object> getAll(int page, int size) {
        if(page<0 || size <0){
            throw new ClientException("page and size should be positive");
        }

    List<DishRest> dishRests = dishCrudRequests.findAll(page, size).stream().map(dish -> dishRestMapper.toRest(dish) ).toList() ;

    return Optional.of(dishRests);
    }
}

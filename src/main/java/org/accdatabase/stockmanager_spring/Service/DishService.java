package org.accdatabase.stockmanager_spring.Service;


import org.accdatabase.stockmanager_spring.DAO.DataSource;
import org.accdatabase.stockmanager_spring.DAO.operations.DishCrudRequests;
import org.accdatabase.stockmanager_spring.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    private DataSource dataSource;
    private DishCrudRequests dishCrudRequests;

    @Autowired
    public DishService(DataSource dataSource, DishCrudRequests dishCrudRequests) {
        this.dataSource = dataSource;
        this.dishCrudRequests = dishCrudRequests;
    }

    public DishService() {
        this.dataSource = new DataSource();
        this.dishCrudRequests = new DishCrudRequests();
    }

    public List<Dish> getAll() {
    return dishCrudRequests.findAll(1,5);
    }
}

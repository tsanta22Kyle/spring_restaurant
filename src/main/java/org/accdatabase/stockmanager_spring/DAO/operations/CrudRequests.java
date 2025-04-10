package org.accdatabase.stockmanager_spring.DAO.operations;

import java.util.List;
import java.util.Optional;

public interface CrudRequests <T>{
    public T findById(String id);
    public List<T> findAll(int page, int size);
    public List<T> saveAll(List<T> entityToSave);
}

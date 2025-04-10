package org.accdatabase.stockmanager_spring.Service;

import org.accdatabase.stockmanager_spring.DAO.operations.IngredientCrudRequests;
import org.accdatabase.stockmanager_spring.Service.exception.ClientException;
import org.accdatabase.stockmanager_spring.endpoint.mapper.IngredientRestMapper;
import org.accdatabase.stockmanager_spring.endpoint.rest.CreateOrUpdateIngredient;
import org.accdatabase.stockmanager_spring.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {
    @Autowired
    private IngredientCrudRequests ingredientCrudRequests;
    @Autowired
    private IngredientRestMapper ingredientRestMapper;


    public Optional<Object> getAllIngredientsByPrice(int page, int size, Double maxPrice, Double minPrice) {
        List<Ingredient> ingredientList = ingredientCrudRequests.findAll(page, size);

        if (minPrice != null && maxPrice < 0) {
            throw new ClientException("PriceMinFilter " + minPrice + " is negative");
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new ClientException("PriceMaxFilter " + maxPrice + " is negative");
        }
        if (minPrice != null && maxPrice != null) {
            if (minPrice > maxPrice) {
                throw new ClientException("PriceMinFilter " + minPrice + " is greater than PriceMaxFilter " + maxPrice);
            }
        }
        return Optional.of(ingredientList.stream().filter(ingredient -> ingredient.getPrices().stream().filter((price) -> price.getValue() >= minPrice && price.getValue() <= maxPrice).findAny().isPresent()).toList());


    }


    public Optional<Object> getAll(int page, int size) {
        if(page < 0 || size < 0) {
            throw new ClientException("page or size is negative");
        }

        return Optional.of(ingredientCrudRequests.findAll(page, size).stream().map(ingredient -> ingredientRestMapper.toRest(ingredient)).toList());
    }

    public Optional<Object> addAll(List<CreateOrUpdateIngredient> ingredientToAdd) {
        //System.out.println("ingredients to add :"+ingredientToAdd);
        List<Ingredient> mappedIngredients = ingredientToAdd.stream().map(createOrUpdateIngredient -> ingredientRestMapper.toModel(createOrUpdateIngredient)).toList();
       // System.out.println("mapped ingredients : "+mappedIngredients);
        return Optional.of(ingredientCrudRequests.saveAll(mappedIngredients).stream().map(ingredient -> ingredientRestMapper.toRest(ingredient)).toList());
    }

    public Optional<Object> findById(String id) {
        return Optional.of(ingredientRestMapper.toRest(ingredientCrudRequests.findById(id)));
    }
}

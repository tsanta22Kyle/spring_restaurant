package org.accdatabase.stockmanager_spring.endpoint.mapper;

import org.accdatabase.stockmanager_spring.endpoint.rest.PriceRest;
import org.accdatabase.stockmanager_spring.model.Price;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class PriceRestMapper implements Function<Price, PriceRest> {
    @Override
    public PriceRest apply(Price price) {
        return new PriceRest(price.getId(), price.getValue(), price.getDate());
    }
}

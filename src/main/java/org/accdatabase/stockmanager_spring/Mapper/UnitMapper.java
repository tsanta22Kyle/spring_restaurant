package org.accdatabase.stockmanager_spring.Mapper;

import lombok.EqualsAndHashCode;
import org.accdatabase.stockmanager_spring.entities.unit;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode@Component
public class UnitMapper {
    public unit mapFromResultSet(String value) {
        if (value == null) return null;
        List<unit> units = Arrays.stream(unit.values()).toList();
        return units.stream().filter(unit -> value.equals(unit.toString())).findAny().orElseThrow(() -> new IllegalArgumentException("invalid unit : " + value));
    }
}

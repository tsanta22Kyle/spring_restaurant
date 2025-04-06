package org.accdatabase.stockmanager_spring.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockMove {
    private MoveType moveType;
    private double quantity;
    private unit unit;
    private LocalDateTime moveDate;

    @Override
    public String toString() {
        return
                "--> moveType : " + moveType +
                        "\n quantity : " + quantity +
                        unit +
                        "\n moveDate : " + moveDate ;
    }
}


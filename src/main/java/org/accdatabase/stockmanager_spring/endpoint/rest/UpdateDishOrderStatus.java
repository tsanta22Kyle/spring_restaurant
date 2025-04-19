package org.accdatabase.stockmanager_spring.endpoint.rest;

import lombok.*;
import org.accdatabase.stockmanager_spring.model.OrderProcess;
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class UpdateDishOrderStatus {
    private OrderProcess orderStatus;
}

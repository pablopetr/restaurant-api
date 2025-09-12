package com.pablopetr.restaurant_api.modules.orders.dtos;

import com.pablopetr.restaurant_api.modules.orders.entities.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse (
    UUID id,
    OrderStatus status,
    BigDecimal total,
    List<Item> items
) {
    public record Item(
        UUID itemId,
        String name,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal lineTotal
    ){}
}

package com.pablopetr.restaurant_api.modules.orders.dtos;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest (
    List<ItemInput> items,
    String note
) {
    public record ItemInput (UUID itemId, Integer quantity) {}
}

package com.pablopetr.restaurant_api.modules.orders.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestDTO(
    @NotEmpty(message = "Order must contain at least one item")
    List<ItemInput> items
) {
    public record ItemInput (
        @NotNull(message = "Item ID is required")
        UUID itemId,

        @NotNull(message = "Item quantity is required")
        Integer quantity
    ) {}
}

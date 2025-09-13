package com.pablopetr.restaurant_api.modules.orders.dtos;

import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

public record AddItemsRequestDTO(
    @NotEmpty List<@Valid Item> items
) {
    public record Item(
        @NotNull UUID itemId,
        @NotNull @Positive Integer quantity
    ) {}
}

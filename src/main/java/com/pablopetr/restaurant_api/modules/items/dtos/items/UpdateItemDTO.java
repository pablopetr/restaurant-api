package com.pablopetr.restaurant_api.modules.items.dtos.items;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateItemDTO (
    @NotBlank String name,
    @NotBlank String description,
    @NotNull Double price,
    @NotNull Integer stock,
    @NotBlank String categoryId
) {}

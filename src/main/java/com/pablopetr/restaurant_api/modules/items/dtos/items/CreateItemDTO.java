package com.pablopetr.restaurant_api.modules.items.dtos.items;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateItemDTO (
    @NotBlank String name,
    @NotBlank String slug,
    @NotBlank String description,
    @NotBlank @DecimalMin(0.0) Double price,
    @NotBlank Integer stock,
    @NotBlank UUID categoryId
) {}

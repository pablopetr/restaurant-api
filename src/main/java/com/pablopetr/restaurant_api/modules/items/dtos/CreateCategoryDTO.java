package com.pablopetr.restaurant_api.modules.items.dtos;

import com.pablopetr.restaurant_api.modules.items.enums.CategoryType;
import com.pablopetr.restaurant_api.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryDTO (
    @NotBlank @Size(min = 3, max = 32) String name,
    @NotBlank @Size(min = 16, max = 255) String description,

    @NotBlank
    @ValueOfEnum(enumClass = CategoryType.class, message = "Category must be MADE_TO_ORDER or FROM_STOCK")
    String type
) {}

package com.pablopetr.restaurant_api.modules.items.dtos.items;

import lombok.Builder;
import java.util.UUID;

@Builder
public record ItemResponseDTO(
        UUID id,
        String name,
        String description,
        Double price,
        String slug,
        Integer stock,
        UUID categoryId
) {}
package com.pablopetr.restaurant_api.modules.items.dtos.items;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ItemResponseDTO(
        UUID id,
        String name,
        String description,
        Double price,
        String slug,
        Integer stock,
        UUID categoryId
) {}
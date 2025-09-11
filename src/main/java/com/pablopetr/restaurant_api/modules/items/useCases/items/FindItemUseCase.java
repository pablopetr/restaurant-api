package com.pablopetr.restaurant_api.modules.items.useCases.items;

import com.pablopetr.restaurant_api.modules.items.dtos.items.ItemResponseDTO;
import com.pablopetr.restaurant_api.modules.items.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindItemUseCase {
    @Autowired
    private ItemRepository itemRepository;

    public ItemResponseDTO execute(UUID id) {
        var itemEntity = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return ItemResponseDTO.builder()
                .id(itemEntity.getId())
                .name(itemEntity.getName())
                .slug(itemEntity.getSlug())
                .description(itemEntity.getDescription())
                .price(itemEntity.getPrice())
                .stock(itemEntity.getStock())
                .categoryId(itemEntity.getCategory().getId())
                .build();
    }
}

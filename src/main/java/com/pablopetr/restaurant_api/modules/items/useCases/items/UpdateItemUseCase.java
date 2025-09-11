package com.pablopetr.restaurant_api.modules.items.useCases.items;

import com.pablopetr.restaurant_api.modules.items.dtos.items.UpdateItemDTO;
import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import com.pablopetr.restaurant_api.modules.items.repositories.ItemRepository;
import com.pablopetr.restaurant_api.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateItemUseCase {
    @Autowired
    private ItemRepository itemRepository;

    public ItemEntity execute(UUID id, UpdateItemDTO updateItemDTO) {
        ItemEntity itemEntity = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found!"));

        itemEntity.setName(updateItemDTO.name());
        itemEntity.setSlug(SlugUtils.toSlug(updateItemDTO.name()));
        itemEntity.setDescription(updateItemDTO.description());
        itemEntity.setPrice(updateItemDTO.price());
        itemEntity.setStock(updateItemDTO.stock());
        itemEntity.setCategoryId(UUID.fromString(updateItemDTO.categoryId()));

        return this.itemRepository.save(itemEntity);
    }
}

package com.pablopetr.restaurant_api.modules.items.useCases.items;

import com.pablopetr.restaurant_api.modules.items.dtos.items.ItemResponseDTO;
import com.pablopetr.restaurant_api.modules.items.dtos.items.UpdateItemDTO;
import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import com.pablopetr.restaurant_api.modules.items.repositories.CategoryRepository;
import com.pablopetr.restaurant_api.modules.items.repositories.ItemRepository;
import com.pablopetr.restaurant_api.utils.SlugUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateItemUseCase {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public ItemResponseDTO execute(UUID id, UpdateItemDTO updateItemDTO) {
        ItemEntity itemEntity = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found!"));

        itemEntity.setName(updateItemDTO.name());
        itemEntity.setSlug(SlugUtils.toSlug(updateItemDTO.name()));
        itemEntity.setDescription(updateItemDTO.description());
        itemEntity.setPrice(updateItemDTO.price());
        itemEntity.setStock(updateItemDTO.stock());

        if (updateItemDTO.categoryId() != null && !updateItemDTO.categoryId().isBlank()) {
            var catId = UUID.fromString(updateItemDTO.categoryId());
            var category = this.categoryRepository.findById(catId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            itemEntity.setCategory(category);
        }

        return toResponse(itemEntity);
    }

    private ItemResponseDTO toResponse(ItemEntity item) {
        UUID categoryId = (item.getCategory() != null) ? item.getCategory().getId() : null;

        return new ItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getSlug(),
                item.getStock(),
                categoryId,
                item.getCreatedAt()
        );
    }
}

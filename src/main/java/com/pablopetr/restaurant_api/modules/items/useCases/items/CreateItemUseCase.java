package com.pablopetr.restaurant_api.modules.items.useCases.items;

import com.pablopetr.restaurant_api.modules.items.dtos.items.CreateItemDTO;
import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import com.pablopetr.restaurant_api.modules.items.repositories.CategoryRepository;
import com.pablopetr.restaurant_api.modules.items.repositories.ItemRepository;
import com.pablopetr.restaurant_api.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateItemUseCase {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ItemEntity execute(CreateItemDTO createItemDTO) {
        var slug = SlugUtils.toSlug(createItemDTO.name());

        var categoryId = UUID.fromString(createItemDTO.categoryId());

        var category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found!"));

        if(this.itemRepository.existsBySlug(slug)) {
            throw new RuntimeException("Item with Slug " + slug + " already exists");
        }

        var itemEntity = ItemEntity.builder()
                .name(createItemDTO.name())
                .slug(slug)
                .description(createItemDTO.description())
                .price(createItemDTO.price())
                .stock(createItemDTO.stock())
                .category(category)
                .build();

        return this.itemRepository.save(itemEntity);
    }
}

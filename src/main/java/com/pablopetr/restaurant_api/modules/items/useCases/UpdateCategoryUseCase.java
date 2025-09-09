package com.pablopetr.restaurant_api.modules.items.useCases;

import com.pablopetr.restaurant_api.modules.items.dtos.categories.CategoryDTO;
import com.pablopetr.restaurant_api.modules.items.entities.CategoryEntity;
import com.pablopetr.restaurant_api.modules.items.enums.CategoryType;
import com.pablopetr.restaurant_api.modules.items.repositories.CategoryRepository;
import com.pablopetr.restaurant_api.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateCategoryUseCase {
    @Autowired
    CategoryRepository categoryRepository;

    public CategoryEntity execute(UUID id, CategoryDTO categoryDTO) {
        Optional<CategoryEntity> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            throw new RuntimeException("Category not found!");
        }

        var categoryEntity = category.get();

        categoryEntity.setName(categoryDTO.name());
        categoryEntity.setSlug(SlugUtils.toSlug(categoryDTO.name()));
        categoryEntity.setDescription(categoryDTO.description());
        categoryEntity.setType(CategoryType.valueOf(categoryDTO.type()));

        return categoryRepository.save(categoryEntity);
    }
}

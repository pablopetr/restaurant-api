package com.pablopetr.restaurant_api.modules.items.useCases.categories;

import com.pablopetr.restaurant_api.modules.items.entities.CategoryEntity;
import com.pablopetr.restaurant_api.modules.items.repositories.CategoryRepository;
import com.pablopetr.restaurant_api.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCategoryUseCase {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryEntity execute(CategoryEntity categoryEntity) {
        var existentCategoryByName = categoryRepository.findByName( categoryEntity.getName());

        if(existentCategoryByName.isPresent()) {
            throw new RuntimeException("Category with name " + categoryEntity.getName() + " already exists");
        }

        var slug = SlugUtils.toSlug(categoryEntity.getName());
        categoryEntity.setSlug(slug);

        var existentCategoryBySlug = categoryRepository.findBySlug(categoryEntity.getSlug());

        if(existentCategoryBySlug.isPresent()) {
            throw new RuntimeException("Category with Slug " + categoryEntity.getSlug() + " already exists");
        }

        return categoryRepository.save(categoryEntity);
    }
}

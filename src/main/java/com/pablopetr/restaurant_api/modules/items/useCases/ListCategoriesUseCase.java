package com.pablopetr.restaurant_api.modules.items.useCases;

import com.pablopetr.restaurant_api.modules.items.entities.CategoryEntity;
import com.pablopetr.restaurant_api.modules.items.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCategoriesUseCase {
    @Autowired
    private CategoryRepository categoryRepository;

    public Page<CategoryEntity> execute(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
}

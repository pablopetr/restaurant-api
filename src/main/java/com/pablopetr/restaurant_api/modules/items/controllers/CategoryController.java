package com.pablopetr.restaurant_api.modules.items.controllers;

import com.pablopetr.restaurant_api.modules.items.dtos.CreateCategoryDTO;
import com.pablopetr.restaurant_api.modules.items.entities.CategoryEntity;
import com.pablopetr.restaurant_api.modules.items.enums.CategoryType;
import com.pablopetr.restaurant_api.modules.items.useCases.CreateCategoryUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees/categories")
public class CategoryController {
    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CreateCategoryDTO createCategoryDTO) {
        try {
            var categoryType = CategoryType.valueOf(createCategoryDTO.type().trim().toUpperCase());

            var categoryEntity = CategoryEntity.builder()
                    .name(createCategoryDTO.name())
                    .description(createCategoryDTO.description())
                    .type(categoryType)
                    .build();

            var result = this.createCategoryUseCase.execute(categoryEntity);

            return ResponseEntity.ok().body(result);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}

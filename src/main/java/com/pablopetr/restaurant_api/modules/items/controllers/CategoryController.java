package com.pablopetr.restaurant_api.modules.items.controllers;

import com.pablopetr.restaurant_api.modules.items.dtos.CreateCategoryDTO;
import com.pablopetr.restaurant_api.modules.items.entities.CategoryEntity;
import com.pablopetr.restaurant_api.modules.items.enums.CategoryType;
import com.pablopetr.restaurant_api.modules.items.useCases.CreateCategoryUseCase;
import com.pablopetr.restaurant_api.modules.items.useCases.ListCategoriesUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees/categories")
public class CategoryController {
    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private ListCategoriesUseCase listCategoriesUseCase;

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

    @GetMapping
    public ResponseEntity<Page<CategoryEntity>> findAll(
            @PageableDefault(size = 2, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        try {
            var result = this.listCategoriesUseCase.execute(pageable);

            return ResponseEntity.ok(result);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

package com.pablopetr.restaurant_api.modules.items.repositories;

import com.pablopetr.restaurant_api.modules.items.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findByName(String name);
    Optional<CategoryEntity> findBySlug(String slug);
}

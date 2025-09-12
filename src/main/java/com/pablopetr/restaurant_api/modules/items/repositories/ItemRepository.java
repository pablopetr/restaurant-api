package com.pablopetr.restaurant_api.modules.items.repositories;

import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {
    boolean existsBySlug(String slug);
}

package com.pablopetr.restaurant_api.modules.items.useCases.items;

import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import com.pablopetr.restaurant_api.modules.items.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindItemUseCase {
    @Autowired
    private ItemRepository itemRepository;

    public ItemEntity execute(UUID id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }
}

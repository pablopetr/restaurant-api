package com.pablopetr.restaurant_api.modules.items.useCases.items;

import com.pablopetr.restaurant_api.modules.items.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteItemUseCase {
    @Autowired
    private ItemRepository itemRepository;

    public void execute(UUID id) {
        var item = itemRepository.findById(id);

        if(item.isEmpty()) {
            throw new RuntimeException("Item not found");
        }

        itemRepository.deleteById(id);
    }
}

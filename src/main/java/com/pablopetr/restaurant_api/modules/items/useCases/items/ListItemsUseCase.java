package com.pablopetr.restaurant_api.modules.items.useCases.items;

import com.pablopetr.restaurant_api.modules.items.dtos.items.ItemResponseDTO;
import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import com.pablopetr.restaurant_api.modules.items.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListItemsUseCase {
    @Autowired private ItemRepository repo;

    @Transactional(readOnly = true)
    public Page<ItemResponseDTO> execute(Pageable pageable) {
        return repo.findAll(pageable)
                .map(item -> new ItemResponseDTO(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getSlug(),
                        item.getStock(),
                        item.getCategory() != null ? item.getCategory().getId() : null
                ));
    }
}

package com.pablopetr.restaurant_api.modules.items.controllers;

import com.pablopetr.restaurant_api.modules.items.dtos.items.CreateItemDTO;
import com.pablopetr.restaurant_api.modules.items.dtos.items.ItemResponseDTO;
import com.pablopetr.restaurant_api.modules.items.dtos.items.UpdateItemDTO;
import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import com.pablopetr.restaurant_api.modules.items.useCases.items.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private CreateItemUseCase createItemUseCase;

    @Autowired
    private UpdateItemUseCase updateItemUseCase;

    @Autowired
    private FindItemUseCase findItemUseCase;

    @Autowired
    private ListItemsUseCase listItemsUseCase;

    @Autowired
    private DeleteItemUseCase deleteItemUseCase;

    @GetMapping
    public ResponseEntity<Page<ItemResponseDTO>> findAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        try {
            var result = this.listItemsUseCase.execute(pageable);

            return ResponseEntity.ok(result);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateItemDTO createItemDTO) {
        try {
            var result = this.createItemUseCase.execute(createItemDTO);

            return ResponseEntity.status(201).body(result);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemEntity> findById(@PathVariable String id) {
        try {
            var uuid = UUID.fromString(id);

            var result = this.findItemUseCase.execute(uuid);

            return ResponseEntity.ok().body(result);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemEntity> update(
        @PathVariable String id,
        @Valid @RequestBody UpdateItemDTO updateItemDTO
    ) {
        try {
            var uuid = UUID.fromString(id);

            var result = this.updateItemUseCase.execute(uuid,  updateItemDTO);

            return ResponseEntity.ok().body(result);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@RequestParam String id) {
        try {
            var uuid = UUID.fromString(id);

            this.deleteItemUseCase.execute(uuid);

            return ResponseEntity.noContent().build();
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}

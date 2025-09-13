package com.pablopetr.restaurant_api.modules.orders.useCases;

import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import com.pablopetr.restaurant_api.modules.items.repositories.ItemRepository;
import com.pablopetr.restaurant_api.modules.orders.dtos.AddItemsRequestDTO;
import com.pablopetr.restaurant_api.modules.orders.dtos.OrderMapper;
import com.pablopetr.restaurant_api.modules.orders.dtos.OrderResponseDTO;
import com.pablopetr.restaurant_api.modules.orders.entities.OrderEntity;
import com.pablopetr.restaurant_api.modules.orders.entities.OrderStatus;
import com.pablopetr.restaurant_api.modules.orders.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AddItemToOrderUseCase {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public OrderResponseDTO execute(UUID orderId, AddItemsRequestDTO addItemsRequestDTO) {
        OrderEntity order = this.orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if(order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order was already paid or cancelled. Please create a new order.");
        }

        var ids = addItemsRequestDTO.items().stream().map(AddItemsRequestDTO.Item::itemId).toList();
        var entities = this.itemRepository.findAllById(ids);

        var found = entities.stream().map(ItemEntity::getId).collect(java.util.stream.Collectors.toSet());
        var missing = ids.stream().filter(id -> !found.contains(id)).toList();

        if(!missing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Items not found: " + missing);
        }

        var byId = entities.stream().collect(java.util.stream.Collectors.toMap(ItemEntity::getId, it -> it));

        for(AddItemsRequestDTO.Item itemRequest : addItemsRequestDTO.items()) {
            ItemEntity item = byId.get(itemRequest.itemId());
            order.addItem(item.getId(), item.getName(), BigDecimal.valueOf(item.getPrice()), itemRequest.quantity());
        }

        this.orderRepository.save(order);

        return OrderMapper.toResponse(order);
    }
}

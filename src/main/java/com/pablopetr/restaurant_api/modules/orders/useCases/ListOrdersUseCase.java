package com.pablopetr.restaurant_api.modules.orders.useCases;

import com.pablopetr.restaurant_api.modules.orders.dtos.OrderMapper;
import com.pablopetr.restaurant_api.modules.orders.dtos.OrderResponseDTO;
import com.pablopetr.restaurant_api.modules.orders.entities.OrderEntity;
import com.pablopetr.restaurant_api.modules.orders.entities.OrderStatus;
import com.pablopetr.restaurant_api.modules.orders.repositories.OrderRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListOrdersUseCase {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> execute(@Nullable OrderStatus status, Pageable pageable) {
        Page<OrderEntity> page = (status == null)
                ? this.orderRepository.findAllBy(pageable)
                : this.orderRepository.findAllByStatus(status, pageable);

        return page.map(OrderMapper::toResponse);
    }
}

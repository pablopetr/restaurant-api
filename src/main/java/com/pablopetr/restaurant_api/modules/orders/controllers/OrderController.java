package com.pablopetr.restaurant_api.modules.orders.controllers;

import com.pablopetr.restaurant_api.modules.orders.dtos.CreateOrderRequestDTO;
import com.pablopetr.restaurant_api.modules.orders.dtos.OrderResponseDTO;
import com.pablopetr.restaurant_api.modules.orders.entities.OrderStatus;
import com.pablopetr.restaurant_api.modules.orders.useCases.CreateOrderUseCase;
import com.pablopetr.restaurant_api.modules.orders.useCases.ListOrdersUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Autowired
    private ListOrdersUseCase listOrdersUseCase;

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> findAll(
        @RequestParam(required = false)
        OrderStatus status,
        @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC)
        Pageable pageable
    ) {
        try {
            var result = this.listOrdersUseCase.execute(status, pageable);

            return ResponseEntity.ok().body(result);
        } catch (Exception exception) {
            exception.printStackTrace();

            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid CreateOrderRequestDTO createOrderRequestDTO) {
        try {
            OrderResponseDTO orderResponseDTO = this.createOrderUseCase.execute(createOrderRequestDTO);

            return ResponseEntity.status(201).body(orderResponseDTO);
        } catch (RuntimeException ex) {
            ex.printStackTrace();

            return ResponseEntity.badRequest().body(null);
        }
    }
}

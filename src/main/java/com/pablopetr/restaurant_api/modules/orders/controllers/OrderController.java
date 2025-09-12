package com.pablopetr.restaurant_api.modules.orders.controllers;

import com.pablopetr.restaurant_api.modules.orders.dtos.CreateOrderRequestDTO;
import com.pablopetr.restaurant_api.modules.orders.dtos.OrderResponseDTO;
import com.pablopetr.restaurant_api.modules.orders.useCases.CreateOrderUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private CreateOrderUseCase createOrderUseCase;

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

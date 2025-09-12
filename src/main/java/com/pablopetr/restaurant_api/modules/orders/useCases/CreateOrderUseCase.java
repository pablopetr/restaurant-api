package com.pablopetr.restaurant_api.modules.orders.useCases;

import com.pablopetr.restaurant_api.modules.items.repositories.ItemRepository;
import com.pablopetr.restaurant_api.modules.orders.dtos.CreateOrderRequestDTO;
import com.pablopetr.restaurant_api.modules.orders.dtos.OrderMapper;
import com.pablopetr.restaurant_api.modules.orders.dtos.OrderResponseDTO;
import com.pablopetr.restaurant_api.modules.orders.entities.OrderEntity;
import com.pablopetr.restaurant_api.modules.orders.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateOrderUseCase {
    @Autowired private OrderRepository orderRepository;
    @Autowired private ItemRepository itemRepository;

    @Transactional
    public OrderResponseDTO execute(CreateOrderRequestDTO req) {
        var order = new OrderEntity();

        for (var in : req.items()) {
            var item = itemRepository.findById(in.itemId())
                    .orElseThrow(() -> new IllegalArgumentException("Item not found: " + in.itemId()));

            var categoryType = item.getCategory().getType();

            System.out.println("Category Type: " + categoryType);

            boolean isFromStock = "FROM_STOCK".equalsIgnoreCase(categoryType.toString());

            if (isFromStock) {
                Integer reqQty = in.quantity();
                Integer stock = item.getStock() == null ? 0 : item.getStock();

                if (reqQty <= 0) throw new IllegalArgumentException("Quantity must be > 0");
                if (stock < reqQty) {
                    throw new IllegalArgumentException(
                        "Insufficient stock for item: " + item.getName() + " (available=" + stock + ", requested=" + reqQty + ")"
                    );
                }

                item.setStock(stock - reqQty);
            }

            order.addItem(
                    item.getId(),
                    item.getName(),
                    BigDecimal.valueOf(item.getPrice()),
                    in.quantity()
            );
        }

        order.place();
        orderRepository.save(order);

        return OrderMapper.toResponse(order);
    }
}

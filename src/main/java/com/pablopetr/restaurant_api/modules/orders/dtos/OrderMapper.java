package com.pablopetr.restaurant_api.modules.orders.dtos;

import com.pablopetr.restaurant_api.modules.orders.entities.OrderEntity;

public class OrderMapper {
    private OrderMapper() {}
    public static OrderResponseDTO toResponse(OrderEntity o) {
        var items = o.getItems().stream().map(i ->
                new OrderResponseDTO.Item(
                        i.getItemId(),
                        i.getItemNameSnapshot(),
                        i.getUnitPriceSnapshot(),
                        i.getQuantity(),
                        i.getLineTotal()
                )
        ).toList();
        return new OrderResponseDTO(o.getId(), o.getStatus(), o.getTotal(), items);
    }
}

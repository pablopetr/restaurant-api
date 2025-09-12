package com.pablopetr.restaurant_api.modules.orders.entities;

import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private OrderEntity order;

    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private ItemEntity item;

    @Column(nullable = false)
    private String itemNameSnapshot;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal unitPriceSnapshot;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal lineTotal;

    public static OrderItemEntity of(
        OrderEntity order, UUID itemId, String name, BigDecimal unitPrice, Integer quantity
    ) {
        var i = new OrderItemEntity();
        i.order = order;
        i.itemId = itemId;
        i.itemNameSnapshot = name;
        i.unitPriceSnapshot = unitPrice;
        i.setQuantity(quantity);
        return i;
    }

    public void increase(Integer by) {
        setQuantity(getQuantity() + by);
    }

    public void setQuantity(Integer quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        this.quantity = quantity;

        recalc();
    }

    private void recalc() {
        this.lineTotal = this.unitPriceSnapshot.multiply(BigDecimal.valueOf(this.quantity));
    }
}

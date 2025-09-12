package com.pablopetr.restaurant_api.modules.orders.entities;

import com.pablopetr.restaurant_api.modules.items.entities.ItemEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter @Getter
@Builder @AllArgsConstructor @NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "order_items")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false) // explicite a FK
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrderEntity order;

    @Column(name = "item_id", nullable = false)
    @EqualsAndHashCode.Include
    private UUID itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
        this.quantity = quantity;
        recalc();
    }

    private void recalc() {
        this.lineTotal = this.unitPriceSnapshot.multiply(BigDecimal.valueOf(this.quantity));
    }
}

package com.pablopetr.restaurant_api.modules.orders.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(
        mappedBy = "order",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
        orphanRemoval = true
    )
    private Set<OrderItemEntity> items = new LinkedHashSet<>();

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Version
    private Long version;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addItem(UUID itemId, String itemName, BigDecimal unitPrice, Integer quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        var existing = items.stream()
            .filter(i -> i.getItemId().equals(itemId))
            .findFirst();

        if(existing.isPresent()) {
            existing.get().increase(quantity);
        } else {
            var item = OrderItemEntity.of(this, itemId, itemName, unitPrice, quantity);

            items.add(item);
        }
    }

    public void changeQuantity(UUID itemId, Integer quantity) {
        var currentItem = requireItem(itemId);

        if(quantity <= 0) {
            items.remove(currentItem);
        } else {
            currentItem.setQuantity(quantity);
        }

        recalcTotals();
    }

    private OrderItemEntity requireItem(UUID itemId) {
        return items.stream()
            .filter(i -> i.getItemId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Item not found in order"));
    }

    public void removeItem(UUID itemId) {
        items.remove(requireItem(itemId));

        recalcTotals();
    }

    public void pay() {
        if(this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be paid");
        }

        this.status = OrderStatus.QUEUED;
    }

    public void startPreparation() {
        if(this.status != OrderStatus.QUEUED) {
            throw new IllegalStateException("Only queued orders can be started");
        }

        this.status = OrderStatus.IN_PREPARATION;
    }

    public void markAsReady() {
        if(this.status != OrderStatus.IN_PREPARATION) {
            throw new IllegalStateException("Only orders in preparation can be finished");
        }

        this.status = OrderStatus.READY;
    }

    public void markAsDelivered() {
        if(this.status != OrderStatus.READY) {
            throw new IllegalStateException("Only ready orders can be delivered");
        }

        this.status = OrderStatus.DELIVERED;
    }

    public void cancel() {
        if(this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Delivered orders cannot be cancelled");
        }

        this.status = OrderStatus.CANCELLED;
    }

    public void recalcTotals() {
        this.total = items.stream()
            .map(OrderItemEntity::getLineTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

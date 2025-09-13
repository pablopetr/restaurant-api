package com.pablopetr.restaurant_api.modules.orders.repositories;

import com.pablopetr.restaurant_api.modules.orders.entities.OrderEntity;
import com.pablopetr.restaurant_api.modules.orders.entities.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    @EntityGraph(attributePaths = {"items"})
    Optional<OrderEntity> findWithItemsById(UUID id);

    @EntityGraph(attributePaths = "items")
    Page<OrderEntity> findAllByStatus(OrderStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "items")
    Page<OrderEntity> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = "items")
    Page<OrderEntity> findByStatusIn(Collection<OrderStatus> statuses, Pageable pageable);
}

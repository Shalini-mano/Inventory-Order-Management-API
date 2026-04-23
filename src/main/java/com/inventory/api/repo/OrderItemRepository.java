package com.inventory.api.repo;

import com.inventory.api.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Find all order items for a specific product
    List<OrderItem> findByProductId(Long productId);

    // Find items belonging to a specific order ID
    List<OrderItem> findByOrderId(Long orderId);
}
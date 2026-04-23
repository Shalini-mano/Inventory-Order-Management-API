package com.inventory.api.controller;
import com.inventory.api.dto.OrderRequest;
import com.inventory.api.dto.OrderResponse;
import com.inventory.api.model.Order;
import com.inventory.api.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.inventory.api.service.OrderService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }

    /**
     * POST /api/orders
     * Logic: Place a new order for the logged-in user.
     * Role: USER
     */
    @PostMapping("/create-orders")
   // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {

        Long userId = getCurrentUserId();

        OrderResponse order = orderService.placeOrder(request, userId);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }


    /**
     * GET /api/orders/my-orders
     * Logic: Fetch all orders belonging to the current user.
     * Role: USER
     */
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {

        Long userId = getCurrentUserId();

        List<OrderResponse> orders = orderService.getOrdersByUser(userId);

        return ResponseEntity.ok(orders);
    }


    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Order> cancel(@PathVariable Long id) {
        Order cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(cancelledOrder);
    }
}
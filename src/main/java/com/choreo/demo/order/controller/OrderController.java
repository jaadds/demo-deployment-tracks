package com.choreo.demo.order.controller;

import com.choreo.demo.order.model.CreateOrderRequest;
import com.choreo.demo.order.model.Order;
import com.choreo.demo.order.model.OrderItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class OrderController {

    @Value("${app.version:1.0.0}")
    private String serviceVersion;

    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public OrderController() {
        Order seed1 = new Order("Acme Corp", List.of(
                new OrderItem("Widget A", 10, 25.00),
                new OrderItem("Widget B", 5, 40.00)
        ));
        Order seed2 = new Order("Globex Inc", List.of(
                new OrderItem("Gadget X", 3, 150.00)
        ));
        orders.put(seed1.getId(), seed1);
        orders.put(seed2.getId(), seed2);
    }

    @GetMapping("/healthz")
    public Map<String, String> health() {
        return Map.of("status", "healthy", "version", serviceVersion);
    }

    @GetMapping("/orders")
    public Map<String, Object> listOrders() {
        List<Order> all = new ArrayList<>(orders.values());
        return Map.of(
                "version", serviceVersion,
                "count", all.size(),
                "orders", all
        );
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        Order order = orders.get(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Order not found"));
        }
        return ResponseEntity.ok(Map.of("version", serviceVersion, "order", order));
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        if (request.getCustomer() == null || request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "customer and items[] are required"));
        }

        Order order = new Order(request.getCustomer(), request.getItems());
        orders.put(order.getId(), order);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("version", serviceVersion, "order", order));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        if (!orders.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Order not found"));
        }
        orders.remove(id);
        return ResponseEntity.noContent().build();
    }
}

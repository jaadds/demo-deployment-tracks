package com.choreo.demo.order.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Order {

    private String id;
    private String customer;
    private List<OrderItem> items;
    private String status;
    private double total;
    private String createdAt;

    public Order() {
        this.id = UUID.randomUUID().toString();
        this.status = "confirmed";
        this.createdAt = Instant.now().toString();
    }

    public Order(String customer, List<OrderItem> items) {
        this();
        this.customer = customer;
        this.items = items;
        this.total = items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

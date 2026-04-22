package com.choreo.demo.order.model;

import java.util.List;

public class CreateOrderRequest {

    private String customer;
    private List<OrderItem> items;

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
}

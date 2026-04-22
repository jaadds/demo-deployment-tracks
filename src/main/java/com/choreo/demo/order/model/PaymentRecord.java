package com.choreo.demo.order.model;

import java.time.Instant;
import java.util.UUID;

public class PaymentRecord {

    private String paymentId;
    private String orderId;
    private double amount;
    private String method;
    private String cardLast4;
    private String status;
    private String paidAt;

    public PaymentRecord() {
    }

    public PaymentRecord(String orderId, double amount, String method, String cardLast4) {
        this.paymentId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.cardLast4 = cardLast4;
        this.status = "completed";
        this.paidAt = Instant.now().toString();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCardLast4() {
        return cardLast4;
    }

    public void setCardLast4(String cardLast4) {
        this.cardLast4 = cardLast4;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }
}

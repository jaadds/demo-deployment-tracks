package com.choreo.demo.order.controller;

import com.choreo.demo.order.model.PaymentRecord;
import com.choreo.demo.order.model.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class PaymentController {

    @Value("${app.version:1.1.0}")
    private String serviceVersion;

    private final OrderController orderController;
    private final Map<String, PaymentRecord> payments = new ConcurrentHashMap<>();

    public PaymentController(OrderController orderController) {
        this.orderController = orderController;
    }

    @PostMapping("/orders/{id}/pay")
    public ResponseEntity<?> payOrder(@PathVariable String id, @RequestBody PaymentRequest request) {
        if (request.getMethod() == null || request.getMethod().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "payment method is required"));
        }

        ResponseEntity<?> orderResponse = orderController.getOrder(id);
        if (orderResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Order not found"));
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) orderResponse.getBody();
        com.choreo.demo.order.model.Order order = (com.choreo.demo.order.model.Order) body.get("order");

        if ("paid".equals(order.getStatus())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Order is already paid"));
        }

        PaymentRecord payment = new PaymentRecord(id, order.getTotal(), request.getMethod(), request.getCardLast4());
        payments.put(payment.getPaymentId(), payment);

        order.setStatus("paid");

        return ResponseEntity.ok(Map.of(
                "version", serviceVersion,
                "payment", payment,
                "order", order
        ));
    }

    @GetMapping("/orders/{id}/payment")
    public ResponseEntity<?> getPayment(@PathVariable String id) {
        List<PaymentRecord> orderPayments = new ArrayList<>();
        for (PaymentRecord p : payments.values()) {
            if (p.getOrderId().equals(id)) {
                orderPayments.add(p);
            }
        }

        if (orderPayments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No payment found for this order"));
        }

        return ResponseEntity.ok(Map.of(
                "version", serviceVersion,
                "payments", orderPayments
        ));
    }
}

package com.bookstore.controller;

import com.bookstore.service.OrderPlacementService;
import com.bookstore.service.TransactionalOrderWorker.OrderExecutionResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderPlacementService orderPlacementService;

    public OrderController(OrderPlacementService orderPlacementService) {
        this.orderPlacementService = orderPlacementService;
    }

    public record CreateOrderRequest(String customerEmail, String sku, int quantity) {}

    @PostMapping
    public ResponseEntity<String> createOrder(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody CreateOrderRequest request) {

        if (idempotencyKey == null || idempotencyKey.isBlank() || idempotencyKey.length() > 64) {
            return ResponseEntity.badRequest().body("{\"error\":\"Valid Idempotency-Key header (1-64 chars) is required\"}");
        }

        OrderExecutionResult result = orderPlacementService.placeOrder(
                idempotencyKey, request.customerEmail(), request.sku(), request.quantity()
        );

        return ResponseEntity.status(result.statusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Idempotent-Replayed", String.valueOf(result.replayed()))
                .body(result.jsonBody());
    }
}

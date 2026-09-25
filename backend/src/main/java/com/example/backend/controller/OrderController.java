package com.example.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.order.CreateOrderRequest;
import com.example.backend.dto.order.OrderResponse;
import com.example.backend.service.OrderService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('USER', 'TENANT')")
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse createOrder(
      @Valid @RequestBody CreateOrderRequest request) {

    return orderService.createOrder(request);
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('USER', 'TENANT')")
  public List<OrderResponse> getMyOrders() {

    return orderService.getMyOrders();
  }

  @GetMapping("/{orderId}")
  @PreAuthorize("hasAnyRole('USER', 'TENANT')")
  public OrderResponse getMyOrder(
      @PathVariable Long orderId) {

    return orderService.getMyOrder(orderId);
  }
}

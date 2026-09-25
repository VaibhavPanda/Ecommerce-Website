package com.example.backend.dto.order;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponse {

  private Long orderId;
  private LocalDateTime orderDate;
  private Integer totalQuantity;
  private BigDecimal totalAmount;
  private List<OrderItemResponse> items;
}

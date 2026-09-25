package com.example.backend.dto.order;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemResponse {

  private Long productId;
  private String productName;
  private Integer quantity;
  private BigDecimal price;
  private BigDecimal subtotal;
}

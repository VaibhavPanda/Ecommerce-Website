package com.example.backend.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

  @NotNull
  private Long productId;

  @NotNull
  @Positive
  private Integer quantity;
}

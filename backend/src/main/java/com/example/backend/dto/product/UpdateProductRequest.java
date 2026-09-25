package com.example.backend.dto.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

  @NotBlank(message = "Product name is required")
  @Size(max = 255, message = "Product name cannot exceed 255 characters")
  private String name;

  @Size(max = 2000, message = "Description cannot exceed 2000 characters")
  private String description;

  @NotNull(message = "Price is required")
  @Positive(message = "Price must be greater than zero")
  private BigDecimal price;

  @NotNull(message = "Quantity is required")
  @Positive(message = "Quantity must be greater than zero")
  private Integer quantity;

  @NotNull(message = "Category ID is required")
  private Long categoryId;
}

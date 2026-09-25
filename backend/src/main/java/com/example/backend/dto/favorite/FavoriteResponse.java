package com.example.backend.dto.favorite;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteResponse {

    private final Long id;
    private final Long productId;
    private final String productName;
    private final BigDecimal price;
}

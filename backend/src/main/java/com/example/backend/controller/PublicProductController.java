package com.example.backend.controller;

import com.example.backend.dto.product.ProductResponse;
import com.example.backend.service.PublicProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class PublicProductController {

  private final PublicProductService publicProductService;

  public PublicProductController(
      PublicProductService publicProductService) {

    this.publicProductService = publicProductService;
  }

  @GetMapping
  public Page<ProductResponse> getProducts(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String category,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "id,asc") String[] sort) {

    Sort.Direction direction = sort.length > 1
        && sort[1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(direction, sort[0]));

    return publicProductService.getProducts(
        search,
        category,
        pageable);
  }

  @GetMapping("/{productId}")
  public ProductResponse getProduct(
      @PathVariable Long productId) {

    return publicProductService.getProduct(productId);
  }
}

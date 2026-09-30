package com.example.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.common.PageResponse;
import com.example.backend.dto.product.CreateProductRequest;
import com.example.backend.dto.product.ProductResponse;
import com.example.backend.dto.product.UpdateProductRequest;
import com.example.backend.service.ProductService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/{tenant}/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('TENANT')")
  public ProductResponse createProduct(
      @PathVariable String tenant,
      @Valid @RequestBody CreateProductRequest request) {

    return productService.createProduct(
        tenant,
        request);
  }

  @GetMapping
  public PageResponse<ProductResponse> getProducts(
      @PathVariable String tenant,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) Long categoryId,
      @Min(0)
      @RequestParam(defaultValue = "0") int page,
      @Min(1) @Max(100)
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

    Page<ProductResponse> products = productService.getProducts(
        tenant,
        search,
        categoryId,
        pageable);

    return new PageResponse<>(
        products.getContent(),
        products.getNumber(),
        products.getSize(),
        products.getTotalElements(),
        products.getTotalPages());
  }

  @GetMapping("/{productId}")
  public ProductResponse getProduct(
      @PathVariable String tenant,
      @PathVariable Long productId) {

    return productService.getProduct(
        tenant,
        productId);
  }

  @PutMapping("/{productId}")
  @PreAuthorize("hasRole('TENANT')")
  public ProductResponse updateProduct(
      @PathVariable String tenant,
      @PathVariable Long productId,
      @Valid @RequestBody UpdateProductRequest request) {

    return productService.updateProduct(
        tenant,
        productId,
        request);
  }

  @DeleteMapping("/{productId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('TENANT')")
  public void deleteProduct(
      @PathVariable String tenant,
      @PathVariable Long productId) {

    productService.deleteProduct(
        tenant,
        productId);
  }

  @PatchMapping("/{productId}/activate")
  @PreAuthorize("hasRole('TENANT')")
  public ProductResponse activateProduct(
      @PathVariable String tenant,
      @PathVariable Long productId) {

    return productService.activateProduct(
        tenant,
        productId);
  }
}

package com.example.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.backend.dto.product.ProductResponse;
import com.example.backend.entity.Product;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ProductRepository;

@Service
public class PublicProductService {

  private final ProductRepository productRepository;

  public PublicProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Page<ProductResponse> getProducts(
      String search,
      String category,
      Pageable pageable) {

    Page<Product> products;

    boolean hasSearch = search != null && !search.isBlank();

    boolean hasCategory = category != null && !category.isBlank();

    if (hasSearch && hasCategory) {

      products = productRepository.searchActiveProductsByCategory(
          category,
          search,
          pageable);

    } else if (hasSearch) {

      products = productRepository.findActiveProductsByName(
          search,
          pageable);

    } else if (hasCategory) {

      products = productRepository.findActiveProductsByCategory(
          category,
          pageable);

    } else {

      products = productRepository.findActiveProducts(
          pageable);
    }

    return products.map(this::mapToResponse);
  }

  public ProductResponse getProduct(Long productId) {

    Product product = productRepository
        .findActiveById(productId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Product not found"));

    return mapToResponse(product);
  }

  private ProductResponse mapToResponse(Product product) {

    return new ProductResponse(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getQuantity(),
        product.isActive(),
        product.getCategory().getId(),
        product.getCategory().getName(),
        product.getTenant().getName());
  }
}

package com.example.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.dto.product.ProductResponse;

import com.example.backend.exception.ResourceNotFoundException;

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

      products = productRepository.searchByCategoryName(
          category,
          search,
          pageable);

    } else if (hasSearch) {

      products = productRepository
          .findByNameContainingIgnoreCase(
              search,
              pageable);

    } else if (hasCategory) {

      products = productRepository
          .findByCategory_NameIgnoreCase(
              category,
              pageable);

    } else {

      products = productRepository.findAll(pageable);
    }

    return products.map(this::mapToResponse);
  }

  public ProductResponse getProduct(Long productId) {

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));


    return mapToResponse(product);
  }

  private ProductResponse mapToResponse(Product product) {

    return new ProductResponse(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getQuantity(),
        product.getCategory().getId(),
        product.getCategory().getName(),
        product.getTenant().getName());
  }
}

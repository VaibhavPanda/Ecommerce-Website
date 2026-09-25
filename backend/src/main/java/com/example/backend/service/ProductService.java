package com.example.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.backend.dto.product.CreateProductRequest;
import com.example.backend.dto.product.ProductResponse;
import com.example.backend.dto.product.UpdateProductRequest;
import com.example.backend.entity.Category;
import com.example.backend.entity.Product;
import com.example.backend.entity.Tenant;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ProductRepository;
import com.example.backend.security.TenantAccessService;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final TenantService tenantService;
  private final CategoryService categoryService;
  private final TenantAccessService tenantAccessService;

  public ProductService(
      ProductRepository productRepository,
      TenantService tenantService,
      CategoryService categoryService,
      TenantAccessService tenantAccessService) {

    this.productRepository = productRepository;
    this.tenantService = tenantService;
    this.categoryService = categoryService;
    this.tenantAccessService = tenantAccessService;
  }

  public ProductResponse createProduct(
      String tenantDomain,
      CreateProductRequest request) {

    tenantAccessService.validateTenantAccess(tenantDomain);

    Tenant tenant = tenantService.getTenantByDomain(tenantDomain);

    Category category = categoryService.getCategory(
        tenantDomain,
        request.getCategoryId());

    Product product = new Product();

    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setPrice(request.getPrice());
    product.setQuantity(request.getQuantity());
    product.setTenant(tenant);
    product.setCategory(category);

    Product savedProduct = productRepository.save(product);

    return mapToResponse(savedProduct);
  }

  public Page<ProductResponse> getProducts(
      String tenantDomain,
      String search,
      Long categoryId,
      Pageable pageable) {

    tenantAccessService.validateTenantAccess(tenantDomain);

    Tenant tenant = tenantService.getTenantByDomain(tenantDomain);

    Page<Product> products;

    if (search != null && !search.isBlank() && categoryId != null) {

      products = productRepository.searchByTenantAndCategory(
          tenant,
          categoryId,
          search,
          pageable);

    } else if (search != null && !search.isBlank()) {

      products = productRepository.findByTenantAndNameContainingIgnoreCase(
          tenant,
          search,
          pageable);

    } else if (categoryId != null) {

      products = productRepository.findByTenantAndCategoryId(
          tenant,
          categoryId,
          pageable);

    } else {

      products = productRepository.findByTenant(
          tenant,
          pageable);
    }

    return products.map(this::mapToResponse);
  }

  public ProductResponse getProduct(
      String tenantDomain,
      Long productId) {

        tenantAccessService.validateTenantAccess(tenantDomain);

    Tenant tenant = tenantService.getTenantByDomain(tenantDomain);

    Product product = productRepository.findByIdAndTenant(
        productId,
        tenant).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    return mapToResponse(product);
  }

  public ProductResponse updateProduct(
      String tenantDomain,
      Long productId,
      UpdateProductRequest request) {

        tenantAccessService.validateTenantAccess(tenantDomain);

    Product product = getProductEntity(
        tenantDomain,
        productId);

    Category category = categoryService.getCategory(
        tenantDomain,
        request.getCategoryId());

    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setPrice(request.getPrice());
    product.setQuantity(request.getQuantity());
    product.setCategory(category);

    Product updatedProduct = productRepository.save(product);

    return mapToResponse(updatedProduct);
  }

  public void deleteProduct(
      String tenantDomain,
      Long productId) {

        tenantAccessService.validateTenantAccess(tenantDomain);

    Product product = getProductEntity(
        tenantDomain,
        productId);

    productRepository.delete(product);
  }

  private Product getProductEntity(
      String tenantDomain,
      Long productId) {

    Tenant tenant = tenantService.getTenantByDomain(tenantDomain);

    return productRepository.findByIdAndTenant(
        productId,
        tenant).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
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

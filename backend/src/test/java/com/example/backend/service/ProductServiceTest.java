// package com.example.backend.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;

// import java.math.BigDecimal;
// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;

// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;

// import com.example.backend.dto.product.CreateProductRequest;
// import com.example.backend.dto.product.ProductResponse;

// import com.example.backend.entity.Category;
// import com.example.backend.entity.Product;
// import com.example.backend.entity.Tenant;

// import com.example.backend.repository.ProductRepository;

// import com.example.backend.security.TenantAccessService;

// import com.example.backend.exception.ResourceNotFoundException;

// @ExtendWith(MockitoExtension.class)
// class ProductServiceTest {

//   @Mock
//   private ProductRepository productRepository;

//   @Mock
//   private TenantService tenantService;

//   @Mock
//   private CategoryService categoryService;

//   @Mock
//   private TenantAccessService tenantAccessService;

//   @InjectMocks
//   private ProductService productService;

//   private Tenant tenant;
//   private Category category;
//   private Product product;

//   @BeforeEach
//   void setUp() {

//     tenant = new Tenant();
//     tenant.setId(1L);
//     tenant.setName("Nike");
//     tenant.setDomain("nike");

//     category = new Category();
//     category.setId(10L);
//     category.setName("Shoes");
//     category.setTenant(tenant);

//     product = new Product();
//     product.setId(100L);
//     product.setName("Nike Air Max");
//     product.setDescription("Running shoes");
//     product.setPrice(new BigDecimal("5000.00"));
//     product.setQuantity(20);
//     product.setTenant(tenant);
//     product.setCategory(category);
//   }

//   // Test successful product creation
//   @Test
//   void shouldCreateProductSuccessfully() {

//     CreateProductRequest request = new CreateProductRequest();

//     request.setName("Nike Air Max");
//     request.setDescription("Running shoes");
//     request.setPrice(new BigDecimal("5000.00"));
//     request.setQuantity(20);
//     request.setCategoryId(10L);

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(categoryService.getCategory("nike", 10L))
//         .thenReturn(category);

//     when(productRepository.save(any(Product.class)))
//         .thenReturn(product);

//     ProductResponse response = productService.createProduct("nike", request);

//     assertNotNull(response);

//     assertEquals(
//         "Nike Air Max",
//         response.getName());

//     assertEquals(
//         new BigDecimal("5000.00"),
//         response.getPrice());

//     assertEquals(
//         20,
//         response.getQuantity());

//     assertEquals(
//         10L,
//         response.getCategoryId());

//     assertEquals(
//         "Shoes",
//         response.getCategoryName());

//     verify(tenantAccessService)
//         .validateTenantAccess("nike");

//     verify(categoryService)
//         .getCategory("nike", 10L);

//     verify(productRepository)
//         .save(any(Product.class));
//   }

//   // Test product lookup when the product does not exist
//   @Test
//   void shouldThrowExceptionWhenProductDoesNotExist() {

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(productRepository.findByIdAndTenant(999L, tenant))
//         .thenReturn(Optional.empty());

//     assertThrows(
//         ResourceNotFoundException.class,
//         () -> productService.getProduct("nike", 999L));

//     verify(productRepository)
//         .findByIdAndTenant(999L, tenant);
//   }

//   // Test successful retrieval of a single product
//   @Test
//   void shouldGetProductSuccessfully() {

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(productRepository.findByIdAndTenant(100L, tenant))
//         .thenReturn(Optional.of(product));

//     ProductResponse response = productService.getProduct("nike", 100L);

//     assertNotNull(response);

//     assertEquals(
//         100L,
//         response.getId());

//     assertEquals(
//         "Nike Air Max",
//         response.getName());

//     assertEquals(
//         new BigDecimal("5000.00"),
//         response.getPrice());

//     assertEquals(
//         "Shoes",
//         response.getCategoryName());
//   }

//   // Test retrieving products when no search or category filter is provided
//   @Test
//   void shouldGetAllProductsWhenNoFiltersProvided() {

//     Page<Product> productPage = new PageImpl<>(List.of(product));

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(productRepository.findByTenant(
//         eq(tenant),
//         any(Pageable.class))).thenReturn(productPage);

//     Pageable pageable = PageRequest.of(0, 20);

//     Page<ProductResponse> response = productService.getProducts(
//         "nike",
//         null,
//         null,
//         pageable);

//     assertEquals(
//         1,
//         response.getTotalElements());

//     assertEquals(
//         "Nike Air Max",
//         response.getContent()
//             .get(0)
//             .getName());

//     verify(productRepository)
//         .findByTenant(
//             eq(tenant),
//             eq(pageable));
//   }

//   // Test searching products by product name
//   @Test
//   void shouldSearchProductsByName() {

//     Page<Product> productPage = new PageImpl<>(List.of(product));

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(productRepository
//         .findByTenantAndNameContainingIgnoreCase(
//             eq(tenant),
//             eq("air"),
//             any(Pageable.class)))
//         .thenReturn(productPage);

//     Pageable pageable = PageRequest.of(0, 20);

//     Page<ProductResponse> response = productService.getProducts(
//         "nike",
//         "air",
//         null,
//         pageable);

//     assertEquals(
//         1,
//         response.getTotalElements());

//     assertEquals(
//         "Nike Air Max",
//         response.getContent()
//             .get(0)
//             .getName());

//     verify(productRepository)
//         .findByTenantAndNameContainingIgnoreCase(
//             eq(tenant),
//             eq("air"),
//             eq(pageable));
//   }

//   // Test filtering products by category
//   @Test
//   void shouldFilterProductsByCategory() {

//     Page<Product> productPage = new PageImpl<>(List.of(product));

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(productRepository.findByTenantAndCategoryId(
//         eq(tenant),
//         eq(10L),
//         any(Pageable.class))).thenReturn(productPage);

//     Pageable pageable = PageRequest.of(0, 20);

//     Page<ProductResponse> response = productService.getProducts(
//         "nike",
//         null,
//         10L,
//         pageable);

//     assertEquals(
//         1,
//         response.getTotalElements());

//     assertEquals(
//         "Shoes",
//         response.getContent()
//             .get(0)
//             .getCategoryName());

//     verify(productRepository)
//         .findByTenantAndCategoryId(
//             eq(tenant),
//             eq(10L),
//             eq(pageable));
//   }

//   // Test searching products within a specific category
//   @Test
//   void shouldSearchProductsWithinCategory() {

//     Page<Product> productPage = new PageImpl<>(List.of(product));

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(productRepository.searchByTenantAndCategory(
//         eq(tenant),
//         eq(10L),
//         eq("air"),
//         any(Pageable.class))).thenReturn(productPage);

//     Pageable pageable = PageRequest.of(0, 20);

//     Page<ProductResponse> response = productService.getProducts(
//         "nike",
//         "air",
//         10L,
//         pageable);

//     assertEquals(
//         1,
//         response.getTotalElements());

//     assertEquals(
//         "Nike Air Max",
//         response.getContent()
//             .get(0)
//             .getName());

//     verify(productRepository)
//         .searchByTenantAndCategory(
//             eq(tenant),
//             eq(10L),
//             eq("air"),
//             eq(pageable));
//   }
// }

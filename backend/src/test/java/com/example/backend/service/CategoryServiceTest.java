// package com.example.backend.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;

// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.example.backend.entity.Category;
// import com.example.backend.entity.Tenant;
// import com.example.backend.exception.ResourceAlreadyExistsException;
// import com.example.backend.exception.ResourceNotFoundException;
// import com.example.backend.repository.CategoryRepository;
// import com.example.backend.security.TenantAccessService;

// @ExtendWith(MockitoExtension.class)
// class CategoryServiceTest {

//   @Mock
//   private CategoryRepository categoryRepository;

//   @Mock
//   private TenantService tenantService;

//   @Mock
//   private TenantAccessService tenantAccessService;

//   @InjectMocks
//   private CategoryService categoryService;

//   private Tenant tenant;
//   private Category category;

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
//   }

//   // Test successful category creation
//   @Test
//   void shouldCreateCategorySuccessfully() {

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(categoryRepository.existsByNameAndTenant("Shoes", tenant))
//         .thenReturn(false);

//     when(categoryRepository.save(any(Category.class)))
//         .thenReturn(category);

//     Category result = categoryService.createCategory("nike", "Shoes");

//     assertNotNull(result);
//     assertEquals("Shoes", result.getName());
//     assertEquals(tenant, result.getTenant());

//     verify(tenantAccessService)
//         .validateTenantAccess("nike");

//     verify(categoryRepository)
//         .save(any(Category.class));
//   }

//   // Test duplicate category rejection
//   @Test
//   void shouldThrowExceptionWhenCategoryAlreadyExists() {

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(categoryRepository.existsByNameAndTenant("Shoes", tenant))
//         .thenReturn(true);

//     assertThrows(
//         ResourceAlreadyExistsException.class,
//         () -> categoryService.createCategory("nike", "Shoes"));

//     verify(categoryRepository, never())
//         .save(any(Category.class));
//   }

//   // Test retrieving all categories for a tenant
//   @Test
//   void shouldGetCategoriesSuccessfully() {

//     List<Category> categories = List.of(category);

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(categoryRepository.findByTenant(tenant))
//         .thenReturn(categories);

//     List<Category> result = categoryService.getCategories("nike");

//     assertNotNull(result);
//     assertEquals(1, result.size());
//     assertEquals("Shoes", result.get(0).getName());

//     verify(tenantAccessService)
//         .validateTenantAccess("nike");

//     verify(categoryRepository)
//         .findByTenant(tenant);
//   }

//   // Test retrieving a category by ID
//   @Test
//   void shouldGetCategorySuccessfully() {

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(categoryRepository.findByIdAndTenant(10L, tenant))
//         .thenReturn(Optional.of(category));

//     Category result = categoryService.getCategory("nike", 10L);

//     assertNotNull(result);
//     assertEquals(10L, result.getId());
//     assertEquals("Shoes", result.getName());
//   }

//   // Test category not found scenario
//   @Test
//   void shouldThrowExceptionWhenCategoryDoesNotExist() {

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(categoryRepository.findByIdAndTenant(999L, tenant))
//         .thenReturn(Optional.empty());

//     assertThrows(
//         ResourceNotFoundException.class,
//         () -> categoryService.getCategory("nike", 999L));
//   }

//   // Test successful category update
//   @Test
//   void shouldUpdateCategorySuccessfully() {

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(categoryRepository.findByIdAndTenant(10L, tenant))
//         .thenReturn(Optional.of(category));

//     when(categoryRepository.existsByNameAndTenant("Clothing", tenant))
//         .thenReturn(false);

//     when(categoryRepository.save(category))
//         .thenReturn(category);

//     Category result = categoryService.updateCategory(
//         "nike",
//         10L,
//         "Clothing");

//     assertNotNull(result);
//     assertEquals("Clothing", result.getName());

//     verify(categoryRepository)
//         .save(category);
//   }

//   // Test duplicate category name during update
//   @Test
//   void shouldThrowExceptionWhenUpdatingToExistingCategoryName() {

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(categoryRepository.findByIdAndTenant(10L, tenant))
//         .thenReturn(Optional.of(category));

//     when(categoryRepository.existsByNameAndTenant("Clothing", tenant))
//         .thenReturn(true);

//     assertThrows(
//         ResourceAlreadyExistsException.class,
//         () -> categoryService.updateCategory(
//             "nike",
//             10L,
//             "Clothing"));

//     verify(categoryRepository, never())
//         .save(any(Category.class));
//   }

//   // Test successful category deletion
//   @Test
//   void shouldDeleteCategorySuccessfully() {

//     when(tenantService.getTenantByDomain("nike"))
//         .thenReturn(tenant);

//     when(categoryRepository.findByIdAndTenant(10L, tenant))
//         .thenReturn(Optional.of(category));

//     categoryService.deleteCategory("nike", 10L);

//     verify(categoryRepository)
//         .delete(category);
//   }

//   // Test retrieving public category names
//   @Test
//   void shouldGetPublicCategoriesSuccessfully() {

//     List<String> categories = List.of("Clothing", "Shoes", "Watches");

//     when(categoryRepository.findDistinctCategoryNames())
//         .thenReturn(categories);

//     List<String> result = categoryService.getPublicCategories();

//     assertNotNull(result);
//     assertEquals(3, result.size());
//     assertEquals("Clothing", result.get(0));
//     assertEquals("Shoes", result.get(1));
//     assertEquals("Watches", result.get(2));

//     verify(categoryRepository)
//         .findDistinctCategoryNames();
//   }
// }

// package com.example.backend.security;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.math.BigDecimal;
// import java.util.List;
// import java.util.Map;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// import com.example.backend.dto.product.ProductResponse;
// import com.example.backend.service.ProductService;

// @SpringBootTest
// @AutoConfigureMockMvc
// class SecurityConfigTest {

//   @Autowired
//   private MockMvc mockMvc;

//   @MockitoBean
//   private ProductService productService;

//   @MockitoBean
//   private JwtDecoder jwtDecoder;

//   @Test
//   void shouldReturn401WhenNoTokenIsProvided() throws Exception {

//     mockMvc.perform(
//         get("/api/nike/products"))
//         .andExpect(status().isUnauthorized());
//   }

//   @Test
//   void shouldReturn403WhenUserTriesToCreateProduct() throws Exception {

//     mockMvc.perform(
//         post("/api/nike/products")
//             .with(
//                 jwt().jwt(jwt -> jwt.claim(
//                     "realm_access",
//                     Map.of(
//                         "roles",
//                         List.of("USER")))))
//             .contentType("application/json")
//             .content("""
//                 {
//                     "name": "Nike Air Max",
//                     "description": "Running shoes",
//                     "price": 5000,
//                     "quantity": 10,
//                     "categoryId": 1
//                 }
//                 """))
//         .andExpect(status().isForbidden());
//   }

//   @Test
//   void shouldAllowTenantToCreateProduct() throws Exception {

//     ProductResponse response = new ProductResponse(
//         1L,
//         "Nike Air Max",
//         "Running shoes",
//         new BigDecimal("5000"),
//         10,
//         true,
//         1L,
//         "Shoes",
//         "Nike");

//     when(productService.createProduct(
//         any(),
//         any()))
//         .thenReturn(response);

//     mockMvc.perform(
//         post("/api/nike/products")
//             .with(
//                 jwt().authorities(
//                     new SimpleGrantedAuthority("ROLE_TENANT")))
//             .contentType("application/json")
//             .content("""
//                 {
//                     "name": "Nike Air Max",
//                     "description": "Running shoes",
//                     "price": 5000,
//                     "quantity": 10,
//                     "categoryId": 1
//                 }
//                 """))
//         .andExpect(status().isCreated());
//   }

//   @Test
//   void shouldAllowUserToViewProducts() throws Exception {

//     when(productService.getProducts(
//         any(),
//         any(),
//         any(),
//         any()))
//         .thenReturn(
//             new PageImpl<ProductResponse>(
//                 List.of(),
//                 PageRequest.of(0, 20),
//                 0));

//     mockMvc.perform(
//         get("/api/nike/products")
//             .with(
//                 jwt().jwt(jwt -> jwt.claim(
//                     "realm_access",
//                     Map.of(
//                         "roles",
//                         List.of("USER"))))))
//         .andExpect(status().isOk());
//   }

//   @Test
//   void shouldDenyAdminFromCreatingProduct() throws Exception {

//     mockMvc.perform(
//         post("/api/nike/products")
//             .with(
//                 jwt().jwt(jwt -> jwt.claim(
//                     "realm_access",
//                     Map.of(
//                         "roles",
//                         List.of("ADMIN")))))
//             .contentType("application/json")
//             .content("""
//                 {
//                     "name": "Nike Air Max",
//                     "description": "Running shoes",
//                     "price": 5000,
//                     "quantity": 10,
//                     "categoryId": 1
//                 }
//                 """))
//         .andExpect(status().isForbidden());
//   }
// }

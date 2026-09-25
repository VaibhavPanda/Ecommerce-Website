package com.example.backend.controller;

import com.example.backend.config.TestDatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestDatabaseConfig.class)
class ProductControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  // Verifies that an unauthenticated request cannot access products.
  @Test
  void shouldRejectUnauthenticatedUser() throws Exception {

    mockMvc.perform(
        get("/api/nike/products")).andExpect(status().isUnauthorized());
  }

  // Verifies that a normal USER can view products.
  @Test
  void shouldAllowUserToViewProducts() throws Exception {

    mockMvc.perform(
        get("/api/nike/products")
            .with(jwt().authorities(
                new SimpleGrantedAuthority("ROLE_USER"))))
        .andExpect(status().isOk());
  }
}

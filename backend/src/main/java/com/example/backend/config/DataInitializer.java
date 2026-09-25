package com.example.backend.config;

import com.example.backend.entity.Role;
import com.example.backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner initializeRoles(RoleRepository roleRepository) {

    return args -> {

      createRoleIfNotExists(
          roleRepository,
          "ADMIN");

      createRoleIfNotExists(
          roleRepository,
          "TENANT");

      createRoleIfNotExists(
          roleRepository,
          "USER");
    };
  }

  private void createRoleIfNotExists(
      RoleRepository roleRepository,
      String roleName) {

    if (!roleRepository.existsByName(roleName)) {

      Role role = new Role();
      role.setName(roleName);

      roleRepository.save(role);
    }
  }
}

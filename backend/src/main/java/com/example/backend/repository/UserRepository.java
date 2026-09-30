package com.example.backend.repository;

import com.example.backend.entity.User;
import com.example.backend.entity.Tenant;
import com.example.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  //unique
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  //keycloak
  Optional<User> findByKeycloakUserId(String keycloakUserId);

  //conflict for same creds
  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  //user tenant relationship
  List<User> findByTenant(Tenant tenant);

  List<User> findByTenantAndRole(Tenant tenant, Role role);

  //get user list
  List<User> findAllByOrderByIdAsc();
}

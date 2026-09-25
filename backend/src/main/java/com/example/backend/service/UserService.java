package com.example.backend.service;

import com.example.backend.entity.Role;
import com.example.backend.entity.Tenant;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final TenantService tenantService;
  private final RoleService roleService;

  public UserService(
      UserRepository userRepository,
      TenantService tenantService,
      RoleService roleService) {

    this.userRepository = userRepository;
    this.tenantService = tenantService;
    this.roleService = roleService;
  }

  public User getUserByUsername(String username) {

    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "User not found"));
  }

  public User getUserByKeycloakUserId(
      String keycloakUserId) {

    return userRepository
        .findByKeycloakUserId(keycloakUserId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "User not found"));
  }

  public List<User> getUsersByTenant(
      String tenantDomain) {

    Tenant tenant = tenantService.getTenantByDomain(tenantDomain);

    return userRepository.findByTenant(tenant);
  }

  public User createApplicationUser(
      String username,
      String email,
      String keycloakUserId,
      String tenantDomain,
      String roleName) {

    if (userRepository.existsByUsername(username)) {
      throw new ResourceAlreadyExistsException(
          "Username already exists");
    }

    if (userRepository.existsByEmail(email)) {
      throw new ResourceAlreadyExistsException(
          "Email already exists");
    }

    Tenant tenant = null;

    if (tenantDomain != null && !tenantDomain.isBlank()) {
      tenant = tenantService.getTenantByDomain(tenantDomain);
    }

    Role role = roleService.getRoleByName(roleName);

    User user = new User();

    user.setUsername(username);
    user.setEmail(email);
    user.setKeycloakUserId(keycloakUserId);
    user.setTenant(tenant);
    user.setRole(role);

    return userRepository.save(user);
  }
}

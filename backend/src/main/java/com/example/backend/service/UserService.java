package com.example.backend.service;

import com.example.backend.dto.user.AdminUserResponse;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.entity.Role;
import com.example.backend.entity.Tenant;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.KeycloakAdminService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final TenantService tenantService;
  private final RoleService roleService;
  private final KeycloakAdminService keycloakAdminService;

  public UserService(
      UserRepository userRepository,
      TenantService tenantService,
      RoleService roleService,
      KeycloakAdminService keycloakAdminService) {

    this.userRepository = userRepository;
    this.tenantService = tenantService;
    this.roleService = roleService;
    this.keycloakAdminService = keycloakAdminService;
  }

  //find username
  public User getUserByUsername(String username) {

    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException(
            "User not found"));
  }

  //find user by keycloak ID
  public User getUserByKeycloakUserId(String keycloakUserId) {

    return userRepository
        .findByKeycloakUserId(keycloakUserId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "User not found"));
  }

  //get tenant user
  public List<User> getUsersByTenant(String tenantDomain) {

    Tenant tenant = tenantService.getTenantByDomain(tenantDomain);

    return userRepository.findByTenant(tenant);
  }

  //create user -> unique check -> role check not needed -> if tenant verify
  public UserResponse createApplicationUser(
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

    if (roleName == null || roleName.isBlank()) {
      throw new IllegalArgumentException(
          "Role is required");
    }

    String normalizedRole = roleName.trim().toUpperCase();

    //only these roles must exist
    if (!normalizedRole.equals("USER")
        && !normalizedRole.equals("TENANT")
        && !normalizedRole.equals("ADMIN")) {

      throw new IllegalArgumentException(
          "Invalid role: " + roleName);
    }

    Tenant tenant = null;

    //tenant verification
    if ("TENANT".equals(normalizedRole)) {

      if (tenantDomain == null || tenantDomain.isBlank()) {
        throw new IllegalArgumentException(
            "Tenant domain is required for TENANT role");
      }

      tenant = tenantService.getTenantByDomain(
          tenantDomain.trim());

    } else if (tenantDomain != null
        && !tenantDomain.isBlank()) {

      throw new IllegalArgumentException(
          "Tenant domain must only be provided for TENANT role");
    }

    Role role = roleService.getRoleByName(normalizedRole);

    User user = new User();

    user.setUsername(username.trim());
    user.setEmail(email.trim());
    user.setKeycloakUserId(keycloakUserId);
    user.setTenant(tenant);
    user.setRole(role);

    User savedUser = userRepository.save(user);

    return mapToUserResponse(savedUser);
  }

  // ADMIN USER MANAGEMENT
  @Transactional(readOnly = true)
  public List<AdminUserResponse> getAllUsers() {

    return userRepository
        .findAllByOrderByIdAsc()
        .stream()
        .map(this::mapToAdminUserResponse)
        .toList();
  }

  @Transactional
  public AdminUserResponse makeUserTenant(
      Long userId,
      String tenantName,
      String tenantDomain) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "User not found"));

    if ("TENANT".equals(user.getRole().getName())) {
      throw new ResourceAlreadyExistsException(
          "User is already a tenant");
    }

    //only regular user can be promoted to tenant
    if (!"USER".equals(user.getRole().getName())) {
      throw new IllegalStateException(
          "Only regular users can be made tenants");
    }

    if (tenantName == null || tenantName.isBlank()) {
      throw new IllegalArgumentException(
          "Tenant name is required");
    }

    if (tenantDomain == null || tenantDomain.isBlank()) {
      throw new IllegalArgumentException(
          "Tenant domain is required");
    }

    Tenant tenant = tenantService.createOrReactivateTenant(
        tenantName.trim(),
        tenantDomain.trim());

    Role tenantRole = roleService.getRoleByName("TENANT");

    user.setTenant(tenant);
    user.setRole(tenantRole);

    User savedUser = userRepository.save(user);

    //sync role with keycloak
    keycloakAdminService.removeRealmRole(
        user.getKeycloakUserId(),
        "USER");

    keycloakAdminService.assignRealmRole(
        user.getKeycloakUserId(),
        "TENANT");

    return mapToAdminUserResponse(savedUser);
  }

  @Transactional
  public AdminUserResponse removeUserTenant(Long userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "User not found"));

    if (!"TENANT".equals(user.getRole().getName())) {
      throw new IllegalStateException(
          "User is not a tenant");
    }

    Tenant tenant = user.getTenant();

    if (tenant == null) {
      throw new IllegalStateException(
          "Tenant user has no tenant assigned");
    }

    //deactivate tenant
    tenantService.deleteTenant(tenant.getId());

    //back to regular user
    Role userRole = roleService.getRoleByName("USER");

    user.setRole(userRole);


    user.setTenant(null);

    User savedUser = userRepository.save(user);

    //sync keycloak
    keycloakAdminService.removeRealmRole(
        user.getKeycloakUserId(),
        "TENANT");

    keycloakAdminService.assignRealmRole(
        user.getKeycloakUserId(),
        "USER");

    return mapToAdminUserResponse(savedUser);
  }


  // RESPONSE MAPPER
  private UserResponse mapToUserResponse(User user) {

    return new UserResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRole().getName());
  }

  private AdminUserResponse mapToAdminUserResponse(User user) {

    AdminUserResponse.TenantInfo tenantInfo = null;

    if (user.getTenant() != null) {

      Tenant tenant = user.getTenant();

      tenantInfo = new AdminUserResponse.TenantInfo(
          tenant.getId(),
          tenant.getName(),
          tenant.getDomain(),
          tenant.isActive());
    }

    return new AdminUserResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRole().getName(),
        tenantInfo);
  }
}

package com.example.backend.service;

import com.example.backend.dto.auth.RegisterRequest;
import com.example.backend.dto.user.CreateUserRequest;
import com.example.backend.entity.User;
import com.example.backend.security.KeycloakAdminService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProvisioningService {

  private final KeycloakAdminService keycloakAdminService;
  private final UserService userService;

  public UserProvisioningService(
      KeycloakAdminService keycloakAdminService,
      UserService userService) {

    this.keycloakAdminService = keycloakAdminService;
    this.userService = userService;
  }

  @Transactional
  public User createUser(CreateUserRequest request) {

    String keycloakUserId = null;

    try {

      keycloakUserId = keycloakAdminService.createUser(
          request.getUsername(),
          request.getEmail(),
          request.getPassword());

      keycloakAdminService.assignRealmRole(
          keycloakUserId,
          request.getRoleName());

      return userService.createApplicationUser(
          request.getUsername(),
          request.getEmail(),
          keycloakUserId,
          request.getTenantDomain(),
          request.getRoleName());

    } catch (RuntimeException exception) {

      if (keycloakUserId != null) {
        keycloakAdminService.deleteUser(keycloakUserId);
      }

      throw exception;
    }

  }

  @Transactional
  public User registerUser(RegisterRequest request) {

    String keycloakUserId = null;

    try {

      keycloakUserId = keycloakAdminService.createUser(
          request.getUsername(),
          request.getEmail(),
          request.getPassword());

      keycloakAdminService.assignRealmRole(
          keycloakUserId,
          "USER");

      return userService.createApplicationUser(
          request.getUsername(),
          request.getEmail(),
          keycloakUserId,
          null,
          "USER");

    } catch (RuntimeException exception) {

      if (keycloakUserId != null) {
        keycloakAdminService.deleteUser(keycloakUserId);
      }

      throw exception;
    }
  }
}

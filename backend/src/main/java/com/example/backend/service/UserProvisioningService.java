package com.example.backend.service;

import com.example.backend.dto.auth.RegisterRequest;
import com.example.backend.dto.user.CreateUserRequest;
import com.example.backend.dto.user.UserResponse;
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

  //KC made
  @Transactional
  public UserResponse createUser(CreateUserRequest request) {

    String keycloakUserId = null;

    try {

      //Create user in Keycloak
      keycloakUserId = keycloakAdminService.createUser(
          request.getUsername(),
          request.getEmail(),
          request.getPassword());

      //Assign requested realm role
      keycloakAdminService.assignRealmRole(
          keycloakUserId,
          request.getRoleName());

      //Create corresponding application user
      return userService.createApplicationUser(
          request.getUsername(),
          request.getEmail(),
          keycloakUserId,
          request.getTenantDomain(),
          request.getRoleName());

    } catch (RuntimeException exception) {

      //DB fail to save then delete from KC
      if (keycloakUserId != null) {
        keycloakAdminService.deleteUser(keycloakUserId);
      }

      throw exception;
    }
  }

  //site form made
  @Transactional
  public UserResponse registerUser(RegisterRequest request) {

    String keycloakUserId = null;

    try {

      //Create user in Keycloak
      keycloakUserId = keycloakAdminService.createUser(
          request.getUsername(),
          request.getEmail(),
          request.getPassword());

      //Normal registration always creates a USER
      keycloakAdminService.assignRealmRole(
          keycloakUserId,
          "USER");

      //Create application user
      return userService.createApplicationUser(
          request.getUsername(),
          request.getEmail(),
          keycloakUserId,
          null,
          "USER");

    } catch (RuntimeException exception) {

      // if database/application-user creation fails -> rollback i.e. delete from KC too
      if (keycloakUserId != null) {
        keycloakAdminService.deleteUser(keycloakUserId);
      }

      throw exception;
    }
  }
}

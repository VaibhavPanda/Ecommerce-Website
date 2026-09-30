package com.example.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.auth.RegisterRequest;
import com.example.backend.dto.auth.UserProfileResponse;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.security.CurrentUserService;
import com.example.backend.service.UserProvisioningService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final CurrentUserService currentUserService;
  private final UserProvisioningService userProvisioningService;

  public AuthController(
      CurrentUserService currentUserService,
      UserProvisioningService userProvisioningService) {

    this.currentUserService = currentUserService;
    this.userProvisioningService = userProvisioningService;
  }

  @GetMapping("/me")
  public UserProfileResponse getCurrentUser() {

    var user = currentUserService.getCurrentUser();

    UserProfileResponse.TenantInfo tenantInfo = null;

    if (user.getTenant() != null) {

      tenantInfo = new UserProfileResponse.TenantInfo(
          user.getTenant().getName(),
          user.getTenant().getDomain());
    }

    return new UserProfileResponse(
        user.getUsername(),
        user.getEmail(),
        user.getRole().getName(),
        tenantInfo);
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')")
  public String userOnly() {
    return "You have USER role";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminOnly() {
    return "You have ADMIN role";
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserProfileResponse register(
      @Valid @RequestBody RegisterRequest request) {

    UserResponse user = userProvisioningService.registerUser(request);

    return new UserProfileResponse(
        user.getUsername(),
        user.getEmail(),
        user.getRole(),
        null);
  }
}

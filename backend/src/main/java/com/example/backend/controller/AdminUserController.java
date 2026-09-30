package com.example.backend.controller;

import com.example.backend.dto.user.AdminUserResponse;
import com.example.backend.dto.user.CreateUserRequest;
import com.example.backend.dto.user.MakeTenantRequest;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.service.UserProvisioningService;
import com.example.backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

  private final UserProvisioningService userProvisioningService;
  private final UserService userService;

  public AdminUserController(
      UserProvisioningService userProvisioningService,
      UserService userService) {

    this.userProvisioningService = userProvisioningService;
    this.userService = userService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse createUser(
      @Valid @RequestBody CreateUserRequest request) {

    return userProvisioningService.createUser(request);
  }

  @GetMapping
  public ResponseEntity<List<AdminUserResponse>> getAllUsers() {

    return ResponseEntity.ok(
        userService.getAllUsers());
  }

  @PatchMapping("/{userId}/make-tenant")
  public ResponseEntity<AdminUserResponse> makeTenant(
      @PathVariable Long userId,
      @Valid @RequestBody MakeTenantRequest request) {

    return ResponseEntity.ok(
        userService.makeUserTenant(
            userId,
            request.getTenantName(),
            request.getTenantDomain()));
  }

  @PatchMapping("/{userId}/remove-tenant")
  public ResponseEntity<AdminUserResponse> removeTenant(
      @PathVariable Long userId) {

    return ResponseEntity.ok(
        userService.removeUserTenant(userId));
  }
}

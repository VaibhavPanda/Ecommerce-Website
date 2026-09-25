package com.example.backend.controller;

import com.example.backend.dto.user.CreateUserRequest;
import com.example.backend.entity.User;
import com.example.backend.service.UserProvisioningService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

  private final UserProvisioningService userProvisioningService;

  public AdminUserController(
      UserProvisioningService userProvisioningService) {
    this.userProvisioningService = userProvisioningService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('ADMIN')")
  public User createUser(
      @Valid @RequestBody CreateUserRequest request) {

    return userProvisioningService.createUser(request);
  }
}

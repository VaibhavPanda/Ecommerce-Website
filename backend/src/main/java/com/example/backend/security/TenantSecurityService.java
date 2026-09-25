package com.example.backend.security;

import com.example.backend.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class TenantSecurityService {

  private final UserRepository userRepository;

  public TenantSecurityService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean hasAccessToTenant(
      Authentication authentication,
      String tenantDomain) {

    Jwt jwt = (Jwt) authentication.getPrincipal();

    String keycloakUserId = jwt.getSubject();

    User user = userRepository
        .findByKeycloakUserId(keycloakUserId)
        .orElseThrow(() -> new ResourceNotFoundException("Application user not found"));

    if (user.getTenant() == null) {
      return false;
    }

    return user.getTenant()
        .getDomain()
        .equalsIgnoreCase(tenantDomain);
  }
}

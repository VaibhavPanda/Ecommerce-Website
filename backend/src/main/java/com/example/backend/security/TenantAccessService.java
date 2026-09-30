package com.example.backend.security;

import com.example.backend.entity.Tenant;
import com.example.backend.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class TenantAccessService {

  private final CurrentUserService currentUserService;

  public TenantAccessService(CurrentUserService currentUserService) {
    this.currentUserService = currentUserService;
  }

  public void validateTenantAccess(String tenantDomain) {

    User currentUser = currentUserService.getCurrentUser();

    // Platform admins are not restricted to a single tenant
    if ("ADMIN".equals(currentUser.getRole().getName())) {
      return;
    }

    Tenant userTenant = currentUser.getTenant();

    if (userTenant == null) {
      throw new AccessDeniedException(
          "User is not associated with a tenant");
    }

    if (!userTenant.isActive()) {
      throw new AccessDeniedException(
          "Tenant is inactive");
    }

    //tenant isolation
    if (!userTenant.getDomain().equalsIgnoreCase(tenantDomain)) {
      throw new AccessDeniedException(
          "You do not have access to this tenant");
    }
    return;
  }
}

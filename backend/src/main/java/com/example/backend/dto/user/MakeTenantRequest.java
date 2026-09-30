package com.example.backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MakeTenantRequest {

  @NotBlank(message = "Tenant name is required")
  @Size(max = 255, message = "Tenant name must not exceed 255 characters")
  private String tenantName;

  @NotBlank(message = "Tenant domain is required")
  @Size(max = 255, message = "Tenant domain must not exceed 255 characters")
  private String tenantDomain;
}

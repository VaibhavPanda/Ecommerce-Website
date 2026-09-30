package com.example.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminUserResponse {

  private Long id;

  private String username;

  private String email;

  private String role;

  private TenantInfo tenant;

  @Getter
  @AllArgsConstructor
  public static class TenantInfo {

    private Long id;

    private String name;

    private String domain;

    private boolean active;
  }
}

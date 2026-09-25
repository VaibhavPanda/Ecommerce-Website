package com.example.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

    private String username;
    private String email;
    private String role;
    private TenantInfo tenant;

    @Getter
    @AllArgsConstructor
    public static class TenantInfo {
        private String name;
        private String domain;
    }
}

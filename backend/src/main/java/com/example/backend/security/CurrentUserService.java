package com.example.backend.security;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

//jwt -> DB user fetch then preserve the authentication in SecurityContextHolder
@Service
public class CurrentUserService {

  private final UserService userService;

  public CurrentUserService(UserService userService) {
    this.userService = userService;
  }

  public User getCurrentUser() {


    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    String keycloakUserId = authentication.getName();

    return userService.getUserByKeycloakUserId(keycloakUserId);
  }
}

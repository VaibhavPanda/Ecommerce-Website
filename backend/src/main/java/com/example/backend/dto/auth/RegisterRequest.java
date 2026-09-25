package com.example.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

  @NotBlank
  @Size(max = 255)
  private String username;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 8, max = 100)
  private String password;

}

package com.example.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {

    http
        .csrf(csrf -> csrf.disable())

        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/test",
                "/api/auth/register",
                "/api/products/**",
                "/api/products",
                "/api/categories")
            .permitAll()

            .anyRequest().authenticated())

        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> {

              JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

              converter.setJwtGrantedAuthoritiesConverter(
                  new KeycloakRoleConverter());

              jwt.jwtAuthenticationConverter(converter);
            }));

    return http.build();
  }
}

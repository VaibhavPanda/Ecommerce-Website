package com.example.backend.security;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;

@Service
public class KeycloakAdminService {

  private final Keycloak keycloak;

  public KeycloakAdminService(Keycloak keycloak) {
    this.keycloak = keycloak;
  }

  public void testConnection() {
    keycloak.realm("ecommerce")
        .users()
        .count();
  }

  public String createUser(
            String username,
            String email,
            String password) {

        UserRepresentation user = new UserRepresentation();

        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        CredentialRepresentation credential =
                new CredentialRepresentation();

        credential.setType(OAuth2Constants.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(
                java.util.List.of(credential)
        );

        Response response = keycloak
                .realm("ecommerce")
                .users()
                .create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException(
                    "Failed to create Keycloak user. Status: "
                    + response.getStatus()
            );
        }

        String location =
                response.getHeaderString("Location");

        response.close();

        return location.substring(
                location.lastIndexOf("/") + 1
        );
    }

    public void assignRealmRole(String userId, String roleName) {

      var role = keycloak
          .realm("ecommerce")
          .roles()
          .get(roleName)
          .toRepresentation();

      keycloak
          .realm("ecommerce")
          .users()
          .get(userId)
          .roles()
          .realmLevel()
          .add(java.util.List.of(role));
    }

    public void deleteUser(String userId) {

      keycloak
          .realm("ecommerce")
          .users()
          .delete(userId);
    }
}

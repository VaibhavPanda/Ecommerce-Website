package com.example.backend.security;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import jakarta.ws.rs.core.Response;

@Service
public class KeycloakAdminService {

  private final Keycloak keycloak;
  private final String realm;

  public KeycloakAdminService(
      Keycloak keycloak,
      @Value("${keycloak.realm}") String realm) {

    this.keycloak = keycloak;
    this.realm = realm;
  }

  public void testConnection() {

    keycloak
        .realm(realm)
        .users()
        .count();
  }

  // Create + delete user

  public String createUser(
      String username,
      String email,
      String password) {

    UserRepresentation user = new UserRepresentation();

    user.setUsername(username);
    user.setEmail(email);
    user.setEnabled(true);

    CredentialRepresentation credential = new CredentialRepresentation();

    credential.setType(OAuth2Constants.PASSWORD);
    credential.setValue(password);
    credential.setTemporary(false);

    user.setCredentials(
        List.of(credential));

    Response response = keycloak
        .realm(realm)
        .users()
        .create(user);

    if (response.getStatus() != 201) {

      int status = response.getStatus();

      response.close();

      throw new RuntimeException(
          "Failed to create Keycloak user. Status: "
              + status);
    }

    String location = response.getHeaderString("Location");

    response.close();

    /*
     * Keycloak should return the created user's
     * location. We need it to extract the user ID.
     */
    if (location == null || location.isBlank()) {

      throw new RuntimeException(
          "Keycloak user was created but "
              + "user ID could not be determined");
    }

    return location.substring(
        location.lastIndexOf("/") + 1);
  }

  public void deleteUser(String userId) {

    keycloak
        .realm(realm)
        .users()
        .delete(userId);
  }

  // Add + remove role from user

  public void assignRealmRole(
      String userId,
      String roleName) {

    var role = keycloak
        .realm(realm)
        .roles()
        .get(roleName)
        .toRepresentation();

    keycloak
        .realm(realm)
        .users()
        .get(userId)
        .roles()
        .realmLevel()
        .add(List.of(role));
  }

  public void removeRealmRole(
      String userId,
      String roleName) {

    var role = keycloak
        .realm(realm)
        .roles()
        .get(roleName)
        .toRepresentation();

    keycloak
        .realm(realm)
        .users()
        .get(userId)
        .roles()
        .realmLevel()
        .remove(List.of(role));
  }
}

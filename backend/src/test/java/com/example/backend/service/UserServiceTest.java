package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.entity.Role;
import com.example.backend.entity.Tenant;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private TenantService tenantService;

  @Mock
  private RoleService roleService;

  @InjectMocks
  private UserService userService;

  private User user;
  private Tenant tenant;
  private Role role;

  @BeforeEach
  void setUp() {

    tenant = new Tenant();
    tenant.setId(1L);
    tenant.setName("Nike");
    tenant.setDomain("nike");

    role = new Role();
    role.setId(1L);
    role.setName("USER");

    user = new User();
    user.setId(100L);
    user.setUsername("nikeuser");
    user.setEmail("nikeuser@gmail.com");
    user.setKeycloakUserId("keycloak-123");
    user.setTenant(tenant);
    user.setRole(role);
  }

  // Test retrieving a user by username
  @Test
  void shouldGetUserByUsernameSuccessfully() {

    when(userRepository.findByUsername("nikeuser"))
        .thenReturn(Optional.of(user));

    User result = userService.getUserByUsername("nikeuser");

    assertNotNull(result);
    assertEquals("nikeuser", result.getUsername());
    assertEquals("nikeuser@gmail.com", result.getEmail());

    verify(userRepository)
        .findByUsername("nikeuser");
  }

  // Test user not found when searching by username
  @Test
  void shouldThrowExceptionWhenUsernameDoesNotExist() {

    when(userRepository.findByUsername("unknown"))
        .thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> userService.getUserByUsername("unknown"));
  }

  // Test retrieving a user by Keycloak user ID
  @Test
  void shouldGetUserByKeycloakUserIdSuccessfully() {

    when(userRepository.findByKeycloakUserId("keycloak-123"))
        .thenReturn(Optional.of(user));

    User result = userService.getUserByKeycloakUserId("keycloak-123");

    assertNotNull(result);
    assertEquals("keycloak-123", result.getKeycloakUserId());
    assertEquals("nikeuser", result.getUsername());

    verify(userRepository)
        .findByKeycloakUserId("keycloak-123");
  }

  // Test user not found when searching by Keycloak ID
  @Test
  void shouldThrowExceptionWhenKeycloakUserDoesNotExist() {

    when(userRepository.findByKeycloakUserId("unknown-id"))
        .thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> userService.getUserByKeycloakUserId("unknown-id"));
  }

  // Test retrieving all users belonging to a tenant
  @Test
  void shouldGetUsersByTenantSuccessfully() {

    when(tenantService.getTenantByDomain("nike"))
        .thenReturn(tenant);

    when(userRepository.findByTenant(tenant))
        .thenReturn(List.of(user));

    List<User> result = userService.getUsersByTenant("nike");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("nikeuser", result.get(0).getUsername());

    verify(tenantService)
        .getTenantByDomain("nike");

    verify(userRepository)
        .findByTenant(tenant);
  }

  // Test successful application user creation
  @Test
  void shouldCreateApplicationUserSuccessfully() {

    when(userRepository.existsByUsername("nikeuser"))
        .thenReturn(false);

    when(userRepository.existsByEmail("nikeuser@gmail.com"))
        .thenReturn(false);

    when(tenantService.getTenantByDomain("nike"))
        .thenReturn(tenant);

    when(roleService.getRoleByName("USER"))
        .thenReturn(role);

    when(userRepository.save(any(User.class)))
        .thenReturn(user);

    User result = userService.createApplicationUser(
        "nikeuser",
        "nikeuser@gmail.com",
        "keycloak-123",
        "nike",
        "USER");

    assertNotNull(result);
    assertEquals("nikeuser", result.getUsername());
    assertEquals("nikeuser@gmail.com", result.getEmail());
    assertEquals("keycloak-123", result.getKeycloakUserId());
    assertEquals(tenant, result.getTenant());
    assertEquals(role, result.getRole());

    verify(userRepository)
        .save(any(User.class));
  }

  // Test duplicate username rejection
  @Test
  void shouldThrowExceptionWhenUsernameAlreadyExists() {

    when(userRepository.existsByUsername("nikeuser"))
        .thenReturn(true);

    assertThrows(
        ResourceAlreadyExistsException.class,
        () -> userService.createApplicationUser(
            "nikeuser",
            "nikeuser@gmail.com",
            "keycloak-123",
            "nike",
            "USER"));

    verify(userRepository, never())
        .save(any(User.class));

    verify(userRepository, never())
        .existsByEmail(anyString());
  }

  // Test duplicate email rejection
  @Test
  void shouldThrowExceptionWhenEmailAlreadyExists() {

    when(userRepository.existsByUsername("nikeuser"))
        .thenReturn(false);

    when(userRepository.existsByEmail("nikeuser@gmail.com"))
        .thenReturn(true);

    assertThrows(
        ResourceAlreadyExistsException.class,
        () -> userService.createApplicationUser(
            "nikeuser",
            "nikeuser@gmail.com",
            "keycloak-123",
            "nike",
            "USER"));

    verify(userRepository, never())
        .save(any(User.class));
  }

  // Test creating a user without a tenant
  @Test
  void shouldCreateApplicationUserWithoutTenantSuccessfully() {

    when(userRepository.existsByUsername("normaluser"))
        .thenReturn(false);

    when(userRepository.existsByEmail("normaluser@gmail.com"))
        .thenReturn(false);

    when(roleService.getRoleByName("USER"))
        .thenReturn(role);

    User userWithoutTenant = new User();
    userWithoutTenant.setId(200L);
    userWithoutTenant.setUsername("normaluser");
    userWithoutTenant.setEmail("normaluser@gmail.com");
    userWithoutTenant.setKeycloakUserId("keycloak-456");
    userWithoutTenant.setTenant(null);
    userWithoutTenant.setRole(role);

    when(userRepository.save(any(User.class)))
        .thenReturn(userWithoutTenant);

    User result = userService.createApplicationUser(
        "normaluser",
        "normaluser@gmail.com",
        "keycloak-456",
        null,
        "USER");

    assertNotNull(result);
    assertEquals("normaluser", result.getUsername());
    assertNull(result.getTenant());
    assertEquals(role, result.getRole());

    verify(tenantService, never())
        .getTenantByDomain(anyString());

    verify(userRepository)
        .save(any(User.class));
  }

  // Test that the correct tenant is assigned during user creation
  @Test
  void shouldAssignCorrectTenantToUser() {

    when(userRepository.existsByUsername("nikeuser"))
        .thenReturn(false);

    when(userRepository.existsByEmail("nikeuser@gmail.com"))
        .thenReturn(false);

    when(tenantService.getTenantByDomain("nike"))
        .thenReturn(tenant);

    when(roleService.getRoleByName("USER"))
        .thenReturn(role);

    when(userRepository.save(any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    User result = userService.createApplicationUser(
        "nikeuser",
        "nikeuser@gmail.com",
        "keycloak-123",
        "nike",
        "USER");

    assertNotNull(result.getTenant());
    assertEquals("nike", result.getTenant().getDomain());
  }

  // Test that the correct role is assigned during user creation
  @Test
  void shouldAssignCorrectRoleToUser() {

    when(userRepository.existsByUsername("nikeuser"))
        .thenReturn(false);

    when(userRepository.existsByEmail("nikeuser@gmail.com"))
        .thenReturn(false);

    when(tenantService.getTenantByDomain("nike"))
        .thenReturn(tenant);

    when(roleService.getRoleByName("USER"))
        .thenReturn(role);

    when(userRepository.save(any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    User result = userService.createApplicationUser(
        "nikeuser",
        "nikeuser@gmail.com",
        "keycloak-123",
        "nike",
        "USER");

    assertNotNull(result.getRole());
    assertEquals("USER", result.getRole().getName());
  }
}

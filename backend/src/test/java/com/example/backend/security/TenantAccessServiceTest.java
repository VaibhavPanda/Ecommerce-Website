// package com.example.backend.security;

// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.Mockito.*;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.access.AccessDeniedException;

// import com.example.backend.entity.Role;
// import com.example.backend.entity.Tenant;
// import com.example.backend.entity.User;

// @ExtendWith(MockitoExtension.class)
// class TenantAccessServiceTest {

//   @Mock
//   private CurrentUserService currentUserService;

//   @InjectMocks
//   private TenantAccessService tenantAccessService;

//   private Tenant nike;
//   private Tenant adidas;

//   private Role tenantRole;
//   private Role adminRole;
//   private Role userRole;

//   private User nikeUser;
//   private User adminUser;
//   private User normalUser;

//   @BeforeEach
//   void setUp() {

//     // Create Nike tenant
//     nike = new Tenant();
//     nike.setId(1L);
//     nike.setName("Nike");
//     nike.setDomain("nike");

//     // Create Adidas tenant
//     adidas = new Tenant();
//     adidas.setId(2L);
//     adidas.setName("Adidas");
//     adidas.setDomain("adidas");

//     // Create TENANT role
//     tenantRole = new Role();
//     tenantRole.setId(1L);
//     tenantRole.setName("TENANT");

//     // Create ADMIN role
//     adminRole = new Role();
//     adminRole.setId(2L);
//     adminRole.setName("ADMIN");

//     // Create USER role
//     userRole = new Role();
//     userRole.setId(3L);
//     userRole.setName("USER");

//     // Nike tenant user
//     nikeUser = new User();
//     nikeUser.setId(100L);
//     nikeUser.setUsername("nikeuser");
//     nikeUser.setTenant(nike);
//     nikeUser.setRole(tenantRole);

//     // Platform admin
//     adminUser = new User();
//     adminUser.setId(200L);
//     adminUser.setUsername("adminuser");
//     adminUser.setTenant(null);
//     adminUser.setRole(adminRole);

//     // Normal user
//     normalUser = new User();
//     normalUser.setId(300L);
//     normalUser.setUsername("normaluser");
//     normalUser.setTenant(null);
//     normalUser.setRole(userRole);
//   }

//   // Test that a tenant user can access their own tenant
//   @Test
//   void shouldAllowUserToAccessOwnTenant() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(nikeUser);

//     assertDoesNotThrow(() -> tenantAccessService.validateTenantAccess("nike"));

//     verify(currentUserService)
//         .getCurrentUser();
//   }

//   // Test that a tenant user cannot access another tenant
//   @Test
//   void shouldDenyUserAccessToAnotherTenant() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(nikeUser);

//     assertThrows(
//         AccessDeniedException.class,
//         () -> tenantAccessService.validateTenantAccess("adidas"));

//     verify(currentUserService)
//         .getCurrentUser();
//   }

//   // Test that tenant comparison is case-insensitive
//   @Test
//   void shouldAllowAccessWhenTenantDomainDiffersOnlyByCase() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(nikeUser);

//     assertDoesNotThrow(() -> tenantAccessService.validateTenantAccess("NIKE"));
//   }

//   // Test that platform admin can access any tenant
//   @Test
//   void shouldAllowAdminToAccessAnyTenant() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(adminUser);

//     assertDoesNotThrow(() -> tenantAccessService.validateTenantAccess("nike"));

//     assertDoesNotThrow(() -> tenantAccessService.validateTenantAccess("adidas"));

//     assertDoesNotThrow(() -> tenantAccessService.validateTenantAccess("any-tenant"));
//   }

//   // Test that a normal user without a tenant cannot access a tenant-scoped
//   // endpoint
//   @Test
//   void shouldDenyUserWhenUserHasNoTenant() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(normalUser);

//     assertThrows(
//         AccessDeniedException.class,
//         () -> tenantAccessService.validateTenantAccess("nike"));
//   }

//   // Test that tenant user cannot access another tenant even if the tenant exists
//   @Test
//   void shouldStrictlyEnforceTenantIsolation() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(nikeUser);

//     assertThrows(
//         AccessDeniedException.class,
//         () -> tenantAccessService.validateTenantAccess("adidas"));

//     assertThrows(
//         AccessDeniedException.class,
//         () -> tenantAccessService.validateTenantAccess("amazon"));

//     assertThrows(
//         AccessDeniedException.class,
//         () -> tenantAccessService.validateTenantAccess("puma"));
//   }
// }

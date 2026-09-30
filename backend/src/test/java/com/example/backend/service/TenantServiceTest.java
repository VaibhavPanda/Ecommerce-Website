// package com.example.backend.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;

// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.example.backend.entity.Tenant;
// import com.example.backend.exception.ResourceAlreadyExistsException;
// import com.example.backend.exception.ResourceNotFoundException;
// import com.example.backend.repository.TenantRepository;

// @ExtendWith(MockitoExtension.class)
// class TenantServiceTest {

//   @Mock
//   private TenantRepository tenantRepository;

//   @InjectMocks
//   private TenantService tenantService;

//   private Tenant tenant;

//   @BeforeEach
//   void setUp() {

//     tenant = new Tenant();
//     tenant.setId(1L);
//     tenant.setName("Nike");
//     tenant.setDomain("nike");
//   }

//   // Test successful tenant creation
//   @Test
//   void shouldCreateTenantSuccessfully() {

//     when(tenantRepository.existsByName("Nike"))
//         .thenReturn(false);

//     when(tenantRepository.existsByDomain("nike"))
//         .thenReturn(false);

//     when(tenantRepository.save(any(Tenant.class)))
//         .thenReturn(tenant);

//     Tenant result = tenantService.createTenant(tenant);

//     assertNotNull(result);
//     assertEquals(1L, result.getId());
//     assertEquals("Nike", result.getName());
//     assertEquals("nike", result.getDomain());

//     verify(tenantRepository)
//         .save(tenant);
//   }

//   // Test duplicate tenant name rejection
//   @Test
//   void shouldThrowExceptionWhenTenantNameAlreadyExists() {

//     when(tenantRepository.existsByName("Nike"))
//         .thenReturn(true);

//     assertThrows(
//         ResourceAlreadyExistsException.class,
//         () -> tenantService.createTenant(tenant));

//     verify(tenantRepository, never())
//         .save(any(Tenant.class));

//     verify(tenantRepository, never())
//         .existsByDomain(anyString());
//   }

//   // Test duplicate tenant domain rejection
//   @Test
//   void shouldThrowExceptionWhenTenantDomainAlreadyExists() {

//     when(tenantRepository.existsByName("Nike"))
//         .thenReturn(false);

//     when(tenantRepository.existsByDomain("nike"))
//         .thenReturn(true);

//     assertThrows(
//         ResourceAlreadyExistsException.class,
//         () -> tenantService.createTenant(tenant));

//     verify(tenantRepository, never())
//         .save(any(Tenant.class));
//   }

//   // Test retrieving all tenants
//   @Test
//   void shouldGetAllTenantsSuccessfully() {

//     Tenant adidas = new Tenant();
//     adidas.setId(2L);
//     adidas.setName("Adidas");
//     adidas.setDomain("adidas");

//     List<Tenant> tenants = List.of(tenant, adidas);

//     when(tenantRepository.findAll())
//         .thenReturn(tenants);

//     List<Tenant> result = tenantService.getAllTenants();

//     assertNotNull(result);
//     assertEquals(2, result.size());
//     assertEquals("Nike", result.get(0).getName());
//     assertEquals("Adidas", result.get(1).getName());

//     verify(tenantRepository)
//         .findAll();
//   }

//   // Test retrieving tenant by ID
//   @Test
//   void shouldGetTenantByIdSuccessfully() {

//     when(tenantRepository.findById(1L))
//         .thenReturn(Optional.of(tenant));

//     Tenant result = tenantService.getTenantById(1L);

//     assertNotNull(result);
//     assertEquals(1L, result.getId());
//     assertEquals("Nike", result.getName());

//     verify(tenantRepository)
//         .findById(1L);
//   }

//   // Test tenant not found when searching by ID
//   @Test
//   void shouldThrowExceptionWhenTenantIdDoesNotExist() {

//     when(tenantRepository.findById(999L))
//         .thenReturn(Optional.empty());

//     assertThrows(
//         ResourceNotFoundException.class,
//         () -> tenantService.getTenantById(999L));
//   }

//   // Test retrieving tenant by domain
//   @Test
//   void shouldGetTenantByDomainSuccessfully() {

//     when(tenantRepository.findByDomain("nike"))
//         .thenReturn(Optional.of(tenant));

//     Tenant result = tenantService.getTenantByDomain("nike");

//     assertNotNull(result);
//     assertEquals("Nike", result.getName());
//     assertEquals("nike", result.getDomain());

//     verify(tenantRepository)
//         .findByDomain("nike");
//   }

//   // Test tenant not found when searching by domain
//   @Test
//   void shouldThrowExceptionWhenTenantDomainDoesNotExist() {

//     when(tenantRepository.findByDomain("unknown"))
//         .thenReturn(Optional.empty());

//     assertThrows(
//         ResourceNotFoundException.class,
//         () -> tenantService.getTenantByDomain("unknown"));
//   }

//   // Test successful tenant deletion
//   @Test
//   void shouldDeleteTenantSuccessfully() {

//     when(tenantRepository.findById(1L))
//         .thenReturn(Optional.of(tenant));

//     tenantService.deleteTenant(1L);

//     verify(tenantRepository)
//         .findById(1L);

//     verify(tenantRepository)
//         .delete(tenant);
//   }

//   // Test deletion when tenant does not exist
//   @Test
//   void shouldThrowExceptionWhenDeletingNonExistingTenant() {

//     when(tenantRepository.findById(999L))
//         .thenReturn(Optional.empty());

//     assertThrows(
//         ResourceNotFoundException.class,
//         () -> tenantService.deleteTenant(999L));

//     verify(tenantRepository, never())
//         .delete(any(Tenant.class));
//   }
// }

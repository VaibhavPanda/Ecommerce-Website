package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.entity.Tenant;
import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.TenantRepository;

@Service
public class TenantService {

  private final TenantRepository tenantRepository;

  public TenantService(TenantRepository tenantRepository) {
    this.tenantRepository = tenantRepository;
  }

  //create tenant
  public Tenant createTenant(Tenant tenant) {

    if (tenantRepository.existsByName(tenant.getName())) {
      throw new ResourceAlreadyExistsException(
          "Tenant name already exists");
    }

    if (tenantRepository.existsByDomain(tenant.getDomain())) {
      throw new ResourceAlreadyExistsException(
          "Tenant domain already exists");
    }

    // New tenants must always start active.
    tenant.setActive(true);

    return tenantRepository.save(tenant);
  }


  public List<Tenant> getAllTenants() {
    return tenantRepository.findAll();
  }


  public Tenant getTenantById(Long id) {

    return tenantRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Tenant not found"));
  }


  public Tenant getTenantByDomain(String domain) {

    return tenantRepository.findByDomain(domain)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Tenant not found"));
  }

  //soft deletion of tenant tenant -> inactive
  public void deleteTenant(Long id) {

    Tenant tenant = getTenantById(id);

    if (!tenant.isActive()) {
      throw new ResourceAlreadyExistsException(
          "Tenant is already inactive");
    }

    tenant.setActive(false);

    tenantRepository.save(tenant);
  }

  // tenant -> inactive
  public void activateTenant(Long id) {

    Tenant tenant = getTenantById(id);

    if (tenant.isActive()) {
      throw new ResourceAlreadyExistsException(
          "Tenant is already active");
    }

    tenant.setActive(true);

    tenantRepository.save(tenant);
  }

  //check for distinct name + domain = tenantID for creating the tenant if exists the activate orElse create
  public Tenant createOrReactivateTenant(
      String name,
      String domain) {

    Tenant existingByName = tenantRepository
        .findByName(name)
        .orElse(null);

    Tenant existingByDomain = tenantRepository
        .findByDomain(domain)
        .orElse(null);

    if (existingByName != null &&
        existingByDomain != null &&
        existingByName.getId().equals(existingByDomain.getId())) {

      if (existingByName.isActive()) {
        throw new ResourceAlreadyExistsException(
            "Tenant already exists and is active");
      }

      existingByName.setActive(true);

      return tenantRepository.save(existingByName);
    }

    if (existingByName != null) {
      throw new ResourceAlreadyExistsException(
          "Tenant name already exists");
    }

    if (existingByDomain != null) {
      throw new ResourceAlreadyExistsException(
          "Tenant domain already exists");
    }

    Tenant tenant = new Tenant();

    tenant.setName(name);
    tenant.setDomain(domain);
    tenant.setActive(true);

    return tenantRepository.save(tenant);
  }

}

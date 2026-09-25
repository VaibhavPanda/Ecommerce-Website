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

    public Tenant createTenant(Tenant tenant) {

        if (tenantRepository.existsByName(tenant.getName())) {
            throw new ResourceAlreadyExistsException("Tenant name already exists");
        }

        if (tenantRepository.existsByDomain(tenant.getDomain())) {
            throw new ResourceAlreadyExistsException("Tenant domain already exists");
        }

        return tenantRepository.save(tenant);
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public Tenant getTenantById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tenant not found"));
    }

    public Tenant getTenantByDomain(String domain) {
        return tenantRepository.findByDomain(domain)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tenant not found"));
    }

    public void deleteTenant(Long id) {

        Tenant tenant = getTenantById(id);

        tenantRepository.delete(tenant);
    }
}

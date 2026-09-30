package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entity.Tenant;
import com.example.backend.service.TenantService;

@RestController
@RequestMapping("/api/admin/tenants")
@PreAuthorize("hasRole('ADMIN')")
public class TenantController {

  private final TenantService tenantService;

  public TenantController(TenantService tenantService) {
    this.tenantService = tenantService;
  }

  @PostMapping
  public ResponseEntity<Tenant> createTenant(
      @RequestBody Tenant tenant) {

    Tenant createdTenant = tenantService.createTenant(tenant);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdTenant);
  }

  @GetMapping
  public ResponseEntity<List<Tenant>> getAllTenants() {

    return ResponseEntity.ok(
        tenantService.getAllTenants());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tenant> getTenantById(
      @PathVariable Long id) {

    return ResponseEntity.ok(
        tenantService.getTenantById(id));
  }

  @GetMapping("/domain/{domain}")
  public ResponseEntity<Tenant> getTenantByDomain(
      @PathVariable String domain) {

    return ResponseEntity.ok(
        tenantService.getTenantByDomain(domain));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTenant(
      @PathVariable Long id) {

    tenantService.deleteTenant(id);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/activate")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> activateTenant(@PathVariable Long id) {

    tenantService.activateTenant(id);

    return ResponseEntity.noContent().build();
  }
}

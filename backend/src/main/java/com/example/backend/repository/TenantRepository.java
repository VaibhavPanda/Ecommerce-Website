package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByDomain(String domain);

    Optional<Tenant> findByName(String name);

    //uniqueness
    boolean existsByName(String name);

    boolean existsByDomain(String domain);
}

package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.backend.entity.Category;
import com.example.backend.entity.Tenant;

public interface CategoryRepository
    extends JpaRepository<Category, Long> {

  // TENANT-SCOPED QUERIES


  List<Category> findByTenant(Tenant tenant);

  Optional<Category> findByIdAndTenant(
      Long id,
      Tenant tenant);

  boolean existsByNameAndTenant(
      String name,
      Tenant tenant);

  // PUBLIC CATEGORY QUERY

  @Query("""
      SELECT DISTINCT c.name
      FROM Category c
      WHERE c.tenant.isActive = true
      ORDER BY c.name
      """)
  List<String> findDistinctActiveCategoryNames();
}

package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Category;
import com.example.backend.entity.Tenant;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByTenant(Tenant tenant);

    Optional<Category> findByIdAndTenant(Long id, Tenant tenant);

    boolean existsByNameAndTenant(String name, Tenant tenant);

    @Query("""
        SELECT DISTINCT c.name
        FROM Category c
        ORDER BY c.name
        """)
    List<String> findDistinctCategoryNames();
}

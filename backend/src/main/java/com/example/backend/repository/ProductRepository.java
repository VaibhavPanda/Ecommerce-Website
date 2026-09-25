package com.example.backend.repository;

import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.entity.Product;
import com.example.backend.entity.Tenant;

public interface ProductRepository
        extends JpaRepository<Product, Long> {


    //tenant filtered
    Page<Product> findByTenant(
            Tenant tenant,
            Pageable pageable
    );

    Page<Product> findByTenantAndNameContainingIgnoreCase(
            Tenant tenant,
            String name,
            Pageable pageable
    );

    Page<Product> findByTenantAndCategoryId(
            Tenant tenant,
            Long categoryId,
            Pageable pageable
    );

    @Query("""
            SELECT p
            FROM Product p
            WHERE p.tenant = :tenant
              AND p.category.id = :categoryId
              AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Page<Product> searchByTenantAndCategory(
            @Param("tenant") Tenant tenant,
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            Pageable pageable
    );

    Optional<Product> findByIdAndTenant(
            Long id,
            Tenant tenant
    );


    //for public access
    Page<Product> findByNameContainingIgnoreCase(
        String name,
        Pageable pageable);

    Page<Product> findByCategoryId(
        Long categoryId,
        Pageable pageable);

    @Query("""
        SELECT p
        FROM Product p
        WHERE p.category.id = :categoryId
          AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
        """)
    Page<Product> searchByCategory(
        @Param("categoryId") Long categoryId,
        @Param("name") String name,
        Pageable pageable);

        Page<Product> findByCategory_NameIgnoreCase(
        String categoryName,
        Pageable pageable);

        @Query("""
            SELECT p
            FROM Product p
            WHERE LOWER(p.category.name) = LOWER(:categoryName)
              AND LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
        Page<Product> searchByCategoryName(
            @Param("categoryName") String categoryName,
            @Param("search") String search,
            Pageable pageable);

          // PESSIMISTIC_WRITE
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT p FROM Product p WHERE p.id = :id")
        Optional<Product> findByIdForUpdate(@Param("id") Long id);
}

package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.category.CategoryRequest;
import com.example.backend.dto.category.UpdateCategoryRequest;
import com.example.backend.entity.Category;
import com.example.backend.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/{tenant}/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping
  @PreAuthorize("hasRole('TENANT')")
  public Category createCategory(
      @PathVariable String tenant,
      @Valid @RequestBody CategoryRequest request) {

    return categoryService.createCategory(
        tenant,
        request.getName());
  }

  @GetMapping
  public ResponseEntity<List<Category>> getCategories(
      @PathVariable String tenant) {

    return ResponseEntity.ok(
        categoryService.getCategories(tenant));
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<Category> getCategory(
      @PathVariable String tenant,
      @PathVariable Long categoryId) {

    return ResponseEntity.ok(
        categoryService.getCategory(
            tenant,
            categoryId));
  }

  @DeleteMapping("/{categoryId}")
  @PreAuthorize("hasRole('TENANT')")
  public ResponseEntity<Void> deleteCategory(
      @PathVariable String tenant,
      @PathVariable Long categoryId) {

    categoryService.deleteCategory(
        tenant,
        categoryId);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{categoryId}")
  @PreAuthorize("hasRole('TENANT')")
  public Category updateCategory(
      @PathVariable String tenant,
      @PathVariable Long categoryId,
      @Valid @RequestBody CategoryRequest request) {

    return categoryService.updateCategory(
        tenant,
        categoryId,
        request.getName());
  }
}

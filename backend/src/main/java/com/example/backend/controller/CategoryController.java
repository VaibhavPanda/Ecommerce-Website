package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.category.CategoryRequest;
import com.example.backend.dto.category.CategoryResponse;
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
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('TENANT')")
  public CategoryResponse createCategory(
      @PathVariable String tenant,
      @Valid @RequestBody CategoryRequest request) {

    Category category = categoryService.createCategory(
        tenant,
        request.getName());

    return mapToResponse(category);
  }

  @GetMapping
  public List<CategoryResponse> getCategories(
      @PathVariable String tenant) {

    return categoryService
        .getCategories(tenant)
        .stream()
        .map(this::mapToResponse)
        .toList();
  }

  @GetMapping("/{categoryId}")
  public CategoryResponse getCategory(
      @PathVariable String tenant,
      @PathVariable Long categoryId) {

    Category category = categoryService.getCategory(
        tenant,
        categoryId);

    return mapToResponse(category);
  }

  @PutMapping("/{categoryId}")
  @PreAuthorize("hasRole('TENANT')")
  public CategoryResponse updateCategory(
      @PathVariable String tenant,
      @PathVariable Long categoryId,
      @Valid @RequestBody CategoryRequest request) {

    Category category = categoryService.updateCategory(
        tenant,
        categoryId,
        request.getName());

    return mapToResponse(category);
  }

  @DeleteMapping("/{categoryId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('TENANT')")
  public void deleteCategory(
      @PathVariable String tenant,
      @PathVariable Long categoryId) {

    categoryService.deleteCategory(
        tenant,
        categoryId);
  }

  private CategoryResponse mapToResponse(Category category) {
    return new CategoryResponse(
        category.getId(),
        category.getName());
  }
}

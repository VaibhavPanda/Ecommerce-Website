package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.favorite.FavoriteResponse;
import com.example.backend.entity.Favorite;
import com.example.backend.service.FavoriteService;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // Add a product to the current user's favorites.
    @PostMapping("/{productId}")
@ResponseStatus(HttpStatus.CREATED)
@PreAuthorize("hasAnyRole('USER', 'TENANT')")
public FavoriteResponse addFavorite(
        @PathVariable Long productId) {

    return favoriteService.addFavorite(productId);
}

    // Remove a product from the current user's favorites.
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'TENANT')")
    public void removeFavorite(
            @PathVariable Long productId) {

        favoriteService.removeFavorite(productId);
    }

    // Get all favorites belonging to the current user.
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'TENANT')")
    public List<FavoriteResponse> getMyFavorites() {

        return favoriteService.getMyFavorites();
    }
}

package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.favorite.FavoriteResponse;
import com.example.backend.entity.Favorite;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.FavoriteRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.security.CurrentUserService;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            ProductRepository productRepository,
            CurrentUserService currentUserService) {

        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public FavoriteResponse addFavorite(Long productId) {

        User currentUser = currentUserService.getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        if (favoriteRepository.existsByUserAndProduct(currentUser, product)) {
            throw new ResourceAlreadyExistsException(
                    "Product is already in favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(currentUser);
        favorite.setProduct(product);

        Favorite savedFavorite =  favoriteRepository.save(favorite);

        return new FavoriteResponse(
            savedFavorite.getId(),
            product.getId(),
            product.getName(),
            product.getPrice());
    }

    @Transactional
    public void removeFavorite(Long productId) {

        User currentUser = currentUserService.getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Favorite favorite = favoriteRepository
                .findByUserAndProduct(currentUser, product)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product is not in favorites"));

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
public List<FavoriteResponse> getMyFavorites() {

    User currentUser = currentUserService.getCurrentUser();

    return favoriteRepository.findByUserOrderByIdDesc(currentUser)
            .stream()
            .map(favorite -> new FavoriteResponse(
                    favorite.getId(),
                    favorite.getProduct().getId(),
                    favorite.getProduct().getName(),
                    favorite.getProduct().getPrice()
            ))
            .toList();
}
}

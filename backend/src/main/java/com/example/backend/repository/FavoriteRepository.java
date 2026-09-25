package com.example.backend.repository;

import com.example.backend.entity.Favorite;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  boolean existsByUserAndProduct(User user, Product product);

  Optional<Favorite> findByUserAndProduct(User user, Product product);

  List<Favorite> findByUserOrderByIdDesc(User user);
}

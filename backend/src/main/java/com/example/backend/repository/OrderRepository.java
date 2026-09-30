package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Order;
import com.example.backend.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

  //My Order History
  List<Order> findByUserOrderByIdDesc(User user);

  //security check acc to order ID and user ID
  Optional<Order> findByIdAndUser(Long id, User user);
}

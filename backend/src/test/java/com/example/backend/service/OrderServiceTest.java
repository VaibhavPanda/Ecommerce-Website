// package com.example.backend.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// import java.math.BigDecimal;
// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.example.backend.dto.order.CreateOrderRequest;
// import com.example.backend.dto.order.OrderItemRequest;
// import com.example.backend.dto.order.OrderResponse;
// import com.example.backend.entity.Product;
// import com.example.backend.entity.User;
// import com.example.backend.exception.InsufficientStockException;
// import com.example.backend.repository.OrderRepository;
// import com.example.backend.repository.ProductRepository;
// import com.example.backend.security.CurrentUserService;

// @ExtendWith(MockitoExtension.class)
// class OrderServiceTest {

//   @Mock
//   private OrderRepository orderRepository;

//   @Mock
//   private ProductRepository productRepository;

//   @Mock
//   private CurrentUserService currentUserService;

//   @InjectMocks
//   private OrderService orderService;

//   private User user;
//   private Product product;

//   @BeforeEach
//   void setUp() {

//     user = new User();
//     user.setId(1L);
//     user.setUsername("testuser");

//     product = new Product();
//     product.setId(1L);
//     product.setName("Nike Shoes");
//     product.setPrice(new BigDecimal("2000.00"));
//     product.setQuantity(10);
//   }

//   @Test
//   void shouldCreateOrderSuccessfully() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(user);

//     when(productRepository.findByIdForUpdate(1L))
//         .thenReturn(Optional.of(product));

//     when(orderRepository.save(any()))
//         .thenAnswer(invocation -> {

//           var order = invocation.getArgument(0,
//               com.example.backend.entity.Order.class);

//           order.setId(100L);

//           return order;
//         });

//     OrderItemRequest itemRequest = new OrderItemRequest();
//     itemRequest.setProductId(1L);
//     itemRequest.setQuantity(2);

//     CreateOrderRequest request = new CreateOrderRequest();
//     request.setItems(List.of(itemRequest));

//     OrderResponse response = orderService.createOrder(request);

//     assertNotNull(response);

//     assertEquals(100L, response.getOrderId());

//     assertEquals(2, response.getTotalQuantity());

//     assertEquals(
//         new BigDecimal("4000.00"),
//         response.getTotalAmount());

//     assertEquals(8, product.getQuantity());

//     verify(productRepository).findByIdForUpdate(1L);

//     verify(orderRepository).save(any());
//   }

//   @Test
//   void shouldRejectOrderWhenQuantityIsNotAvailable() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(user);

//     when(productRepository.findByIdForUpdate(1L))
//         .thenReturn(Optional.of(product));

//     OrderItemRequest itemRequest = new OrderItemRequest();
//     itemRequest.setProductId(1L);
//     itemRequest.setQuantity(10);

//     CreateOrderRequest request = new CreateOrderRequest();
//     request.setItems(List.of(itemRequest));

//     assertThrows(
//         InsufficientStockException.class,
//         () -> orderService.createOrder(request));

//     assertEquals(10, product.getQuantity());

//     verify(orderRepository, never()).save(any());
//   }

//   @Test
//   void shouldRejectOrderWhenProductDoesNotExist() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(user);

//     when(productRepository.findByIdForUpdate(999L))
//         .thenReturn(Optional.empty());

//     OrderItemRequest itemRequest = new OrderItemRequest();
//     itemRequest.setProductId(999L);
//     itemRequest.setQuantity(1);

//     CreateOrderRequest request = new CreateOrderRequest();
//     request.setItems(List.of(itemRequest));

//     assertThrows(
//         com.example.backend.exception.ResourceNotFoundException.class,
//         () -> orderService.createOrder(request));

//     verify(orderRepository, never()).save(any());
//   }

//   @Test
//   void shouldReturnCurrentUsersOrders() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(user);

//     com.example.backend.entity.Order order = new com.example.backend.entity.Order();

//     order.setId(100L);
//     order.setUser(user);
//     order.setTotalQuantity(2);
//     order.setTotalAmount(new BigDecimal("4000.00"));
//     order.setOrderDate(java.time.LocalDateTime.now());
//     order.setOrderItems(List.of());

//     when(orderRepository.findByUserOrderByIdDesc(user))
//         .thenReturn(List.of(order));

//     List<OrderResponse> responses = orderService.getMyOrders();

//     assertEquals(1, responses.size());

//     assertEquals(
//         100L,
//         responses.get(0).getOrderId());

//     assertEquals(
//         new BigDecimal("4000.00"),
//         responses.get(0).getTotalAmount());

//     verify(orderRepository)
//         .findByUserOrderByIdDesc(user);
//   }

//   @Test
//   void shouldRejectOrderBelongingToAnotherUser() {

//     when(currentUserService.getCurrentUser())
//         .thenReturn(user);

//     when(orderRepository.findByIdAndUser(999L, user))
//         .thenReturn(Optional.empty());

//     assertThrows(
//         com.example.backend.exception.ResourceNotFoundException.class,
//         () -> orderService.getMyOrder(999L));
//   }
// }

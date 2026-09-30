// package com.example.backend.config;

// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Primary;
// import org.testcontainers.containers.MySQLContainer;

// @TestConfiguration
// public class TestDatabaseConfig {

//     @Bean
//     @Primary
//     public MySQLContainer<?> mysqlContainer() {

//         MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
//                 .withDatabaseName("ecommerce_test")
//                 .withUsername("test")
//                 .withPassword("test");

//         mysql.start();

//         return mysql;
//     }
// }

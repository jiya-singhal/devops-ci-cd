package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class
 * 
 * This is the entry point of our Spring Boot application.
 * The @SpringBootApplication annotation tells Spring Boot to:
 * 1. Enable auto-configuration
 * 2. Enable component scanning
 * 3. Define this as a configuration class
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        // This line starts our Spring Boot application
        SpringApplication.run(DemoApplication.class, args);
    }
}

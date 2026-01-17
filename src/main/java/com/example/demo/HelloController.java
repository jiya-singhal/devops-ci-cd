package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello Controller
 * 
 * This is a REST Controller that handles incoming HTTP requests.
 * 
 * @RestController tells Spring this class will handle web requests
 * and return data (not HTML pages).
 */
@RestController
public class HelloController {

    /**
     * Health Check Endpoint
     * 
     * This endpoint is crucial for CI/CD pipelines!
     * It allows us to verify that our application is running correctly.
     * 
     * URL: GET /health
     * Returns: "OK" if the application is healthy
     */
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    /**
     * Hello Endpoint
     * 
     * A simple greeting endpoint that demonstrates basic functionality.
     * 
     * URL: GET /hello
     * URL: GET /hello?name=YourName
     * Returns: A greeting message
     */
    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s! Welcome to DevOps CI/CD Demo.", name);
    }

    /**
     * Version Endpoint
     * 
     * Returns the current version of the application.
     * Useful for deployment verification.
     * 
     * URL: GET /version
     * Returns: Current application version
     */
    @GetMapping("/version")
    public String getVersion() {
        return "1.0.0";
    }
}

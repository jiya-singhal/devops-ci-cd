package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Hello Controller Integration Tests
 * 
 * These are INTEGRATION TESTS - they test how different parts of the 
 * application work together, specifically testing our HTTP endpoints.
 * 
 * WHY INTEGRATION TESTS MATTER:
 * 1. They verify the full request/response cycle works
 * 2. They catch issues that unit tests might miss
 * 3. They simulate real user interactions
 * 
 * @SpringBootTest starts the full application context
 * @AutoConfigureMockMvc provides a MockMvc instance for testing HTTP requests
 */
@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Health endpoint returns OK")
    void healthEndpointReturnsOk() throws Exception {
        // This test is CRITICAL for CI/CD!
        // It verifies our application starts and responds correctly
        mockMvc.perform(get("/health"))
               .andExpect(status().isOk())
               .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("Hello endpoint returns greeting with default name")
    void helloEndpointWithDefaultName() throws Exception {
        mockMvc.perform(get("/hello"))
               .andExpect(status().isOk())
               .andExpect(content().string("Hello, World! Welcome to DevOps CI/CD Demo."));
    }

    @Test
    @DisplayName("Hello endpoint returns greeting with custom name")
    void helloEndpointWithCustomName() throws Exception {
        mockMvc.perform(get("/hello?name=Student"))
               .andExpect(status().isOk())
               .andExpect(content().string("Hello, Student! Welcome to DevOps CI/CD Demo."));
    }

    @Test
    @DisplayName("Version endpoint returns version number")
    void versionEndpointReturnsVersion() throws Exception {
        mockMvc.perform(get("/version"))
               .andExpect(status().isOk())
               .andExpect(content().string("1.0.0"));
    }
}

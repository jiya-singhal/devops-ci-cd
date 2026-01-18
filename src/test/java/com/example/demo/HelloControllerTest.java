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

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Health endpoint returns OK")
    void healthEndpointReturnsOk() throws Exception {
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

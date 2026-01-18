package com.example.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests de Integración del Controlador")
class ControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Reset del estado entre tests
    }

    @Test
    @DisplayName("GET / retorna página principal")
    void testIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("GET /users retorna página de usuarios")
    void testUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"));
    }

    @Test
    @DisplayName("GET /monitoring retorna página de monitoreo")
    void testMonitoringPage() throws Exception {
        mockMvc.perform(get("/monitoring"))
                .andExpect(status().isOk())
                .andExpect(view().name("monitoring"));
    }

    @Test
    @DisplayName("POST /api/users crea nuevo usuario")
    void testCreateUser() throws Exception {
        User user = new User(null, "Test User", "test@example.com", 25);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    @DisplayName("POST /api/users falla con usuario inválido")
    void testCreateInvalidUser() throws Exception {
        User user = new User(null, "", "test@example.com", 25);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/users/{id} obtiene usuario")
    void testGetUser() throws Exception {
        // Crear usuario primero
        User user = new User(null, "Test User", "test@example.com", 25);
        String response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        User createdUser = objectMapper.readValue(response, User.class);
        
        // Obtener usuario
        mockMvc.perform(get("/api/users/" + createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    @DisplayName("GET /api/users retorna lista de usuarios")
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} elimina usuario")
    void testDeleteUser() throws Exception {
        // Crear usuario
        User user = new User(null, "Test User", "test@example.com", 25);
        String response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        User createdUser = objectMapper.readValue(response, User.class);
        
        // Eliminar usuario
        mockMvc.perform(delete("/api/users/" + createdUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Prometheus endpoint accesible")
    void testPrometheusEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}

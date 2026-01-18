package com.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de Validación de Usuario")
class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Usuario válido con datos correctos")
    void testValidUser() {
        user.setName("Juan Pérez");
        user.setEmail("juan@example.com");
        user.setAge(30);
        
        assertTrue(user.isValid());
    }

    @Test
    @DisplayName("Usuario inválido por nombre vacío")
    void testInvalidUserEmptyName() {
        user.setName("");
        user.setEmail("juan@example.com");
        user.setAge(30);
        
        assertFalse(user.isValid());
    }

    @Test
    @DisplayName("Usuario inválido por email sin @")
    void testInvalidUserBadEmail() {
        user.setName("Juan Pérez");
        user.setEmail("juanexample.com");
        user.setAge(30);
        
        assertFalse(user.isValid());
    }

    @Test
    @DisplayName("Usuario inválido por edad fuera de rango")
    void testInvalidUserBadAge() {
        user.setName("Juan Pérez");
        user.setEmail("juan@example.com");
        user.setAge(200);
        
        assertFalse(user.isValid());
    }

    @Test
    @DisplayName("Usuario inválido por edad negativa")
    void testInvalidUserNegativeAge() {
        user.setName("Juan Pérez");
        user.setEmail("juan@example.com");
        user.setAge(-5);
        
        assertFalse(user.isValid());
    }

    @Test
    @DisplayName("Getters y Setters funcionan correctamente")
    void testGettersAndSetters() {
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(25);
        
        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(25, user.getAge());
    }
}

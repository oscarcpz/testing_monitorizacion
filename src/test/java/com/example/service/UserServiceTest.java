package com.example.service;

import com.example.model.User;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests del Servicio de Usuarios")
class UserServiceTest {
    private UserService userService;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        userService = new UserService(meterRegistry);
    }

    @Test
    @DisplayName("Crear usuario exitosamente")
    void testCreateUser() {
        User user = new User(null, "Juan Pérez", "juan@example.com", 30);
        User createdUser = userService.createUser(user);

        assertNotNull(createdUser.getId());
        assertEquals("Juan Pérez", createdUser.getName());
        assertEquals("juan@example.com", createdUser.getEmail());
        assertEquals(30, createdUser.getAge());
    }

    @Test
    @DisplayName("Fallo al crear usuario inválido")
    void testCreateInvalidUser() {
        User user = new User(null, "", "juan@example.com", 30);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    @DisplayName("Obtener usuario por ID")
    void testGetUserById() {
        User user = new User(null, "Juan Pérez", "juan@example.com", 30);
        User createdUser = userService.createUser(user);
        
        User retrieved = userService.getUserById(createdUser.getId());
        
        assertEquals(createdUser.getId(), retrieved.getId());
        assertEquals("Juan Pérez", retrieved.getName());
    }

    @Test
    @DisplayName("Error al obtener usuario inexistente")
    void testGetUserByIdNotFound() {
        assertThrows(RuntimeException.class, () -> userService.getUserById(999L));
    }

    @Test
    @DisplayName("Obtener todos los usuarios")
    void testGetAllUsers() {
        userService.createUser(new User(null, "Juan", "juan@example.com", 30));
        userService.createUser(new User(null, "María", "maria@example.com", 25));
        userService.createUser(new User(null, "Pedro", "pedro@example.com", 35));

        List<User> users = userService.getAllUsers();

        assertEquals(3, users.size());
    }

    @Test
    @DisplayName("Actualizar usuario exitosamente")
    void testUpdateUser() {
        User user = new User(null, "Juan", "juan@example.com", 30);
        User createdUser = userService.createUser(user);
        
        User updatedData = new User(null, "Juan Actualizado", "juan.updated@example.com", 31);
        User updated = userService.updateUser(createdUser.getId(), updatedData);

        assertEquals("Juan Actualizado", updated.getName());
        assertEquals("juan.updated@example.com", updated.getEmail());
        assertEquals(31, updated.getAge());
    }

    @Test
    @DisplayName("Error al actualizar usuario inexistente")
    void testUpdateUserNotFound() {
        User updatedData = new User(null, "Juan", "juan@example.com", 30);
        
        assertThrows(RuntimeException.class, () -> userService.updateUser(999L, updatedData));
    }

    @Test
    @DisplayName("Eliminar usuario exitosamente")
    void testDeleteUser() {
        User user = new User(null, "Juan", "juan@example.com", 30);
        User createdUser = userService.createUser(user);
        
        userService.deleteUser(createdUser.getId());
        
        assertThrows(RuntimeException.class, () -> userService.getUserById(createdUser.getId()));
    }

    @Test
    @DisplayName("Error al eliminar usuario inexistente")
    void testDeleteUserNotFound() {
        assertThrows(RuntimeException.class, () -> userService.deleteUser(999L));
    }

    @Test
    @DisplayName("Buscar usuarios por nombre")
    void testSearchByName() {
        userService.createUser(new User(null, "Juan García", "juan@example.com", 30));
        userService.createUser(new User(null, "María López", "maria@example.com", 25));
        userService.createUser(new User(null, "Juan Pérez", "juanperez@example.com", 35));

        List<User> results = userService.searchByName("Juan");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(u -> u.getName().contains("Juan")));
    }

    @Test
    @DisplayName("Búsqueda de nombre sin resultados")
    void testSearchByNameEmpty() {
        userService.createUser(new User(null, "Juan García", "juan@example.com", 30));
        
        List<User> results = userService.searchByName("María");

        assertEquals(0, results.size());
    }

    @Test
    @DisplayName("Búsqueda de nombre insensible a mayúsculas")
    void testSearchByNameCaseInsensitive() {
        userService.createUser(new User(null, "Juan García", "juan@example.com", 30));

        List<User> results = userService.searchByName("juan");

        assertEquals(1, results.size());
        assertEquals("Juan García", results.get(0).getName());
    }
}

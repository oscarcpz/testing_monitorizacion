package com.example.service;

import com.example.model.User;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final MeterRegistry meterRegistry;
    private Long idCounter = 1L;

    public UserService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        initializeMetrics();
    }

    private void initializeMetrics() {
        // Métrica personalizada para contar usuarios
        meterRegistry.gaugeMapSize("users.count", Collections.emptyList(), users);
    }

    public User createUser(User user) {
        if (!user.isValid()) {
            log.warn("Intento de crear usuario inválido: {}", user.getEmail());
            throw new IllegalArgumentException("Usuario inválido");
        }
        
        user.setId(idCounter++);
        users.put(user.getId(), user);
        
        log.info("Usuario creado exitosamente: ID={}, Email={}", user.getId(), user.getEmail());
        meterRegistry.counter("users.created").increment();
        
        return user;
    }

    public User getUserById(Long id) {
        log.debug("Buscando usuario con ID: {}", id);
        User user = users.get(id);
        
        if (user == null) {
            log.warn("Usuario no encontrado: ID={}", id);
            meterRegistry.counter("users.not_found").increment();
            throw new RuntimeException("Usuario no encontrado");
        }
        
        meterRegistry.counter("users.retrieved").increment();
        return user;
    }

    public List<User> getAllUsers() {
        log.info("Obteniendo todos los usuarios. Total: {}", users.size());
        return new ArrayList<>(users.values());
    }

    public User updateUser(Long id, User updatedUser) {
        if (!updatedUser.isValid()) {
            log.warn("Intento de actualizar usuario con datos inválidos: ID={}", id);
            throw new IllegalArgumentException("Usuario inválido");
        }

        User existingUser = users.get(id);
        if (existingUser == null) {
            log.warn("No se puede actualizar usuario no existente: ID={}", id);
            throw new RuntimeException("Usuario no encontrado");
        }

        updatedUser.setId(id);
        users.put(id, updatedUser);
        
        log.info("Usuario actualizado exitosamente: ID={}", id);
        meterRegistry.counter("users.updated").increment();
        
        return updatedUser;
    }

    public void deleteUser(Long id) {
        if (!users.containsKey(id)) {
            log.warn("Intento de eliminar usuario no existente: ID={}", id);
            throw new RuntimeException("Usuario no encontrado");
        }

        users.remove(id);
        log.info("Usuario eliminado exitosamente: ID={}", id);
        meterRegistry.counter("users.deleted").increment();
    }

    public List<User> searchByName(String name) {
        log.info("Buscando usuarios por nombre: {}", name);
        return users.values().stream()
                .filter(u -> u.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}

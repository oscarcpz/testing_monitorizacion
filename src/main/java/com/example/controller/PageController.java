package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class PageController {
    private final UserService userService;

    public PageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model) {
        log.info("Acceso a la página principal");
        model.addAttribute("title", "Aplicación de Monitoreo");
        return "index";
    }

    @GetMapping("/users")
    public String usersPage(Model model) {
        log.info("Acceso a la página de usuarios");
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("userCount", users.size());
        return "users";
    }

    @GetMapping("/monitoring")
    public String monitoringPage(Model model) {
        log.info("Acceso a la página de monitoreo");
        model.addAttribute("title", "Dashboard de Monitoreo");
        return "monitoring";
    }
}

@RestController
@RequestMapping("/api/users")
@Slf4j
class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("POST /api/users - Creando nuevo usuario");
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            log.error("Error al crear usuario", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.info("GET /api/users/{} - Obteniendo usuario", id);
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error al obtener usuario", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("GET /api/users - Obteniendo todos los usuarios");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("PUT /api/users/{} - Actualizando usuario", id);
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error al actualizar usuario", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} - Eliminando usuario", id);
        try {
            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al eliminar usuario", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        log.info("GET /api/users/search - Buscando usuarios por nombre: {}", name);
        List<User> results = userService.searchByName(name);
        return ResponseEntity.ok(results);
    }
}

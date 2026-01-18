# 📡 Ejemplos de API REST

Documentación de todos los endpoints disponibles en la aplicación.

## 🌐 Base URL

```
http://localhost:8080
```

## 📋 Endpoints Disponibles

### 1️⃣ Obtener Todos los Usuarios

**Endpoint:**
```
GET /api/users
```

**Ejemplo:**
```bash
curl -X GET http://localhost:8080/api/users
```

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "age": 30
  },
  {
    "id": 2,
    "name": "María García",
    "email": "maria@example.com",
    "age": 25
  }
]
```

**Respuesta (lista vacía):**
```json
[]
```

---

### 2️⃣ Obtener Usuario por ID

**Endpoint:**
```
GET /api/users/{id}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:8080/api/users/1
```

**Respuesta (200 OK):**
```json
{
  "id": 1,
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "age": 30
}
```

**Respuesta (404 Not Found):**
```json
{
  "error": "User not found with ID: 999",
  "timestamp": "2024-01-15T10:30:45.123Z"
}
```

---

### 3️⃣ Crear Nuevo Usuario

**Endpoint:**
```
POST /api/users
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "Carlos López",
  "email": "carlos@example.com",
  "age": 35
}
```

**Ejemplo completo:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Carlos López",
    "email": "carlos@example.com",
    "age": 35
  }'
```

**Respuesta (201 Created):**
```json
{
  "id": 3,
  "name": "Carlos López",
  "email": "carlos@example.com",
  "age": 35
}
```

**Respuesta (400 Bad Request) - Datos inválidos:**
```json
{
  "error": "Invalid user data: Email must contain @",
  "timestamp": "2024-01-15T10:30:45.123Z"
}
```

---

### 4️⃣ Actualizar Usuario

**Endpoint:**
```
PUT /api/users/{id}
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "Carlos Actualizado",
  "email": "carlos.new@example.com",
  "age": 36
}
```

**Ejemplo completo:**
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Carlos Actualizado",
    "email": "carlos.new@example.com",
    "age": 36
  }'
```

**Respuesta (200 OK):**
```json
{
  "id": 1,
  "name": "Carlos Actualizado",
  "email": "carlos.new@example.com",
  "age": 36
}
```

**Respuesta (404 Not Found):**
```json
{
  "error": "User not found with ID: 999",
  "timestamp": "2024-01-15T10:30:45.123Z"
}
```

---

### 5️⃣ Eliminar Usuario

**Endpoint:**
```
DELETE /api/users/{id}
```

**Ejemplo:**
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

**Respuesta (204 No Content):**
```
(sin contenido)
```

**Respuesta (404 Not Found):**
```json
{
  "error": "User not found with ID: 999",
  "timestamp": "2024-01-15T10:30:45.123Z"
}
```

---

### 6️⃣ Buscar Usuarios por Nombre

**Endpoint:**
```
GET /api/users/search?name={name}
```

**Ejemplo:**
```bash
curl -X GET "http://localhost:8080/api/users/search?name=juan"
```

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "age": 30
  },
  {
    "id": 4,
    "name": "Juan García",
    "email": "juan.garcia@example.com",
    "age": 28
  }
]
```

**Nota:** La búsqueda es **case-insensitive** (no importa mayúsculas/minúsculas)

---

## 🏥 Endpoints de Monitoreo

### Health Check

**Endpoint:**
```
GET /actuator/health
```

**Ejemplo:**
```bash
curl http://localhost:8080/actuator/health
```

**Respuesta:**
```json
{
  "status": "UP"
}
```

---

### Métricas en Prometheus

**Endpoint:**
```
GET /actuator/prometheus
```

**Ejemplo:**
```bash
curl http://localhost:8080/actuator/prometheus | grep 'users_'
```

**Respuesta (extracto):**
```
# HELP users_count Gauge metric for users count
# TYPE users_count gauge
users_count{} 3.0

# HELP users_created_total Users created counter
# TYPE users_created_total counter
users_created_total{} 5.0

# HELP users_retrieved_total Users retrieved counter
# TYPE users_retrieved_total counter
users_retrieved_total{} 12.0

# HELP users_updated_total Users updated counter
# TYPE users_updated_total counter
users_updated_total{} 2.0

# HELP users_deleted_total Users deleted counter
# TYPE users_deleted_total counter
users_deleted_total{} 1.0

# HELP users_not_found_total Users not found counter
# TYPE users_not_found_total counter
users_not_found_total{} 3.0
```

---

## 🛠️ Ejemplos Prácticos

### Crear múltiples usuarios

```bash
#!/bin/bash

# Crear 3 usuarios
for i in {1..3}; do
  curl -X POST http://localhost:8080/api/users \
    -H "Content-Type: application/json" \
    -d "{
      \"name\": \"Usuario $i\",
      \"email\": \"usuario$i@example.com\",
      \"age\": $((20 + i))
    }"
  echo ""
done
```

### Obtener todas las métricas relacionadas con usuarios

```bash
curl http://localhost:8080/actuator/prometheus | grep 'users'
```

### Monitoreo en tiempo real con watch

```bash
# Actualizar cada 2 segundos
watch -n 2 'curl -s http://localhost:8080/actuator/prometheus | grep "users_count"'
```

### Buscar usuarios (case-insensitive)

```bash
# Estas búsquedas devuelven el mismo resultado
curl "http://localhost:8080/api/users/search?name=juan"
curl "http://localhost:8080/api/users/search?name=JUAN"
curl "http://localhost:8080/api/users/search?name=JuAn"
```

---

## ✅ Validaciones

### Validación de Usuario

Los siguientes campos son **obligatorios** y se validan:

| Campo | Validación | Ejemplo |
|-------|-----------|---------|
| `name` | No vacío, no nulo | "Juan Pérez" ✅ |
| `email` | Debe contener "@" | "juan@example.com" ✅ |
| `age` | Entre 0 y 150 | 30 ✅ |

### Ejemplos de datos inválidos

```bash
# ❌ Nombre vacío
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "", "email": "test@example.com", "age": 30}'

# ❌ Email sin @
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Juan", "email": "testexample.com", "age": 30}'

# ❌ Edad fuera de rango
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Juan", "email": "test@example.com", "age": 200}'
```

---

## 🔐 Códigos de Respuesta HTTP

| Código | Significado | Ejemplo |
|--------|------------|---------|
| 200 | OK | GET, PUT exitosos |
| 201 | Created | POST exitoso |
| 204 | No Content | DELETE exitoso |
| 400 | Bad Request | Datos inválidos |
| 404 | Not Found | Usuario no existe |
| 500 | Server Error | Error interno |

---

## 📊 Flujo Completo Ejemplo

```bash
# 1. Obtener usuarios (debería estar vacío)
echo "1. Obtener todos los usuarios:"
curl http://localhost:8080/api/users
echo -e "\n\n"

# 2. Crear un usuario
echo "2. Crear usuario:"
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Juan", "email": "juan@example.com", "age": 30}'
echo -e "\n\n"

# 3. Obtener usuarios nuevamente
echo "3. Obtener todos los usuarios:"
curl http://localhost:8080/api/users
echo -e "\n\n"

# 4. Obtener usuario por ID
echo "4. Obtener usuario por ID 1:"
curl http://localhost:8080/api/users/1
echo -e "\n\n"

# 5. Actualizar usuario
echo "5. Actualizar usuario:"
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Juan Actualizado", "email": "juan.new@example.com", "age": 31}'
echo -e "\n\n"

# 6. Buscar por nombre
echo "6. Buscar usuarios con 'juan':"
curl "http://localhost:8080/api/users/search?name=juan"
echo -e "\n\n"

# 7. Ver métricas
echo "7. Ver métricas de usuarios:"
curl http://localhost:8080/actuator/prometheus | grep 'users_'
echo -e "\n\n"

# 8. Eliminar usuario
echo "8. Eliminar usuario:"
curl -X DELETE http://localhost:8080/api/users/1
echo -e "\n"
```

---

## 🧪 Testing con Postman

### Importar colección

1. Abre Postman
2. Crea nueva colección: "Spring Boot Monitoring"
3. Añade los siguientes requests:

**GET - Todos los usuarios**
```
GET http://localhost:8080/api/users
```

**POST - Crear usuario**
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "name": "Nuevo Usuario",
  "email": "nuevo@example.com",
  "age": 25
}
```

**GET - Usuario por ID**
```
GET http://localhost:8080/api/users/{{userId}}
```

**PUT - Actualizar usuario**
```
PUT http://localhost:8080/api/users/{{userId}}
Content-Type: application/json

{
  "name": "Usuario Actualizado",
  "email": "actualizado@example.com",
  "age": 26
}
```

**DELETE - Eliminar usuario**
```
DELETE http://localhost:8080/api/users/{{userId}}
```

---

## 🔄 Ciclo de Vida de Datos

```
┌─────────────────────────────────────────────┐
│ 1. POST /api/users                          │
│    ↓                                         │
│ 2. Validar datos (name, email, age)        │
│    ↓                                         │
│ 3. Crear usuario en memoria                 │
│    ↓                                         │
│ 4. Incremente métrica: users_created_total │
│    ↓                                         │
│ 5. Retornar 201 Created + usuario           │
│    ↓                                         │
│ 6. GET /api/users/{id} - usuario visible   │
│    └─ Incrementa: users_retrieved_total    │
│    ↓                                         │
│ 7. PUT /api/users/{id} - actualizar        │
│    └─ Incrementa: users_updated_total      │
│    ↓                                         │
│ 8. DELETE /api/users/{id} - eliminar       │
│    └─ Incrementa: users_deleted_total      │
└─────────────────────────────────────────────┘
```

---

**¡Disfruta usando la API!** 🚀

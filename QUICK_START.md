# 🚀 Quick Start - Spring Boot Monitoring App

## Inicio Rápido en 3 Pasos

### 1️⃣ Compilar

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
mvn clean install
```

### 2️⃣ Ejecutar

```bash
./run.sh
# O: mvn spring-boot:run
```

### 3️⃣ Acceder

- **Aplicación:** http://localhost:8080
- **Métricas:** http://localhost:8080/actuator/prometheus
- **Salud:** http://localhost:8080/actuator/health

---

## 📝 Testing Rápido

### Tests Unitarios
```bash
./test.sh
# O: mvn clean test
```

### Tests Selenium (requiere app ejecutándose)
```bash
./test-selenium.sh
# O: mvn test -Dtest=SeleniumUITest
```

---

## 🐳 Docker

### Con Docker
```bash
docker build -t monitoring-app:1.0 .
docker run -p 8080:8080 monitoring-app:1.0
```

### Con Docker Compose (incluye Prometheus)
```bash
docker-compose up
# Prometheus: http://localhost:9090
```

---

## 📍 URLs Principales

| Página | URL |
|--------|-----|
| Inicio | http://localhost:8080/ |
| Usuarios | http://localhost:8080/users |
| Monitoreo | http://localhost:8080/monitoring |
| API REST | http://localhost:8080/api/users |
| Prometheus | http://localhost:8080/actuator/prometheus |
| Métricas | http://localhost:8080/actuator/metrics |
| Health | http://localhost:8080/actuator/health |

---

## 🔍 Logs

```bash
# Ver logs en tiempo real
tail -f logs/app-rolling.log

# Ver últimas líneas
tail -20 logs/app-rolling.log
```

---

## 🎯 API REST Ejemplos

### Crear Usuario
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "age": 30
  }'
```

### Obtener Todos
```bash
curl http://localhost:8080/api/users
```

### Obtener por ID
```bash
curl http://localhost:8080/api/users/1
```

### Buscar por Nombre
```bash
curl "http://localhost:8080/api/users/search?name=Juan"
```

### Actualizar
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Actualizado",
    "email": "juan.new@example.com",
    "age": 31
  }'
```

### Eliminar
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

---

## 📊 Verificar Métricas

```bash
# Usuarios creados
curl http://localhost:8080/actuator/prometheus | grep users_created

# Usuarios recuperados
curl http://localhost:8080/actuator/prometheus | grep users_retrieved

# Total de usuarios
curl http://localhost:8080/actuator/prometheus | grep users_count

# Formato legible
curl http://localhost:8080/actuator/prometheus | grep 'users_' | head -10
```

---

## 🛑 Detener

```bash
# Presionar Ctrl+C en la terminal donde corre la app

# O si corre en background
kill <PID>
```

---

## ❓ Ayuda

- **README.md** - Documentación completa
- **DEVELOPMENT.md** - Guía de desarrollo
- **pom.xml** - Dependencias y configuración Maven

---

**¡Listo! Ahora puedes comenzar a desarrollar.** 🎉

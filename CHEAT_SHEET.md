# ⚡ Cheat Sheet - Comandos y Referencia Rápida

Referencia rápida de comandos más comunes para la aplicación.

## 🚀 Inicio Rápido (3 pasos)

```bash
# 1. Compilar
mvn clean install

# 2. Ejecutar
mvn spring-boot:run

# 3. Acceder
open http://localhost:8080  # macOS
# o
xdg-open http://localhost:8080  # Linux
# o
start http://localhost:8080  # Windows
```

---

## 🏗️ Compilación y Build

```bash
# Compilar todo
mvn clean install

# Compilar sin tests
mvn clean install -DskipTests

# Build JAR ejecutable
mvn clean package

# Build con tests
mvn clean package -DskipTests=false
```

---

## ▶️ Ejecutar Aplicación

```bash
# Ejecutar con Maven
mvn spring-boot:run

# Ejecutar JAR directamente
java -jar target/springboot-monitoring-app-1.0.0.jar

# Ejecutar con puerto diferente
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"

# Con script
./run.sh
```

---

## 🧪 Ejecutar Tests

```bash
# Todos los tests
mvn clean test

# Solo tests unitarios (sin Selenium)
mvn clean test -DskipTests=false

# Test específico
mvn test -Dtest=UserTest

# Tests de clase
mvn test -Dtest=UserServiceTest

# Tests de integración
mvn test -Dtest=ControllerIntegrationTest

# Tests de Selenium (app debe estar running)
# Opción 1
mvn test -Dtest=SeleniumUITest

# Opción 2 (usando script)
./test-selenium.sh

# Con cobertura
mvn clean test jacoco:report
# Reporte: target/site/jacoco/index.html

# Con detalle
mvn clean test -e -X
```

---

## 📊 Ver Logs

```bash
# Logs en tiempo real
tail -f logs/app-rolling.log

# Últimas 50 líneas
tail -50 logs/app-rolling.log

# Con búsqueda
grep "ERROR" logs/app-rolling.log
grep "User created" logs/app-rolling.log

# Contar líneas
wc -l logs/app-rolling.log

# Ver archivo completo
cat logs/app-rolling.log

# Paginador
less logs/app-rolling.log
```

---

## 🌐 Acceso a Endpoints

### Página Web
```bash
# Página principal
curl http://localhost:8080/

# Gestión de usuarios
curl http://localhost:8080/users

# Dashboard de monitoreo
curl http://localhost:8080/monitoring
```

### API REST

```bash
# Obtener todos los usuarios
curl http://localhost:8080/api/users

# Obtener usuario por ID
curl http://localhost:8080/api/users/1

# Crear usuario
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Juan","email":"juan@example.com","age":30}'

# Actualizar usuario
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Juan Updated","email":"juan.new@example.com","age":31}'

# Eliminar usuario
curl -X DELETE http://localhost:8080/api/users/1

# Buscar por nombre
curl "http://localhost:8080/api/users/search?name=juan"
```

### Monitoreo

```bash
# Health Check
curl http://localhost:8080/actuator/health

# Todas las métricas
curl http://localhost:8080/actuator/prometheus

# Solo métricas de usuarios
curl http://localhost:8080/actuator/prometheus | grep 'users_'

# Métrica específica
curl http://localhost:8080/actuator/prometheus | grep 'users_count'
```

---

## 🐳 Docker

```bash
# Construir imagen
mvn clean package
docker build -t monitoring-app:1.0 .

# Ejecutar contenedor
docker run -p 8080:8080 monitoring-app:1.0

# Con volumen de logs
docker run -p 8080:8080 -v $(pwd)/logs:/app/logs monitoring-app:1.0

# Con docker-compose
docker-compose up

# Detener docker-compose
docker-compose down
```

---

## 📝 Editor y Herramientas

```bash
# Abrir VS Code
code .

# Abrir en navegador
open http://localhost:8080  # macOS
xdg-open http://localhost:8080  # Linux

# IDE alternativas
# IntelliJ IDEA
idea .

# Eclipse
# Abrir y File > Open Projects from File System
```

---

## 🔧 Configuración Común

### Cambiar Puerto

**En application.yml:**
```yaml
server:
  port: 9090
```

**O por línea de comandos:**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

### Cambiar Nivel de Log

**En application.yml:**
```yaml
logging:
  level:
    root: DEBUG
    com.example: DEBUG
```

### Habilitar Actuator Endpoints

**En application.yml:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus,env,info
```

---

## 🔍 Debugging

```bash
# Build con verbose
mvn clean install -e -X

# Tests con verbose
mvn test -e -X

# Java debug mode
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

---

## 🧹 Limpiar y Resetear

```bash
# Limpiar target
mvn clean

# Limpiar logs
rm -rf logs/

# Limpiar todo y compilar
mvn clean install

# Limpiar caché Maven
rm -rf ~/.m2/repository

# Resetear proyecto
mvn clean && git clean -fd
```

---

## 🔍 Buscar y Reemplazar

### En Logs
```bash
# Búsqueda simple
grep "pattern" logs/app-rolling.log

# Búsqueda case-insensitive
grep -i "error" logs/app-rolling.log

# Búsqueda con regex
grep -E "^ERROR|^WARN" logs/app-rolling.log

# Contar ocurrencias
grep -c "User created" logs/app-rolling.log
```

### En Código
```bash
# Búsqueda en archivos
grep -r "searchTerm" src/

# Solo en Java
grep -r "searchTerm" src/ --include="*.java"

# Mostrar línea con número
grep -n "searchTerm" src/main/java/com/example/service/UserService.java
```

---

## 📈 Monitoreo Avanzado

```bash
# Monitor de métricas (actualiza cada 2s)
watch -n 2 'curl -s http://localhost:8080/actuator/prometheus | grep "users_"'

# Script personalizado
cat > monitor.sh << 'EOF'
#!/bin/bash
while true; do
    clear
    echo "=== Métricas en Tiempo Real ==="
    date
    curl -s http://localhost:8080/actuator/prometheus | grep 'users_'
    sleep 2
done
EOF
chmod +x monitor.sh
./monitor.sh
```

---

## 🎯 Flujos Comunes

### Flujo 1: Desarrollo Rápido
```bash
mvn clean install -DskipTests && mvn spring-boot:run
```

### Flujo 2: Testing Completo
```bash
mvn clean test && mvn clean package
```

### Flujo 3: Testing + Selenium
```bash
mvn clean test && sleep 5 && ./test-selenium.sh
```

### Flujo 4: Build y Deploy
```bash
mvn clean package && docker build -t monitoring-app:1.0 . && docker-compose up
```

---

## 📊 Referencia de Archivos

| Archivo | Propósito |
|---------|-----------|
| `pom.xml` | Dependencias y configuración Maven |
| `application.yml` | Configuración Spring Boot |
| `log4j2.xml` | Configuración de logging |
| `prometheus.yml` | Configuración de Prometheus |
| `Dockerfile` | Imagen Docker |
| `docker-compose.yml` | Orquestación Docker |
| `run.sh` | Script para ejecutar |
| `test.sh` | Script para tests |
| `test-selenium.sh` | Script para Selenium |

---

## 🚨 Problemas Comunes

```bash
# "Port 8080 already in use"
# Solución 1: Encontrar y matar proceso
lsof -i :8080
kill -9 <PID>

# Solución 2: Cambiar puerto
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"

# "Chrome driver not found"
# Solución: WebDriverManager lo descarga automáticamente
# Si falla, instala Chrome:
# macOS: brew install --cask google-chrome
# Linux: sudo apt-get install google-chrome-stable

# "Connection refused"
# Asegurate que app está running:
curl http://localhost:8080/actuator/health
```

---

## 📚 Ver Documentación

```bash
# Documentación completa
cat DOCUMENTATION_INDEX.md

# Guía de Log4j2
cat LOG4J2_GUIDE.md

# Guía de Prometheus
cat PROMETHEUS_GUIDE.md

# Guía de Selenium
cat SELENIUM_GUIDE.md

# Ejemplos de API
cat API_EXAMPLES.md

# Troubleshooting
cat TROUBLESHOOTING.md

# Comandos
cat COMMANDS.md
```

---

## ⌚ Atajos Útiles

```bash
# Crear alias
alias start-app="mvn spring-boot:run"
alias test-app="mvn clean test"
alias run-selenium="./test-selenium.sh"

# Guardar en ~/.zshrc o ~/.bashrc
source ~/.zshrc

# Luego usar
start-app
test-app
run-selenium
```

---

## 🔗 Enlaces Rápidos

- 🏠 Aplicación: http://localhost:8080
- 👥 Usuarios: http://localhost:8080/users
- 📊 Monitoreo: http://localhost:8080/monitoring
- 💊 Salud: http://localhost:8080/actuator/health
- 📈 Métricas: http://localhost:8080/actuator/prometheus
- 📦 Prometheus: http://localhost:9090 (si Docker)
- 🎨 Grafana: http://localhost:3000 (si Docker)

---

**¡Usa este cheat sheet como referencia rápida!** 🚀

Para más detalles, ver: [DOCUMENTATION_INDEX.md](./DOCUMENTATION_INDEX.md)

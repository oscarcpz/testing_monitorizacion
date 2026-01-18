#!/bin/bash

# Guía de Comandos Útiles - Spring Boot Monitoring App

cat << 'EOF'

╔═══════════════════════════════════════════════════════════════════════╗
║   🚀 SPRING BOOT MONITORING APP - COMANDOS ÚTILES                    ║
╚═══════════════════════════════════════════════════════════════════════╝

═══════════════════════════════════════════════════════════════════════
📌 CONFIGURACIÓN INICIAL
═══════════════════════════════════════════════════════════════════════

1. Configurar Java:
   export JAVA_HOME=$(/usr/libexec/java_home -v 17)

2. Compilar el proyecto:
   mvn clean install

3. Crear directorio de logs:
   mkdir -p logs

═══════════════════════════════════════════════════════════════════════
▶️  EJECUTAR LA APLICACIÓN
═══════════════════════════════════════════════════════════════════════

Opción 1 - Maven (desde terminal):
   mvn spring-boot:run

Opción 2 - Script bash:
   chmod +x run.sh
   ./run.sh

Opción 3 - JAR compilado:
   java -jar target/springboot-monitoring-app-1.0.0.jar

Opción 4 - Docker:
   docker build -t monitoring-app:1.0 .
   docker run -p 8080:8080 monitoring-app:1.0

Opción 5 - Docker Compose (con Prometheus):
   docker-compose up

═══════════════════════════════════════════════════════════════════════
🧪 EJECUTAR TESTS
═══════════════════════════════════════════════════════════════════════

Todos los tests:
   mvn clean test

Tests unitarios del modelo:
   mvn test -Dtest=UserTest

Tests del servicio:
   mvn test -Dtest=UserServiceTest

Tests de integración:
   mvn test -Dtest=ControllerIntegrationTest

Tests de Selenium (requiere app ejecutándose):
   mvn test -Dtest=SeleniumUITest

Con cobertura de código:
   mvn clean test jacoco:report
   # Ver reporte en: target/site/jacoco/index.html

═══════════════════════════════════════════════════════════════════════
📊 VER LOGS EN TIEMPO REAL
═══════════════════════════════════════════════════════════════════════

Seguimiento en vivo:
   tail -f logs/app-rolling.log

Últimas 20 líneas:
   tail -20 logs/app.log

Buscar en logs:
   grep "ERROR" logs/app-rolling.log
   grep -i "usuario" logs/app-rolling.log

═══════════════════════════════════════════════════════════════════════
🌐 API REST - EJEMPLOS CON CURL
═══════════════════════════════════════════════════════════════════════

Crear usuario:
   curl -X POST http://localhost:8080/api/users \
     -H "Content-Type: application/json" \
     -d '{"name":"Juan","email":"juan@example.com","age":30}'

Obtener todos:
   curl http://localhost:8080/api/users

Obtener por ID (cambiar 1 por ID real):
   curl http://localhost:8080/api/users/1

Buscar por nombre:
   curl "http://localhost:8080/api/users/search?name=Juan"

Actualizar usuario (cambiar 1 por ID real):
   curl -X PUT http://localhost:8080/api/users/1 \
     -H "Content-Type: application/json" \
     -d '{"name":"Juan Updated","email":"juan.new@example.com","age":31}'

Eliminar usuario (cambiar 1 por ID real):
   curl -X DELETE http://localhost:8080/api/users/1

═══════════════════════════════════════════════════════════════════════
📈 VERIFICAR MÉTRICAS Y SALUD
═══════════════════════════════════════════════════════════════════════

Ver todas las métricas:
   curl http://localhost:8080/actuator/prometheus

Filtrar métricas de usuarios:
   curl http://localhost:8080/actuator/prometheus | grep 'users_'

Ver estado de salud (JSON formateado):
   curl http://localhost:8080/actuator/health | jq

Ver métricas disponibles:
   curl http://localhost:8080/actuator/metrics | jq

═══════════════════════════════════════════════════════════════════════
🔗 URLS PRINCIPALES
═══════════════════════════════════════════════════════════════════════

Página Principal:         http://localhost:8080/
Gestión Usuarios:         http://localhost:8080/users
Dashboard Monitoreo:      http://localhost:8080/monitoring
API REST:                 http://localhost:8080/api/users
Métricas Prometheus:      http://localhost:8080/actuator/prometheus
Métricas Disponibles:     http://localhost:8080/actuator/metrics
Estado Salud:             http://localhost:8080/actuator/health
Prometheus Web:           http://localhost:9090 (si usas docker-compose)

═══════════════════════════════════════════════════════════════════════
🛠️  COMANDOS DE DESARROLLO
═══════════════════════════════════════════════════════════════════════

Compilar sin tests:
   mvn clean install -DskipTests

Compilar con verbosidad:
   mvn clean install -X

Ejecutar app con puerto diferente:
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"

Ejecutar app con DEBUG logging:
   mvn spring-boot:run -Dspring-boot.run.arguments="--logging.level.com.example=DEBUG"

Generar JAR:
   mvn clean package

Limpiar caché:
   mvn clean

═══════════════════════════════════════════════════════════════════════
🐛 DEBUGGING Y TROUBLESHOOTING
═══════════════════════════════════════════════════════════════════════

Ver qué procesos usan puerto 8080:
   lsof -i :8080

Matar proceso en puerto 8080 (macOS/Linux):
   kill -9 $(lsof -t -i :8080)

Verificar Java instalado:
   java -version

Listar JDKs disponibles (macOS):
   /usr/libexec/java_home -V

Verificar Maven:
   mvn -version

Ver estructura del proyecto:
   find . -type f -name "*.java" -o -name "*.xml" | head -20

═══════════════════════════════════════════════════════════════════════
📦 DOCKER ÚTILES
═══════════════════════════════════════════════════════════════════════

Construir imagen sin cache:
   docker build --no-cache -t monitoring-app:1.0 .

Ver logs del contenedor:
   docker logs <container-id>

Seguir logs del contenedor:
   docker logs -f <container-id>

Ver contenedores en ejecución:
   docker ps

Ver todas las imágenes:
   docker images

Detener contenedor:
   docker stop <container-id>

Eliminar contenedor:
   docker rm <container-id>

Eliminar imagen:
   docker rmi monitoring-app:1.0

Usar docker-compose:
   docker-compose up          # Iniciar
   docker-compose down        # Detener
   docker-compose logs -f     # Ver logs
   docker-compose ps          # Estado

═══════════════════════════════════════════════════════════════════════
📝 OTRAS OPERACIONES ÚTILES
═══════════════════════════════════════════════════════════════════════

Abrir pom.xml en editor:
   nano pom.xml
   # o: code pom.xml (si usas VS Code)

Ver estructura en árbol:
   tree -L 3 -I 'target'

Hacer scripts ejecutables:
   chmod +x run.sh test.sh test-selenium.sh

Crear backup:
   tar -czf backup-$(date +%Y%m%d_%H%M%S).tar.gz .

═══════════════════════════════════════════════════════════════════════

💡 TIPS:
  • Siempre configura JAVA_HOME antes de usar Maven
  • Usa ./run.sh para ejecución rápida
  • Los logs se guardan en ./logs/
  • Tests Selenium requieren que la app esté ejecutándose
  • Docker Compose es ideal para desarrollo completo

📚 Documentación:
  • README.md - Documentación completa
  • DEVELOPMENT.md - Guía detallada
  • QUICK_START.md - Inicio rápido
  • PROJECT_SUMMARY.md - Resumen del proyecto

═══════════════════════════════════════════════════════════════════════

EOF

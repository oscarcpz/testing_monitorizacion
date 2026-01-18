# 🚀 INICIO RÁPIDO - 5 Minutos

Comienza con la aplicación en solo **5 minutos**.

---

## ⚡ Opción 1: Desarrollo (Recomendado)

### Paso 1️⃣ - Compilar (1 minuto)
```bash
cd /Users/o.coscollano/workspace/github/testing_monitorizacion/testing_monitorizacion
mvn clean install
```

✅ Deberías ver: `BUILD SUCCESS`

### Paso 2️⃣ - Ejecutar (1 minuto)
```bash
mvn spring-boot:run
```

✅ Deberías ver: `Started MonitoringApplication in X.XXX seconds`

### Paso 3️⃣ - Acceder (3 minutos)
**En tu navegador:**
- 🏠 Inicio: http://localhost:8080
- 👥 Usuarios: http://localhost:8080/users
- 📊 Métricas: http://localhost:8080/monitoring

**Hacer una prueba rápida:**
```bash
# En otra terminal
curl http://localhost:8080/api/users
```

**¡Listo! 🎉** La aplicación está corriendo.

---

## 🧪 Opción 2: Con Tests (6 minutos)

### Paso 1️⃣ - Ejecutar Tests
```bash
mvn clean test
```

✅ Deberías ver: `Tests run: 27, Failures: 0, Errors: 0`

### Paso 2️⃣ - Compilar JAR
```bash
mvn clean package
```

✅ Deberías ver: `BUILD SUCCESS` y `JAR created`

### Paso 3️⃣ - Ejecutar JAR
```bash
java -jar target/springboot-monitoring-app-1.0.0.jar
```

✅ La aplicación arranca desde el JAR.

---

## 🐳 Opción 3: Con Docker (10 minutos)

### Paso 1️⃣ - Compilar
```bash
mvn clean package -DskipTests
```

### Paso 2️⃣ - Construir imagen
```bash
docker build -t monitoring-app:1.0 .
```

### Paso 3️⃣ - Ejecutar con Docker Compose
```bash
docker-compose up
```

✅ Accede a:
- 🏠 Aplicación: http://localhost:8080
- 📈 Prometheus: http://localhost:9090
- 🎨 Grafana: http://localhost:3000 (admin/admin)

---

## 🎯 Próximos Pasos

### Explorar la API
```bash
# Obtener usuarios
curl http://localhost:8080/api/users

# Crear usuario
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mi Usuario",
    "email": "usuario@example.com",
    "age": 30
  }'
```

**Documentación completa:** [API_EXAMPLES.md](./API_EXAMPLES.md)

### Ver Métricas
```bash
curl http://localhost:8080/actuator/prometheus | grep users_
```

**Documentación completa:** [PROMETHEUS_GUIDE.md](./PROMETHEUS_GUIDE.md)

### Ver Logs
```bash
tail -f logs/app-rolling.log
```

**Documentación completa:** [LOG4J2_GUIDE.md](./LOG4J2_GUIDE.md)

---

## 📚 Documentación

| Documento | Tiempo | Descripción |
|-----------|--------|-------------|
| [README.md](./README.md) | 10 min | Descripción general |
| [API_EXAMPLES.md](./API_EXAMPLES.md) | 15 min | Ejemplos de API REST |
| [LOG4J2_GUIDE.md](./LOG4J2_GUIDE.md) | 20 min | Sistema de logging |
| [PROMETHEUS_GUIDE.md](./PROMETHEUS_GUIDE.md) | 20 min | Métricas y monitoreo |
| [SELENIUM_GUIDE.md](./SELENIUM_GUIDE.md) | 20 min | Tests automatizados |
| [CHEAT_SHEET.md](./CHEAT_SHEET.md) | 5 min | Comandos rápidos |
| [DOCUMENTATION_INDEX.md](./DOCUMENTATION_INDEX.md) | - | Índice completo |

---

## ✅ Verificación Rápida

### Todo funcionando?
```bash
# Verificar que app está corriendo
curl -s http://localhost:8080/actuator/health | grep -q "UP" && echo "✅ APP RUNNING" || echo "❌ APP DOWN"

# Verificar métricas
curl -s http://localhost:8080/actuator/prometheus | grep -q "users_count" && echo "✅ METRICS OK" || echo "❌ METRICS ERROR"

# Verificar tests
mvn clean test -q && echo "✅ TESTS PASSED" || echo "❌ TESTS FAILED"
```

---

## 🐛 Problemas Comunes

### Puerto 8080 en uso
```bash
# Cambiar puerto
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Chrome no encontrado (Selenium)
```bash
# Instalar Chrome
# macOS:
brew install --cask google-chrome

# Linux:
sudo apt-get install google-chrome-stable
```

### Más problemas?
Ver: [TROUBLESHOOTING.md](./TROUBLESHOOTING.md)

---

## 🎬 Próxima Lectura

Después de ejecutar la aplicación:

1. 📖 Lee [README.md](./README.md) para entender la arquitectura
2. 🔍 Explora [API_EXAMPLES.md](./API_EXAMPLES.md) para ver todos los endpoints
3. 📊 Aprende [PROMETHEUS_GUIDE.md](./PROMETHEUS_GUIDE.md) para monitoreo
4. 📝 Estudia [LOG4J2_GUIDE.md](./LOG4J2_GUIDE.md) para logging
5. 🤖 Domina [SELENIUM_GUIDE.md](./SELENIUM_GUIDE.md) para tests

---

## 💡 Tips

- 💾 Los datos se guardan en memoria (se pierden al reiniciar)
- 📁 Los logs se guardan en `logs/app-rolling.log`
- 🔄 Los cambios en código requieren recompilación (`mvn clean install`)
- 🧪 Los tests se pueden ejecutar sin iniciar la app
- 📊 Prometheus solo funciona si la app está corriendo

---

## 🎉 ¡Comenzar!

**Elige tu opción favorita arriba y ¡empieza en 5 minutos!**

---

*Última actualización: 15 de Enero de 2024*

# 🎯 Inicio Rápido - Primeros 5 Minutos

Guía visual para empezar en 5 minutos.

## 🚀 Opción 1: Usar Script Interactivo (RECOMENDADO)

```bash
# 1. Navegar al proyecto
cd /Users/o.coscollano/workspace/github/testing_monitorizacion/testing_monitorizacion

# 2. Ejecutar script de gestión
chmod +x manage.sh
./manage.sh

# 3. Seleccionar opción 1: "Iniciar todo"
# → Esperar 30 segundos

# 4. Seleccionar opción 10: "Ver URLs de acceso"
# → Abre las URLs en navegador
```

---

## 📱 URLs Después de Iniciar

```
┌─────────────────────────────────────────────────┐
│ 🌐 APLICACIÓN                                   │
├─────────────────────────────────────────────────┤
│ http://localhost:8080          (Home)           │
│ http://localhost:8080/users    (Gestión)        │
│ http://localhost:8080/monitoring (Dashboard)    │
└─────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────┐
│ 📊 MÉTRICAS                                     │
├─────────────────────────────────────────────────┤
│ http://localhost:9090          (Prometheus)     │
│ http://localhost:8080/actuator/prometheus       │
└─────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────┐
│ 🔍 ANÁLISIS DE CÓDIGO                           │
├─────────────────────────────────────────────────┤
│ http://localhost:9000          (SonarQube)      │
│ Usuario: admin                                   │
│ Contraseña: admin                               │
└─────────────────────────────────────────────────┘
```

---

## 🎬 Flujo Completo en 5 Minutos

### Minuto 1: Iniciar Servicios
```bash
cd /Users/o.coscollano/workspace/github/testing_monitorizacion/testing_monitorizacion
./manage.sh
# Selecciona: 1 (Iniciar todo)
```

### Minuto 2: Esperar y Verificar
```bash
# En otra terminal
docker-compose ps
# Todos los servicios en "UP"
```

### Minuto 3: Crear Datos
```bash
# Crear algunos usuarios
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Juan","email":"juan@example.com","age":30}'

curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"María","email":"maria@example.com","age":25}'
```

### Minuto 4: Ver Métricas
```bash
# En navegador
http://localhost:8080/monitoring
# O
http://localhost:9090/graph
```

### Minuto 5: Ejecutar Análisis
```bash
./sonar-analysis.sh full
# O en manage.sh: opción 8
```

---

## 📋 Comandos Rápidos

```bash
# 🟢 INICIAR
./manage.sh
# Opción 1: Todo
# Opción 2: App + Prometheus
# Opción 3: Solo SonarQube

# 🟡 VERIFICAR
docker-compose ps
docker-compose logs -f app

# 🔴 PARAR
./manage.sh
# Opción 4: Parar todo

# 🧪 TESTS
./manage.sh
# Opción 7: Ejecutar tests

# 🔍 ANÁLISIS
./sonar-analysis.sh full
# O: ./manage.sh → Opción 8

# 📊 VER LOGS
tail -f logs/app-rolling.log
```

---

## ✨ Casos de Uso Comunes

### Caso 1: Desarrollar Localmente
```bash
# Opción 1: Solo app
mvn spring-boot:run

# O Opción 2: App + Prometheus
./manage.sh → Opción 2
```

### Caso 2: Analizar Código
```bash
# Paso 1: Tests
mvn clean test

# Paso 2: SonarQube
./sonar-analysis.sh full

# Paso 3: Ver resultados
# http://localhost:9000
```

### Caso 3: Monitoreo en Tiempo Real
```bash
# Terminal 1: App
./manage.sh → Opción 1

# Terminal 2: Logs
tail -f logs/app-rolling.log

# Terminal 3: Métricas
watch -n 2 'curl -s http://localhost:8080/actuator/prometheus | grep users_'
```

### Caso 4: Tests Selenium
```bash
# Terminal 1: App ejecutándose
./manage.sh → Opción 2

# Terminal 2: Tests
mvn test -Dtest=SeleniumUITest
```

---

## 🔧 Troubleshooting Rápido

| Problema | Solución |
|----------|----------|
| Puerto 8080 en uso | `lsof -i :8080` luego `kill -9 PID` |
| SonarQube lento | Esperar 2-3 minutos en primer inicio |
| Tests fallan | `mvn clean test` |
| Logs no aparecen | `tail -f logs/app-rolling.log` |
| Docker no responde | `docker-compose restart` |

---

## 📚 Documentación Disponible

```
📖 Documentos Principales:
├── README.md .......................... Este archivo
├── SONARQUBE_QUICKSTART.md ........... Guía de SonarQube
├── SONARQUBE_GUIDE.md ............... Guía completa SonarQube
├── PROMETHEUS_GUIDE.md .............. Métricas Prometheus
├── LOG4J2_GUIDE.md .................. Configuración Log4j2
├── API_EXAMPLES.md .................. Ejemplos de API REST
├── TROUBLESHOOTING.md ............... Solución de errores
└── DOCUMENTATION_INDEX.md ........... Índice de documentos
```

---

## 🎓 Próximas Acciones

1. **Primero:** Ejecuta `./manage.sh` opción 1
2. **Luego:** Crea algunos usuarios en http://localhost:8080
3. **Después:** Ejecuta análisis con `./sonar-analysis.sh full`
4. **Finalmente:** Mejora el código según resultados de SonarQube

---

## 📞 Ayuda Rápida

```bash
# Ver estado general
./manage.sh → Opción 6

# Ver todas las URLs
./manage.sh → Opción 10

# Ver logs detallados
./manage.sh → Opción 5

# Limpiar todo (borrar contenedores)
./manage.sh → Opción 9
```

---

**¡Listo! Ya tienes todo configurado para empezar.** 🚀

Próximo: Abre http://localhost:8080 en tu navegador

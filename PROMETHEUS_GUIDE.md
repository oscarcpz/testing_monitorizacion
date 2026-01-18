# 📊 Guía de Prometheus - Métricas

Documentación completa del sistema de métricas de Prometheus integrado en la aplicación.

## 📋 Contenidos

- [¿Qué es Prometheus?](#qué-es-prometheus)
- [Métricas Disponibles](#métricas-disponibles)
- [Cómo Consultar Métricas](#cómo-consultar-métricas)
- [Ejemplos Prácticos](#ejemplos-prácticos)
- [Monitoreo en Tiempo Real](#monitoreo-en-tiempo-real)
- [Dashboard Grafana](#dashboard-grafana)
- [Troubleshooting](#troubleshooting)

---

## 🤔 ¿Qué es Prometheus?

**Prometheus** es un sistema de monitoreo y alertas que recopila métricas de aplicaciones.

### Características

- ✅ Recolección de métricas en tiempo real
- ✅ Almacenamiento en series temporales
- ✅ Lenguaje de consulta (PromQL)
- ✅ Interfaz web integrada
- ✅ Integración con Grafana
- ✅ Alertas basadas en umbrales

### Integración en la Aplicación

**Framework:** Micrometer + Spring Boot Actuator
**Endpoint:** `/actuator/prometheus`
**Formato:** Prometheus text format

---

## 📈 Métricas Disponibles

### 1. Users Count (Gauge)

**Métrica:** `users_count`
**Tipo:** Gauge (valor instantáneo)
**¿Qué mide?** Número actual de usuarios en memoria

**Características:**
- Se actualiza en tiempo real
- Aumenta cuando se crea usuario
- Disminuye cuando se elimina usuario

**Ejemplo:**
```
users_count{} 3.0
```

**Consulta PromQL:**
```promql
users_count
```

---

### 2. Users Created Counter

**Métrica:** `users_created_total`
**Tipo:** Counter (solo aumenta)
**¿Qué mide?** Total acumulativo de usuarios creados

**Características:**
- Siempre aumenta, nunca disminuye
- Se reinicia al reiniciar la aplicación
- Ideal para auditoría

**Ejemplo:**
```
users_created_total{} 5.0
```

**Consulta PromQL:**
```promql
users_created_total
```

---

### 3. Users Retrieved Counter

**Métrica:** `users_retrieved_total`
**Tipo:** Counter
**¿Qué mide?** Total de búsquedas exitosas de usuarios

**Características:**
- Aumenta cada vez que se obtiene un usuario
- Incluye GET por ID y búsquedas por nombre

**Ejemplo:**
```
users_retrieved_total{} 12.0
```

---

### 4. Users Updated Counter

**Métrica:** `users_updated_total`
**Tipo:** Counter
**¿Qué mide?** Total de usuarios actualizados

**Ejemplo:**
```
users_updated_total{} 2.0
```

---

### 5. Users Deleted Counter

**Métrica:** `users_deleted_total`
**Tipo:** Counter
**¿Qué mide?** Total de usuarios eliminados

**Ejemplo:**
```
users_deleted_total{} 1.0
```

---

### 6. Users Not Found Counter

**Métrica:** `users_not_found_total`
**Tipo:** Counter
**¿Qué mide?** Total de búsquedas fallidas (usuario no encontrado)

**Ejemplo:**
```
users_not_found_total{} 3.0
```

---

## 🔍 Cómo Consultar Métricas

### Endpoint Base

```
GET http://localhost:8080/actuator/prometheus
```

### Ver todas las métricas

```bash
curl http://localhost:8080/actuator/prometheus
```

### Filtrar solo métricas de usuarios

```bash
curl http://localhost:8080/actuator/prometheus | grep 'users_'
```

### Ver una métrica específica

```bash
curl http://localhost:8080/actuator/prometheus | grep 'users_count'
```

### Con pretty-print (mejor formato)

```bash
curl -s http://localhost:8080/actuator/prometheus | grep 'users_' | sort
```

---

## 💡 Ejemplos Prácticos

### Ejemplo 1: Monitorear Creación de Usuarios

```bash
# Terminal 1: Ver métricas cada 2 segundos
watch -n 2 'curl -s http://localhost:8080/actuator/prometheus | grep "users_created"'

# Terminal 2: Crear usuarios
for i in {1..5}; do
  curl -X POST http://localhost:8080/api/users \
    -H "Content-Type: application/json" \
    -d "{\"name\": \"User $i\", \"email\": \"user$i@example.com\", \"age\": 30}"
  sleep 1
done
```

**Resultado en Terminal 1:**
```
# Antes
users_created_total{} 0.0

# Después
users_created_total{} 5.0
```

### Ejemplo 2: Calcular Tasa de Acceso

```bash
# Calcular accesos por minuto (usando PromQL)
curl -s "http://localhost:8080/actuator/prometheus" | grep "users_retrieved_total"
```

**Fórmula PromQL para tasa:**
```promql
rate(users_retrieved_total[1m])  # Accesos por segundo en el último minuto
```

### Ejemplo 3: Dashboard Manual

```bash
#!/bin/bash

echo "=== Dashboard de Métricas de Usuarios ==="
echo ""

metrics=$(curl -s http://localhost:8080/actuator/prometheus | grep -E 'users_')

echo "📊 Métricas actuales:"
echo "$metrics" | grep -v "^#"

echo ""
echo "📈 Cálculos:"
users_count=$(echo "$metrics" | grep '^users_count' | awk '{print $2}')
created=$(echo "$metrics" | grep '^users_created_total' | awk '{print $2}')
retrieved=$(echo "$metrics" | grep '^users_retrieved_total' | awk '{print $2}')
updated=$(echo "$metrics" | grep '^users_updated_total' | awk '{print $2}')
deleted=$(echo "$metrics" | grep '^users_deleted_total' | awk '{print $2}')

echo "Usuarios activos: $users_count"
echo "Total creados: $created"
echo "Total recuperados: $retrieved"
echo "Total actualizados: $updated"
echo "Total eliminados: $deleted"
```

### Ejemplo 4: Comparar Métricas

```bash
# Tomar medición 1
echo "Medición 1 (t=0):"
M1=$(curl -s http://localhost:8080/actuator/prometheus | grep users_retrieved_total | awk '{print $2}')
echo "Accesos: $M1"

# Hacer operaciones
echo "Haciendo 10 accesos..."
for i in {1..10}; do
  curl -s http://localhost:8080/api/users/1 > /dev/null 2>&1 || true
done

# Tomar medición 2
sleep 1
echo "Medición 2 (t=5s):"
M2=$(curl -s http://localhost:8080/actuator/prometheus | grep users_retrieved_total | awk '{print $2}')
echo "Accesos: $M2"

# Calcular diferencia
DIFF=$(echo "$M2 - $M1" | bc)
echo "Diferencia: $DIFF accesos"
```

---

## ⏱️ Monitoreo en Tiempo Real

### Con watch (Linux/Mac)

```bash
# Actualizar cada 2 segundos
watch -n 2 'curl -s http://localhost:8080/actuator/prometheus | grep "users_"'
```

### Con script Bash

```bash
#!/bin/bash

while true; do
    clear
    echo "=== Métricas de Usuarios en Tiempo Real ==="
    date
    echo ""
    curl -s http://localhost:8080/actuator/prometheus | grep 'users_' | sort
    echo ""
    sleep 2
done
```

**Guardar como:** `monitor.sh`
**Ejecutar:** `chmod +x monitor.sh && ./monitor.sh`

### Con PowerShell (Windows)

```powershell
while($true) {
    Clear-Host
    "=== Métricas de Usuarios en Tiempo Real ==="
    Get-Date
    ""
    $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/prometheus"
    $response.Content -split "`n" | Where-Object { $_ -match "users_" } | Sort-Object
    ""
    Start-Sleep -Seconds 2
}
```

---

## 📊 Dashboard Grafana

### Instalación con Docker Compose

Añadir a `docker-compose.yml`:

```yaml
version: '3.8'

services:
  prometheus:
    image: prom/prometheus:latest
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'

  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
    depends_on:
      - prometheus
```

### Configuración de Prometheus (prometheus.yml)

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'spring-app'
    static_configs:
      - targets: ['localhost:8080']
    metrics_path: '/actuator/prometheus'
```

### Acceder a Grafana

1. Abre: http://localhost:3000
2. Usuario: admin
3. Contraseña: admin
4. Añade Prometheus como data source: http://localhost:9090

### Crear Dashboard

**Panel 1: Usuarios Actuales**
```promql
users_count
```

**Panel 2: Tasa de Creación**
```promql
rate(users_created_total[1m])
```

**Panel 3: Total de Operaciones**
```promql
users_created_total + users_retrieved_total + users_updated_total
```

---

## 🔧 Registrar Métricas Personalizadas

### En UserService.java

```java
@Service
public class UserService {
    private final MeterRegistry meterRegistry;
    private final Map<Integer, User> users;
    private Integer nextId = 1;
    
    public UserService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.users = new ConcurrentHashMap<>();
        initializeMetrics();
    }
    
    private void initializeMetrics() {
        // Gauge: usuarios actuales
        meterRegistry.gauge("users.count", users, Map::size);
        
        // Counters
        meterRegistry.counter("users.created", "action", "create");
        meterRegistry.counter("users.retrieved", "action", "retrieve");
    }
}
```

### Usar Counters

```java
public User createUser(User user) {
    // ... validación y lógica ...
    
    // Incrementar counter
    meterRegistry.counter("users.created", "status", "success").increment();
    
    return savedUser;
}
```

### Usar Timers

```java
public User getUserById(Integer id) {
    Timer timer = Timer.start(meterRegistry);
    
    try {
        User user = users.get(id);
        timer.stop(Timer.resource("users.retrieval", "status", "found"));
        return user;
    } catch (Exception e) {
        timer.stop(Timer.resource("users.retrieval", "status", "error"));
        throw e;
    }
}
```

---

## 📡 Consultas PromQL Útiles

### Métricas Básicas

```promql
# Todos los usuarios actuales
users_count

# Total creados
users_created_total

# Últimas 5 mediciones
users_count offset 5m
```

### Tasas de Cambio

```promql
# Usuarios creados por segundo en el último minuto
rate(users_created_total[1m])

# Usuarios creados por hora en las últimas 24 horas
rate(users_created_total[24h])
```

### Comparaciones

```promql
# Relación creados/eliminados
users_created_total / users_deleted_total

# Usuarios en relación a máximo histórico
users_count / max(users_count)
```

### Predicciones

```promql
# Predicción de creación de usuarios en próxima hora
predict_linear(users_created_total[1h], 3600)
```

---

## 🐛 Troubleshooting

### Problema: Endpoint retorna 404

**Causa:** Actuator no está habilitado

**Solución:** En `application.yml`
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
```

### Problema: No hay métricas de usuarios

**Causa:** La aplicación no ha registrado métricas

**Solución:** Crear al menos un usuario
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Test", "email": "test@example.com", "age": 30}'
```

### Problema: Métricas en 0

**Causa:** Normal si no hay operaciones

**Solución:** Hacer operaciones manualmente
```bash
curl http://localhost:8080/api/users
```

### Problema: Prometheus no se conecta

**Causa:** URL incorrecta o aplicación no running

**Solución:**
1. Verificar que app está ejecutándose: http://localhost:8080/actuator/health
2. Verificar metrics: http://localhost:8080/actuator/prometheus
3. En prometheus.yml: `targets: ['localhost:8080']`

---

## 📈 Ejemplos de Alertas

### Alerta: Muchos No Encontrados

```yaml
alert: TooManyNotFoundErrors
expr: users_not_found_total > 10
for: 5m
annotations:
  summary: "Too many user not found errors"
```

### Alerta: Correlación Crear/Eliminar

```yaml
alert: UnbalancedUserOperations
expr: users_created_total - users_deleted_total > 100
for: 10m
annotations:
  summary: "Too many users created vs deleted"
```

---

## 🎯 Checklist de Monitoreo

- [ ] Aplicación ejecutándose en puerto 8080
- [ ] Endpoint `/actuator/prometheus` accesible
- [ ] Almenos una operación CRUD ejecutada
- [ ] Métricas visibles en endpoint
- [ ] Dashboard Grafana conectado (opcional)
- [ ] Alerts configuradas (opcional)

---

**¡Ahora eres un experto en Prometheus!** 🚀

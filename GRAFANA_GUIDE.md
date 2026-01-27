# Grafana - Dashboards de Monitoreo

Este proyecto incluye **Grafana** integrado con Prometheus para visualizar las métricas de la aplicación Spring Boot en tiempo real.

## Acceso a Grafana

- **URL:** http://localhost:3000
- **Usuario:** admin
- **Contraseña:** admin

## Características

### Datasource Prometheus
La fuente de datos Prometheus está **preconfigurada automáticamente** en:
- URL: `http://prometheus:9090`
- Acceso: Proxy

### Dashboard Pre-configurado
Se incluye un dashboard de ejemplo **"Spring Boot Monitoring Dashboard"** que muestra:

1. **HTTP Requests Rate (5m)**
   - Tasa de solicitudes HTTP por método y estado en los últimos 5 minutos
   - Gráfico de líneas

2. **Total HTTP Requests**
   - Contador total de solicitudes HTTP
   - Medidor (gauge)

3. **HTTP Request Duration (p95, p99)**
   - Percentiles 95 y 99 de latencia en solicitudes HTTP
   - Gráfico con estadísticas de media y máximo

4. **JVM Memory Usage**
   - Uso de memoria JVM en bytes
   - Gráfico de líneas con estadísticas

## Métricas Disponibles desde la Aplicación

La aplicación Spring Boot expone las siguientes métricas en `/actuator/prometheus`:

- `http_requests_total` - Contador de solicitudes HTTP
- `http_request_duration_seconds` - Histograma de duración de solicitudes (con percentiles)
- `jvm_memory_used_bytes` - Uso de memoria JVM por área
- `jvm_gc_*` - Métricas de recolección de basura
- Muchas otras métricas de Spring Boot Actuator

## Primeros Pasos

### 1. Iniciar la Infraestructura
```bash
sh manage.sh
# Selecciona opción 1: Iniciar todo
```

### 2. Acceder a Grafana
Abre en el navegador: http://localhost:3000

### 3. Ver el Dashboard
Una vez dentro de Grafana:
- El dashboard **"Spring Boot Monitoring Dashboard"** se carga automáticamente
- Si no aparece, ve a **Dashboards → Browse** y busca "Spring Boot Monitoring"

### 4. Generar Tráfico
Para ver datos en los gráficos, genera tráfico en la aplicación:
```bash
# En otra terminal
curl http://localhost:8080/users
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@example.com"}'
```

## Estructura de Provisioning

```
grafana/
├── provisioning/
│   ├── datasources/
│   │   └── prometheus.yml          # Configuración de Prometheus como datasource
│   └── dashboards/
│       ├── dashboard-provider.yml  # Configuración de carga de dashboards
│       └── springboot-dashboard.json  # Dashboard de ejemplo
```

## Personalización

### Crear un Nuevo Dashboard
1. Ve a http://localhost:3000/dashboard/new
2. Agrega paneles con tus queries de Prometheus
3. Guarda el dashboard
4. Exporta el JSON a `grafana/provisioning/dashboards/`

### Modificar el Dashboard Existente
Edita `grafana/provisioning/dashboards/springboot-dashboard.json` y reinicia Grafana:
```bash
docker compose restart grafana
```

### Cambiar Credenciales
Edita las variables de entorno en `docker-compose.yml`:
```yaml
environment:
  GF_SECURITY_ADMIN_USER: tu_usuario
  GF_SECURITY_ADMIN_PASSWORD: tu_contraseña
```

## Plugins Disponibles
El contenedor incluye `grafana-piechart-panel` preinstalado para gráficos de pie.

## Documentación Oficial
- [Grafana Docs](https://grafana.com/docs/grafana/latest/)
- [Grafana Provisioning](https://grafana.com/docs/grafana/latest/provisioning/provisioning/)
- [Prometheus Data Source](https://grafana.com/docs/grafana/latest/datasources/prometheus/)

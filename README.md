# Aplicación Spring Boot - Monitoreo con Log4j2 y Prometheus

Una aplicación de ejemplo que demuestra la integración de **Log4j2**, **Prometheus**, **Tests Unitarios** y **Selenium** en un proyecto Spring Boot.

## 🎯 Características

- ✅ **Spring Boot 3.2** - Framework web moderno
- ✅ **Log4j2** - Sistema de logging avanzado con configuración XML
- ✅ **Prometheus** - Métricas de aplicación en tiempo real
- ✅ **Spring Actuator** - Endpoints de monitoreo y salud
- ✅ **Thymeleaf** - Motor de plantillas HTML
- ✅ **Tests Unitarios** - JUnit 5 con MockMvc
- ✅ **Selenium** - Pruebas automatizadas de interfaz web
- ✅ **WebDriverManager** - Gestión automática de drivers

## 🚀 Requisitos

- **Java 17+**
- **Maven 3.8+**
- **Chrome/Chromium** (para Selenium)

## 📦 Instalación

### 1. Clonar el repositorio

```bash
cd /Users/o.coscollano/workspace/github/testing_monitorizacion/testing_monitorizacion
```

### 2. Compilar el proyecto

```bash
mvn clean install
```

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 🏗️ Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── MonitoringApplication.java      # Clase principal
│   │   ├── model/
│   │   │   └── User.java                   # Modelo de usuario
│   │   ├── service/
│   │   │   └── UserService.java            # Lógica de negocio
│   │   └── controller/
│   │       └── PageController.java         # Controladores web y API REST
│   └── resources/
│       ├── application.yml                 # Configuración de Spring Boot
│       ├── log4j2.xml                      # Configuración de Log4j2
│       └── templates/
│           ├── index.html                  # Página principal
│           ├── users.html                  # Gestión de usuarios
│           └── monitoring.html             # Dashboard de monitoreo
└── test/
    └── java/com/example/
        ├── model/
        │   └── UserTest.java               # Tests del modelo
        ├── service/
        │   └── UserServiceTest.java        # Tests del servicio
        ├── controller/
        │   └── ControllerIntegrationTest.java # Tests de integración
        └── selenium/
            └── SeleniumUITest.java         # Tests de interfaz con Selenium
```

## 📊 Log4j2 - Configuración

La configuración se encuentra en `src/main/resources/log4j2.xml`:

### Appenders configurados:

1. **Console** - Salida en consola con timestamp y nivel
2. **File** - Archivo de log principal `logs/app.log`
3. **RollingFile** - Archivo rotativo con compresión gzip
   - Rotación diaria
   - Máximo 10MB por archivo
   - Máximo 10 archivos guardados

### Niveles de Log:

- `com.example` - **INFO**
- `org.springframework` - **INFO**
- `org.springframework.boot.actuate` - **DEBUG**

### Ejemplos de uso en código:

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {
    
    public void createUser(User user) {
        log.info("Usuario creado: ID={}, Email={}", user.getId(), user.getEmail());
        log.warn("Usuario inválido: {}", user.getEmail());
        log.error("Error al crear usuario", exception);
        log.debug("Detalles de depuración");
    }
}
```

## 📈 Prometheus - Métricas

### Endpoints disponibles:

- `http://localhost:8080/actuator/prometheus` - Métricas en formato Prometheus
- `http://localhost:8080/actuator/metrics` - Listado de métricas disponibles
- `http://localhost:8080/actuator/health` - Estado de salud

### Métricas personalizadas implementadas:

- `users.count` - Gauge del total de usuarios
- `users.created` - Contador de usuarios creados
- `users.retrieved` - Contador de usuarios recuperados
- `users.updated` - Contador de usuarios actualizados
- `users.deleted` - Contador de usuarios eliminados
- `users.not_found` - Contador de búsquedas sin resultado

### Configuración en `application.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

## 🧪 Tests

### 1. Tests Unitarios del Modelo

```bash
mvn test -Dtest=UserTest
```

**Pruebas:**
- ✅ Usuario válido con datos correctos
- ✅ Usuario inválido por nombre vacío
- ✅ Usuario inválido por email sin @
- ✅ Usuario inválido por edad fuera de rango

### 2. Tests del Servicio

```bash
mvn test -Dtest=UserServiceTest
```

**Pruebas:**
- ✅ Crear usuario exitosamente
- ✅ Obtener usuario por ID
- ✅ Obtener todos los usuarios
- ✅ Actualizar usuario
- ✅ Eliminar usuario
- ✅ Búsqueda por nombre (case-insensitive)

### 3. Tests de Integración (MockMvc)

```bash
mvn test -Dtest=ControllerIntegrationTest
```

**Pruebas:**
- ✅ GET / retorna página principal
- ✅ POST /api/users crea nuevo usuario
- ✅ GET /api/users/{id} obtiene usuario
- ✅ DELETE /api/users/{id} elimina usuario
- ✅ Prometheus endpoint accesible

### 4. Tests Selenium - Interfaz Web

```bash
mvn test -Dtest=SeleniumUITest
```

**Nota:** La aplicación debe estar ejecutándose en `http://localhost:8080`

**Pruebas:**
- ✅ Navegar a página principal
- ✅ Validar navegación desde página principal
- ✅ Navegar a página de usuarios
- ✅ Crear usuario desde formulario
- ✅ Validar tabla de usuarios cargada
- ✅ Navegar a dashboard de monitoreo
- ✅ Validar tarjetas de métricas en dashboard
- ✅ Validar responsividad (desktop, tablet, mobile)
- ✅ Validar estilos CSS aplicados

## 🚦 Ejecutar todos los tests

```bash
mvn clean test
```

### Con cobertura de código:

```bash
mvn clean test jacoco:report
```

Los reportes se generarán en: `target/site/jacoco/`

## 💻 Interfaz Web

### Página Principal (`/`)
- Presentación de la aplicación
- Descripción de características
- Links rápidos a otras secciones

### Gestión de Usuarios (`/users`)
- Formulario para crear usuarios
- Tabla de usuarios registrados
- Botón para eliminar usuarios
- Validación en cliente y servidor

### Dashboard de Monitoreo (`/monitoring`)
- Tarjetas de métricas en tiempo real
- Estado del sistema
- Botón para actualizar métricas
- Links a endpoints de Prometheus

## 🔍 Monitoreo

### Ver logs

```bash
# Logs en consola (durante ejecución)
tail -f logs/app-rolling.log

# Última línea de logs
tail -1 logs/app.log
```

### Consultar métricas Prometheus

```bash
curl http://localhost:8080/actuator/prometheus | grep 'users_'
```

### Ejemplos de respuestas:

```
# Usuarios creados
users_created_total{} 5

# Usuarios recuperados
users_retrieved_total{} 12

# Usuarios en memoria
users_count{} 3
```

## 🐳 Docker (Opcional)

### Construir imagen

```bash
mvn clean package
docker build -t monitoring-app:1.0 .
```

### Ejecutar contenedor

```bash
docker run -p 8080:8080 -v $(pwd)/logs:/app/logs monitoring-app:1.0
```

## 🔧 Configuración Avanzada

### Cambiar puerto

En `application.yml`:
```yaml
server:
  port: 9090
```

### Cambiar nivel de log

En `log4j2.xml`:
```xml
<Logger name="com.example" level="DEBUG"/>
```

### Incluir más métricas de Prometheus

En `application.yml`:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus,env,info
```

## 📚 Recursos

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Log4j2 Documentation](https://logging.apache.org/log4j/2.x/)
- [Prometheus Metrics](https://micrometer.io/)
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Thymeleaf Template Engine](https://www.thymeleaf.org/)

## 🐛 Troubleshooting

### Error: "ChromeDriver not found"

```bash
# WebDriverManager descargará automáticamente el driver
# Si falla, instala Chrome/Chromium manualmente
```

### Error: "Address already in use"

```bash
# Cambiar puerto en application.yml o ejecutar en otro puerto
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Tests Selenium no funcionan

Asegúrate de:
1. Ejecutar la aplicación: `mvn spring-boot:run`
2. Chrome/Chromium está instalado
3. Puerto 8080 está accesible

## 📝 Licencia

MIT License - Libre para uso personal y educativo

## 👤 Autor

Aplicación de ejemplo para demostración de tecnologías de monitoreo en Spring Boot

---

**¡Disfruta aprendiendo!** 🎉

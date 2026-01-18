# Guía de Desarrollo - Spring Boot Monitoring App

## 📋 Tabla de Contenidos

1. [Configuración del Entorno](#configuración-del-entorno)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Ejecutar la Aplicación](#ejecutar-la-aplicación)
4. [Desarrollo](#desarrollo)
5. [Testing](#testing)
6. [Debugging](#debugging)

## 🔧 Configuración del Entorno

### Requisitos Previos

```bash
# Verificar Java 17
java -version

# Verificar Maven
mvn -version

# Verificar Git
git --version
```

### Instalación Inicial

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd testing_monitorizacion

# 2. Compilar el proyecto
mvn clean install

# 3. Crear directorio de logs
mkdir -p logs
```

## 📁 Estructura del Proyecto

```
proyecto/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── model/          # Clases de dominio
│   │   │   ├── service/        # Lógica de negocio
│   │   │   └── controller/     # Controladores web y API
│   │   └── resources/
│   │       ├── templates/      # Páginas HTML (Thymeleaf)
│   │       ├── application.yml # Configuración Spring Boot
│   │       └── log4j2.xml      # Configuración de logging
│   └── test/
│       └── java/com/example/
│           ├── model/          # Tests unitarios
│           ├── service/        # Tests de servicio
│           ├── controller/     # Tests de integración
│           └── selenium/       # Tests E2E
├── pom.xml                     # Dependencias Maven
├── Dockerfile                  # Configuración Docker
├── docker-compose.yml          # Stack de servicios
├── prometheus.yml              # Configuración Prometheus
├── run.sh                       # Script de ejecución
├── test.sh                      # Script de tests
└── test-selenium.sh            # Script de tests Selenium
```

## 🚀 Ejecutar la Aplicación

### Opción 1: Maven

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
mvn spring-boot:run
```

### Opción 2: Script Bash

```bash
chmod +x run.sh
./run.sh
```

### Opción 3: JAR Compilado

```bash
java -jar target/springboot-monitoring-app-1.0.0.jar
```

### Opción 4: Docker

```bash
docker build -t monitoring-app:1.0 .
docker run -p 8080:8080 monitoring-app:1.0
```

### Opción 5: Docker Compose (con Prometheus)

```bash
docker-compose up
```

## 💻 Desarrollo

### Añadir Nuevo Endpoint

**1. Crear Controlador:**

```java
@RestController
@RequestMapping("/api/resources")
@Slf4j
public class ResourceController {
    private final ResourceService resourceService;
    
    @GetMapping
    public List<Resource> getAll() {
        log.info("GET /api/resources");
        return resourceService.findAll();
    }
}
```

**2. Crear Servicio:**

```java
@Service
@Slf4j
public class ResourceService {
    private final MeterRegistry meterRegistry;
    
    public List<Resource> findAll() {
        log.debug("Fetching all resources");
        meterRegistry.counter("resources.fetched").increment();
        return resources;
    }
}
```

### Añadir Nueva Métrica de Prometheus

```java
@Service
@Slf4j
public class CustomService {
    private final MeterRegistry meterRegistry;
    
    public void doSomething() {
        // Contador
        meterRegistry.counter("custom.counter", "type", "important").increment();
        
        // Temporizador
        long startTime = System.currentTimeMillis();
        // ... hacer algo ...
        long duration = System.currentTimeMillis() - startTime;
        meterRegistry.timer("custom.duration").record(Duration.ofMillis(duration));
        
        // Gauge (valor actual)
        meterRegistry.gauge("custom.value", 42);
    }
}
```

### Modificar Configuración de Logging

**En `log4j2.xml`:**

```xml
<!-- Aumentar verbosidad para un paquete específico -->
<Logger name="com.example.service" level="DEBUG" additivity="false">
    <AppenderRef ref="Console"/>
    <AppenderRef ref="RollingFile"/>
</Logger>
```

## 🧪 Testing

### Ejecutar Todos los Tests

```bash
mvn clean test
```

### Ejecutar Tests Específicos

```bash
# Tests del modelo
mvn test -Dtest=UserTest

# Tests del servicio
mvn test -Dtest=UserServiceTest

# Tests de integración
mvn test -Dtest=ControllerIntegrationTest

# Tests con patrón
mvn test -Dtest=*ServiceTest
```

### Ejecutar Tests con Cobertura

```bash
mvn clean test jacoco:report
# Abrir: target/site/jacoco/index.html
```

### Tests de Interfaz (Selenium)

**Requisitos:**
- Aplicación en ejecución: `http://localhost:8080`
- Chrome/Chromium instalado

```bash
chmod +x test-selenium.sh
./test-selenium.sh
```

### Escribir Nuevo Test Unitario

```java
@DisplayName("Tests de Funcionalidad")
class MyFeatureTest {
    
    @BeforeEach
    void setUp() {
        // Inicialización
    }
    
    @Test
    @DisplayName("Descripción del test")
    void testSomething() {
        // Arrange (preparar)
        String input = "test";
        
        // Act (actuar)
        String result = myService.process(input);
        
        // Assert (verificar)
        assertEquals("expected", result);
    }
}
```

### Escribir Test con Selenium

```java
@Test
@DisplayName("Interacción con elemento")
void testElementInteraction() {
    driver.navigate().to(BASE_URL + "/users");
    
    // Esperar elemento
    WebElement button = wait.until(
        ExpectedConditions.elementToBeClickable(By.id("submitBtn"))
    );
    
    // Interactuar
    button.click();
    
    // Verificar resultado
    WebElement result = driver.findElement(By.id("result"));
    assertEquals("Success", result.getText());
}
```

## 🐛 Debugging

### Logs en Consola

```bash
# Mientras la app está ejecutándose
tail -f logs/app-rolling.log
```

### Aumentar Verbosidad de Logs

**Temporalmente en consola:**

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--logging.level.com.example=DEBUG"
```

**Permanentemente en `application.yml`:**

```yaml
logging:
  level:
    com.example: DEBUG
    org.springframework.web: DEBUG
```

### Depuración en VS Code

**Crear `.vscode/launch.json`:**

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Spring Boot App",
            "request": "launch",
            "cwd": "${workspaceFolder}",
            "mainClass": "com.example.MonitoringApplication",
            "projectName": "springboot-monitoring-app",
            "preLaunchTask": "mvn: clean",
            "console": "integratedTerminal"
        }
    ]
}
```

### Ver Métricas en Tiempo Real

```bash
# Abrir en navegador
curl http://localhost:8080/actuator/prometheus | grep 'users_'

# O en navegador:
# http://localhost:8080/actuator/prometheus
```

### Health Check

```bash
curl http://localhost:8080/actuator/health | jq
```

## 📊 Monitoreo

### Prometheus Local

Si usas Docker Compose:
```bash
# Prometheus disponible en http://localhost:9090
docker-compose up
```

### Queries PromQL Útiles

```promql
# Usuarios creados totales
users_created_total

# Tasa de creación por minuto
rate(users_created_total[1m])

# Total de usuarios en memoria
users_count

# Errores de búsqueda
users_not_found_total
```

## 🔍 Troubleshooting

### Error: "Port already in use"

```bash
# Cambiar puerto en application.yml
server:
  port: 9090

# O pasar como argumento
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

### Error: "JAVA_HOME not set"

```bash
# macOS
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Linux
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk

# Windows (PowerShell)
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
```

### Tests Selenium fallan

```bash
# Verificar que Chrome está instalado
which google-chrome  # o which chromium

# WebDriverManager descarga automáticamente el driver
# Si falla, reinstalar Chrome o Chromium
```

### Logs no se generan

```bash
# Verificar permisos en carpeta logs
chmod 755 logs

# Verificar que la carpeta existe
mkdir -p logs

# Verificar configuración en log4j2.xml
ls -la logs/
```

## 📚 Recursos Útiles

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Log4j2 Configuration](https://logging.apache.org/log4j/2.x/manual/configuration.html)
- [Micrometer Prometheus](https://micrometer.io/docs/registry/prometheus)
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

---

**¡Listo para desarrollar!** 🚀

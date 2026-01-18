# 📝 Guía de Log4j2

Documentación completa del sistema de logging configurado en la aplicación.

## 📋 Contenidos

- [Configuración General](#configuración-general)
- [Niveles de Log](#niveles-de-log)
- [Appenders (Destinos)](#appenders-destinos)
- [Cómo Usar en el Código](#cómo-usar-en-el-código)
- [Ejemplos Prácticos](#ejemplos-prácticos)
- [Troubleshooting](#troubleshooting)

---

## ⚙️ Configuración General

La configuración de Log4j2 se encuentra en: `src/main/resources/log4j2.xml`

### Estructura de la Configuración

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
    <Appenders>
        <!-- Diferentes destinos de logs -->
    </Appenders>
    
    <Loggers>
        <!-- Configuración de loggers por paquete/clase -->
    </Loggers>
</Configuration>
```

---

## 📊 Niveles de Log

Los niveles de severidad (de menor a mayor):

### 1. **TRACE** - Más detallado
```
Información de depuración muy detallada
Ejemplo: Entrada a métodos, valores de variables locales
```

### 2. **DEBUG**
```
Información útil para diagnosticar problemas
Ejemplo: Valores de variables, control de flujo
```

### 3. **INFO** - Por defecto
```
Información general del flujo de la aplicación
Ejemplo: Aplicación iniciada, usuario creado
```

### 4. **WARN**
```
Situaciones que podrían ser problemas
Ejemplo: Usuario no encontrado, valor fuera de rango
```

### 5. **ERROR**
```
Errores que afectan la funcionalidad
Ejemplo: Fallo en base de datos, validación fallida
```

### 6. **FATAL** - Más grave
```
Errores muy graves que impiden continuar
Ejemplo: Error al inicializar aplicación
```

---

## 📁 Appenders (Destinos)

### 1. Console Appender
**¿Dónde?** - Terminal/Consola
**Formato:** `[TIMESTAMP] [NIVEL] [Logger] - Mensaje`

**Ejemplo de salida:**
```
2024-01-15 10:30:45,123 INFO  com.example.service.UserService - User created successfully: Juan Pérez
```

### 2. File Appender
**¿Dónde?** - `logs/app.log`
**Formato:** El mismo que Console

**Ejemplo de archivo:**
```
logs/
└── app.log                 # Todos los logs combinados
```

### 3. Rolling File Appender
**¿Dónde?** - `logs/app-rolling.log`
**Rotación:** Cada 10 MB
**Máximo:** 10 archivos
**Compresión:** Los archivos viejos se comprimen en GZIP

**Ejemplo de archivos:**
```
logs/
├── app-rolling.log                    # Archivo actual
├── app-rolling.log-2024-01-15-1.gz   # Archivos rotados comprimidos
├── app-rolling.log-2024-01-15-2.gz
└── app-rolling.log-2024-01-15-3.gz
```

---

## 💻 Cómo Usar en el Código

### Paso 1: Importar Logger

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
```

### Paso 2: Crear instancia de Logger

```java
@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    
    // ... resto del código
}
```

### Paso 3: Usar el logger

```java
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    
    public User createUser(User user) {
        logger.info("Creating user: {}", user.getName());
        
        if (!user.isValid()) {
            logger.warn("Invalid user data: {}", user.getEmail());
            throw new IllegalArgumentException("Invalid user data");
        }
        
        User savedUser = users.put(nextId++, user);
        logger.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }
}
```

---

## 🔍 Ejemplos Prácticos

### Ejemplo 1: Crear Usuario con Logging

```java
@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    
    public User createUser(User user) {
        logger.info("Creating new user: name='{}', email='{}'", 
                   user.getName(), user.getEmail());
        
        if (!user.isValid()) {
            logger.error("User validation failed: {}", user);
            throw new IllegalArgumentException("Invalid user");
        }
        
        User savedUser = users.put(nextId, user);
        nextId++;
        
        logger.info("User created successfully: ID={}, Name={}", 
                   savedUser.getId(), savedUser.getName());
        logger.debug("User details: {}", savedUser);
        
        return savedUser;
    }
}
```

**Salida esperada:**
```
2024-01-15 10:30:45,123 INFO  UserService - Creating new user: name='Juan Pérez', email='juan@example.com'
2024-01-15 10:30:45,124 INFO  UserService - User created successfully: ID=1, Name=Juan Pérez
2024-01-15 10:30:45,125 DEBUG UserService - User details: User{id=1, name='Juan Pérez', email='juan@example.com', age=30}
```

### Ejemplo 2: Búsqueda de Usuario con Logging

```java
public User getUserById(Integer id) {
    logger.debug("Fetching user by ID: {}", id);
    
    if (id == null || id <= 0) {
        logger.warn("Invalid user ID requested: {}", id);
        throw new IllegalArgumentException("Invalid ID");
    }
    
    User user = users.get(id);
    
    if (user == null) {
        logger.warn("User not found: ID={}", id);
        throw new RuntimeException("User not found with ID: " + id);
    }
    
    logger.info("User retrieved: ID={}, Name={}", user.getId(), user.getName());
    return user;
}
```

**Salida esperada (usuario encontrado):**
```
2024-01-15 10:30:46,200 DEBUG UserService - Fetching user by ID: 1
2024-01-15 10:30:46,201 INFO  UserService - User retrieved: ID=1, Name=Juan Pérez
```

**Salida esperada (usuario no encontrado):**
```
2024-01-15 10:30:47,300 DEBUG UserService - Fetching user by ID: 999
2024-01-15 10:30:47,301 WARN  UserService - User not found: ID=999
```

### Ejemplo 3: Actualización con Debug

```java
public User updateUser(Integer id, User userUpdate) {
    logger.info("Updating user: ID={}", id);
    
    User existingUser = getUserById(id);
    logger.debug("Found user to update: {}", existingUser);
    
    existingUser.setName(userUpdate.getName());
    existingUser.setEmail(userUpdate.getEmail());
    existingUser.setAge(userUpdate.getAge());
    
    logger.debug("Updated fields: name='{}', email='{}', age={}", 
                existingUser.getName(), 
                existingUser.getEmail(), 
                existingUser.getAge());
    
    logger.info("User updated successfully: ID={}", id);
    return existingUser;
}
```

### Ejemplo 4: Manejo de Excepciones

```java
public void deleteUser(Integer id) {
    logger.info("Deleting user: ID={}", id);
    
    try {
        User user = getUserById(id);
        users.remove(id);
        logger.info("User deleted: ID={}, Name={}", id, user.getName());
    } catch (RuntimeException e) {
        logger.error("Error deleting user ID={}: {}", id, e.getMessage());
        throw e;
    } catch (Exception e) {
        logger.error("Unexpected error while deleting user: {}", id, e);
        throw new RuntimeException("Failed to delete user", e);
    }
}
```

---

## 📺 Ver Logs en Tiempo Real

### Ver logs por consola (durante ejecución)

```bash
# Ejecutar aplicación
mvn spring-boot:run
```

Los logs aparecerán en la consola:
```
2024-01-15 10:30:45,123 INFO  MonitoringApplication - Starting MonitoringApplication v1.0.0...
2024-01-15 10:30:47,456 INFO  MonitoringApplication - Started MonitoringApplication in 2.333 seconds
```

### Ver archivo de logs con tail

```bash
# Ver los últimos 50 líneas
tail -50 logs/app-rolling.log

# Ver en tiempo real (sigue nuevas líneas)
tail -f logs/app-rolling.log
```

### Ver logs con grep (filtrar)

```bash
# Ver solo errores
grep "ERROR" logs/app-rolling.log

# Ver solo usuarios creados
grep "User created" logs/app-rolling.log

# Ver logs de una clase específica
grep "UserService" logs/app-rolling.log

# Ver logs de un rango horario
grep "10:30" logs/app-rolling.log
```

### Ver logs con menos (paginador)

```bash
# Ver archivo paginado
less logs/app-rolling.log

# Búsqueda dentro de less: /texto
# Siguiente resultado: n
# Anterior resultado: N
# Salir: q
```

---

## 🔧 Cambiar Niveles de Log

### Cambiar nivel en application.yml

**Nivel global:**
```yaml
logging:
  level:
    root: INFO
```

**Nivel por paquete:**
```yaml
logging:
  level:
    root: INFO
    com.example: DEBUG
    org.springframework: WARN
```

**Nivel por clase:**
```yaml
logging:
  level:
    com.example.service.UserService: DEBUG
    com.example.controller.PageController: INFO
```

### Recargar configuración en tiempo de ejecución

Edita `log4j2.xml` y guarda. Log4j2 automáticamente detecta cambios cada 30 segundos.

---

## 📊 Ejemplos de Salidas de Log

### Inicio de Aplicación

```
2024-01-15 10:30:45,123 INFO  MonitoringApplication - Starting MonitoringApplication v1.0.0
2024-01-15 10:30:45,456 INFO  MonitoringApplication - The following profiles are active: default
2024-01-15 10:30:46,789 INFO  EmbeddedTomcatFactory - Tomcat initialized with port(s): 8080
2024-01-15 10:30:47,012 INFO  TomcatWebServer - Tomcat started on port(s): 8080 (http) with context path ''
2024-01-15 10:30:47,234 INFO  MonitoringApplication - Started MonitoringApplication in 2.111 seconds
```

### Creación de Usuario

```
2024-01-15 10:31:00,100 INFO  PageController - GET / - User navigated to home page
2024-01-15 10:31:05,200 INFO  PageController - GET /users - User navigated to users page
2024-01-15 10:31:10,300 INFO  UserService - Creating user: name='Juan', email='juan@example.com'
2024-01-15 10:31:10,301 INFO  UserService - User created successfully with ID: 1
2024-01-15 10:31:10,302 DEBUG UserService - User details: User(id=1, name=Juan, email=juan@example.com, age=30)
```

### Error de Validación

```
2024-01-15 10:32:00,100 INFO  UserService - Creating user: name='', email='test@example.com'
2024-01-15 10:32:00,101 WARN  UserService - Invalid user data: Email must contain @
2024-01-15 10:32:00,102 ERROR PageController - Error creating user: Invalid user data
```

### Usuario No Encontrado

```
2024-01-15 10:33:00,100 DEBUG UserService - Fetching user by ID: 999
2024-01-15 10:33:00,101 WARN  UserService - User not found: ID=999
2024-01-15 10:33:00,102 ERROR PageController - User not found with ID: 999
```

---

## 🐛 Troubleshooting

### Problema: No se crean archivos de log

**Causa:** Permisos insuficientes en carpeta `logs/`

**Solución:**
```bash
# Crear carpeta con permisos
mkdir -p logs
chmod 755 logs
```

### Problema: Logs no aparecen en consola

**Solución 1:** Cambiar nivel en `application.yml`
```yaml
logging:
  level:
    root: DEBUG
```

**Solución 2:** Cambiar nivel en `log4j2.xml`
```xml
<Root level="DEBUG">
```

### Problema: Archivos de log muy grandes

**Solución:** Cambiar tamaño de rotación en `log4j2.xml`
```xml
<SizeBasedTriggeringPolicy size="10 MB" /> <!-- Aumentar a 50 MB -->
```

### Problema: Logs no se comprimen

**Causa:** Carpeta `logs/` no tiene permisos de escritura

**Solución:**
```bash
chmod 755 logs
```

### Problema: Muchos archivos rotados

**Solución:** Aumentar retención en `log4j2.xml`
```xml
<DefaultRolloverStrategy max="5" /> <!-- Mantener solo 5 archivos -->
```

---

## 📈 Patrones de Logging Recomendados

### ✅ Buen Logging

```java
logger.info("User created: ID={}, Name={}", user.getId(), user.getName());
logger.warn("Retry attempt {} for operation", retryCount);
logger.error("Failed to save user", exception);
```

### ❌ Mal Logging

```java
logger.info("Creating user: " + user.getName() + " with email " + user.getEmail());
logger.warn("Retry attempt " + retryCount);
logger.error("Failed: " + exception.toString());
```

**Por qué:**
- Los parámetros `{}` son más eficientes
- Menos concatenación de strings
- Mejor formato y legibilidad

---

## 🔐 Seguridad en Logs

### ⚠️ No loguear información sensible

```java
// ❌ Nunca hagas esto
logger.info("User password: {}", user.getPassword());
logger.info("Credit card: {}", user.getCreditCard());

// ✅ Hazlo así
logger.info("User authenticated successfully");
logger.info("Payment processed for user ID: {}", user.getId());
```

---

## 📚 Recursos

- [Log4j2 Documentation](https://logging.apache.org/log4j/2.x/)
- [Log4j2 Configuration](https://logging.apache.org/log4j/2.x/manual/configuration.html)
- [Log4j2 Appenders](https://logging.apache.org/log4j/2.x/log4j-core/manual/appenders.html)

---

**¡Ahora eres un experto en Log4j2!** 🚀

# 🔍 Guía de SonarQube - Análisis de Calidad de Código

Documentación completa sobre cómo usar SonarQube para analizar la calidad del código de la aplicación.

## 📋 Contenidos

- [¿Qué es SonarQube?](#qué-es-sonarqube)
- [Instalación con Docker](#instalación-con-docker)
- [Primer Análisis](#primer-análisis)
- [Dashboard de SonarQube](#dashboard-de-sonarqube)
- [Métricas de Calidad](#métricas-de-calidad)
- [Reglas de Calidad](#reglas-de-calidad)
- [Ejemplos Prácticos](#ejemplos-prácticos)
- [Troubleshooting](#troubleshooting)

---

## 🤔 ¿Qué es SonarQube?

**SonarQube** es una plataforma de análisis estático de código que ayuda a detectar:

### Tipos de Problemas

| Tipo | Descripción | Ejemplo |
|------|-------------|---------|
| **Bugs** | Errores que causan comportamiento incorrecto | Variable no inicializada |
| **Code Smells** | Código de baja calidad | Método muy largo |
| **Vulnerabilities** | Problemas de seguridad | SQL Injection |
| **Technical Debt** | Deuda técnica acumulada | Código duplicado |
| **Coverage** | Cobertura de tests | % de código testeado |

### Ventajas

✅ Detecta problemas antes de producción
✅ Mejora la calidad del código
✅ Identifica vulnerabilidades de seguridad
✅ Genera reportes detallados
✅ Integración con CI/CD
✅ Dashboard visual

---

## 🐳 Instalación con Docker

### Requisitos

- Docker y Docker Compose instalados
- Puerto 9000 disponible (SonarQube)
- Puerto 5432 disponible (PostgreSQL)

### Paso 1: Iniciar los Contenedores

```bash
cd /Users/o.coscollano/workspace/github/testing_monitorizacion/testing_monitorizacion

# Iniciar SonarQube y PostgreSQL
docker-compose up -d sonarqube db

# Verificar que están corriendo
docker-compose ps
```

**Salida esperada:**
```
NAME                  STATUS
sonarqube             Up 2 minutes (healthy)
sonarqube_db          Up 2 minutes (healthy)
```

### Paso 2: Esperar a que SonarQube esté listo

```bash
# Verificar que SonarQube está disponible
curl -s http://localhost:9000/api/system/health

# Resultado esperado:
# {"health":"GREEN"}
```

**Tiempo de espera:** ~1-2 minutos

### Paso 3: Acceder a SonarQube

Abre en tu navegador: **http://localhost:9000**

**Credenciales por defecto:**
- Usuario: `admin`
- Contraseña: `admin`

---

## 🚀 Primer Análisis

### Opción 1: Script Automático (Recomendado)

```bash
# Dale permisos de ejecución al script
chmod +x sonar-analysis.sh

# Ejecutar análisis completo
./sonar-analysis.sh full

# O solo análisis de SonarQube (si ya compilaste)
./sonar-analysis.sh sonar
```

### Opción 2: Comandos Manuales

**Paso 1: Compilar el proyecto**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
mvn clean package -DskipTests
```

**Paso 2: Ejecutar tests**
```bash
mvn test
```

**Paso 3: Ejecutar análisis de SonarQube**
```bash
mvn sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=admin \
  -Dsonar.password=admin \
  -Dsonar.projectKey=springboot-monitoring-app
```

### Paso 4: Ver Resultados

Una vez completado, accede a:
**http://localhost:9000/projects/springboot-monitoring-app**

---

## 📊 Dashboard de SonarQube

### 1. Vista General (Overview)

**Ubicación:** Home → springboot-monitoring-app

**Muestra:**
- 📈 Métrica general de calidad
- 🐛 Número de bugs
- 🔒 Vulnerabilidades
- 💡 Code Smells
- 📝 Líneas de código
- 🧪 Cobertura de tests

### 2. Issues (Problemas)

**Ubicación:** Issues tab

Muestra todos los problemas encontrados:
- **Bugs:** Críticos
- **Vulnerabilities:** Seguridad
- **Code Smells:** Calidad
- **Hotspots:** Áreas de riesgo

### 3. Code (Código)

**Ubicación:** Code tab

Exploración del código fuente:
- Archivos con problemas
- Líneas problemáticas
- Detalles de cada issue

### 4. Measures (Métricas)

**Ubicación:** Measures tab

Métricas detalladas:
- Lines of Code (LOC)
- Complexity (Complejidad)
- Coverage (Cobertura)
- Duplication (Código duplicado)

---

## 📈 Métricas de Calidad

### 1. Reliability (Confiabilidad)

**¿Qué mide?** Probabilidad de bugs

```
Rating A (1.0% - 3%) = Excelente
Rating B (3% - 10%)  = Bueno
Rating C (10% - 20%) = Aceptable
Rating D (20% - 50%) = Deficiente
Rating E (> 50%)     = Muy deficiente
```

### 2. Security (Seguridad)

**¿Qué mide?** Vulnerabilidades de seguridad

- SQL Injection
- XSS (Cross-Site Scripting)
- Hardcoded passwords
- Etc.

### 3. Maintainability (Mantenibilidad)

**¿Qué mide?** Facilidad de mantener el código

```
Rating A (0-5)      = Excelente
Rating B (6-10)     = Bueno
Rating C (11-20)    = Aceptable
Rating D (21-50)    = Deficiente
Rating E (> 50)     = Muy deficiente
```

### 4. Code Coverage (Cobertura)

**¿Qué mide?** % de código testeado

```
> 80% = Excelente
60-80% = Bueno
40-60% = Aceptable
< 40% = Deficiente
```

### 5. Duplications (Duplicaciones)

**¿Qué mide?** Porcentaje de código duplicado

```
< 3% = Excelente
3-5% = Bueno
5-10% = Aceptable
> 10% = Deficiente
```

---

## 🎯 Reglas de Calidad

### Ejemplos de Reglas Java

| Regla | Problema | Solución |
|-------|----------|----------|
| **Long Method** | Método muy largo | Dividir en métodos más pequeños |
| **Too Many Parameters** | Muchos parámetros | Usar objeto o builder |
| **Unused Variable** | Variable no usada | Eliminar variable |
| **Hardcoded IP** | IP hardcoded | Usar configuración |
| **Empty Catch** | Catch vacío | Loguear o manejar |
| **SQL Injection** | Inyección SQL | Usar prepared statements |

### Configurar Reglas

1. Ir a: **Quality Profiles**
2. Seleccionar: **Java**
3. Cambiar reglas según necesidad

---

## 💡 Ejemplos Prácticos

### Ejemplo 1: Encontrar Bugs

```bash
# Ejecutar análisis
./sonar-analysis.sh sonar

# Ir al dashboard
# Issues → Filter by Type: Bug
```

**Ejemplo de bug encontrado:**
```
Null pointer dereference (línea 45)
User user = null;
user.getName();  // ❌ NullPointerException
```

### Ejemplo 2: Code Smells

```
Method 'getUserById' is too complex (Cyclomatic Complexity = 8)
```

**Solución:** Refactorizar el método

```java
// ❌ Complejo
public User getUserById(Integer id) {
    if (id != null) {
        if (id > 0) {
            User user = users.get(id);
            if (user != null) {
                return user;
            }
        }
    }
    throw new RuntimeException("Invalid user");
}

// ✅ Simplificado
public User getUserById(Integer id) {
    validateId(id);
    User user = users.get(id);
    if (user == null) {
        throw new RuntimeException("User not found");
    }
    return user;
}

private void validateId(Integer id) {
    if (id == null || id <= 0) {
        throw new IllegalArgumentException("Invalid ID");
    }
}
```

### Ejemplo 3: Cobertura de Tests

```
Coverage: 65.3% (Target: 80%)
Missing coverage in: UserService.java (lines 23, 45, 67)
```

**Acción:** Crear tests para esas líneas

### Ejemplo 4: Vulnerabilidades

```
Potential SQL Injection (línea 120)
String query = "SELECT * FROM users WHERE name = " + userInput;
```

**Solución:** Usar prepared statements

```java
// ❌ Vulnerable
String query = "SELECT * FROM users WHERE name = '" + name + "'";

// ✅ Seguro
String query = "SELECT * FROM users WHERE name = ?";
PreparedStatement ps = connection.prepareStatement(query);
ps.setString(1, name);
```

---

## 📊 Monitoreo Continuo

### Generar Reportes Regulares

```bash
# Script para análisis diario
#!/bin/bash
cd /Users/o.coscollano/workspace/github/testing_monitorizacion/testing_monitorizacion
./sonar-analysis.sh full > sonar-report-$(date +%Y%m%d).log
```

### Configurar Alertas

1. **Ir a:** Settings → Alerts
2. **Crear nueva alerta:**
   - Condición: Coverage < 80%
   - Acción: Email notification

### Exportar Métricas

```bash
# API de SonarQube para obtener métricas
curl -s "http://localhost:9000/api/measures/component?component=springboot-monitoring-app&metricKeys=coverage,bugs,vulnerabilities" | jq
```

---

## 🐛 Troubleshooting

### Problema: "Connection refused" a SonarQube

**Causa:** SonarQube no está corriendo

**Solución:**
```bash
docker-compose up -d sonarqube db
# Esperar 1-2 minutos
curl http://localhost:9000/api/system/health
```

### Problema: "Auth failed"

**Causa:** Contraseña incorrecta

**Solución:**
```bash
# En script o comando, usar credenciales correctas
-Dsonar.login=admin
-Dsonar.password=admin
```

### Problema: "Coverage is 0%"

**Causa:** JaCoCo no generó reporte

**Solución:**
```bash
# Asegurar que JaCoCo está configurado
mvn clean test jacoco:report

# Verificar archivo
ls -la target/site/jacoco/jacoco.xml
```

### Problema: "Database not ready"

**Causa:** PostgreSQL no está inicializado

**Solución:**
```bash
# Reiniciar contenedores
docker-compose down
docker-compose up -d sonarqube db
# Esperar 2-3 minutos
```

### Problema: Análisis muy lento

**Causa:** Primer análisis toma más tiempo

**Solución:** Esperar o mejorar hardware

---

## 📈 Quality Gate

### ¿Qué es Quality Gate?

Son criterios para pasar/fallar un análisis.

### Configurar Quality Gate

1. **Ir a:** Quality Gates
2. **Crear nuevo gate:**
   - Coverage >= 80%
   - Bugs <= 0
   - Vulnerabilities <= 0

### Usar en CI/CD

```bash
# En tu pipeline CI/CD
./sonar-analysis.sh full

# Verificar status
curl -s "http://localhost:9000/api/qualitygates/project_status?projectKey=springboot-monitoring-app" | jq
```

---

## 🔄 Integración con GitHub Actions

Crear archivo `.github/workflows/sonarqube.yml`:

```yaml
name: SonarQube Analysis

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
    
    - name: Run tests and SonarQube
      run: |
        mvn clean verify sonar:sonar \
          -Dsonar.host.url=http://localhost:9000 \
          -Dsonar.login=${{ secrets.SONAR_TOKEN }}
```

---

## 📚 Recursos

- [SonarQube Documentation](https://docs.sonarqube.org/)
- [SonarQube Java Plugin](https://docs.sonarqube.org/latest/analysis/languages/java/)
- [JaCoCo Documentation](https://www.jacoco.org/)
- [Quality Gates](https://docs.sonarqube.org/latest/user-guide/quality-gates/)

---

## ✅ Checklist

- [ ] Docker y Docker Compose instalados
- [ ] SonarQube corriendo: `docker-compose up -d sonarqube db`
- [ ] Acceso a http://localhost:9000
- [ ] Proyecto creado: springboot-monitoring-app
- [ ] Análisis completado: `./sonar-analysis.sh full`
- [ ] Dashboard visible con métricas
- [ ] Tests ejecutados correctamente
- [ ] Cobertura >= 60%

---

**¡Ahora eres un experto en SonarQube!** 🚀

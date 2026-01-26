# 🚀 Guía Rápida - SonarQube en 5 Pasos

Instrucciones paso a paso para analizar tu aplicación con SonarQube.

## 📋 Contenidos

- [Requisitos](#requisitos)
- [Paso 1: Iniciar SonarQube](#paso-1-iniciar-sonarqube)
- [Paso 2: Verificar Conexión](#paso-2-verificar-conexión)
- [Paso 3: Ejecutar Análisis](#paso-3-ejecutar-análisis)
- [Paso 4: Ver Resultados](#paso-4-ver-resultados)
- [Paso 5: Interpretar Métricas](#paso-5-interpretar-métricas)
- [Acceso Rápido](#acceso-rápido)

---

## ✅ Requisitos

```bash
# Verificar Docker
docker --version
# Docker version 20.10+ ✓

# Verificar Docker Compose
docker-compose --version
# Docker Compose version 1.29+ ✓

# Verificar Java 17
java -version
# openjdk version "17" ✓
```

---

## ⏱️ Paso 1: Iniciar SonarQube

### Opción A: Usar Script Interactivo (Recomendado)

```bash
chmod +x manage.sh
./manage.sh
# Selecciona opción 3: "Iniciar SonarQube"
```

### Opción B: Comandos Manuales

```bash
# Navegar al proyecto
cd /Users/o.coscollano/workspace/github/testing_monitorizacion/testing_monitorizacion

# Iniciar SonarQube y PostgreSQL
docker-compose up -d sonarqube db

# Esperar 1-2 minutos...
```

### Verificar que está corriendo

```bash
docker-compose ps

# Debe mostrar:
# NAME              STATUS
# sonarqube         Up X minutes (healthy)
# sonarqube_db      Up X minutes (healthy)
```

---

## 🔗 Paso 2: Verificar Conexión

### Verificar que SonarQube está listo

```bash
# En terminal
curl http://localhost:9000/api/system/health

# Resultado esperado:
# {"health":"GREEN"}
```

### Acceder a SonarQube en navegador

1. Abre: **http://localhost:9000**
2. Verás la pantalla de login
3. Credenciales:
   - **Usuario:** admin
   - **Contraseña:** admin

### Cambiar contraseña (Opcional)

1. Login con admin/admin
2. Ir a: **Profile → Security → Change Password**
3. Guardar nueva contraseña

---

## 🔍 Paso 3: Ejecutar Análisis

### Opción A: Script Automático (Fácil)

```bash
# Dale permisos
chmod +x sonar-analysis.sh

# Ejecutar análisis completo
./sonar-analysis.sh full

# El script hace:
# 1. Compilar código
# 2. Ejecutar tests
# 3. Enviar a SonarQube
# 4. Mostrar enlace al dashboard
```

### Opción B: Comandos Manuales

```bash
# Paso 1: Compilar
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
mvn clean package -DskipTests

# Paso 2: Tests
mvn test

# Paso 3: Análisis
mvn sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=admin \
  -Dsonar.password=admin \
  -Dsonar.projectKey=springboot-monitoring-app
```

### Salida esperada

```
[INFO] Scanner configuration:
[INFO]   SonarQube Server: http://localhost:9000
[INFO]   Project key: springboot-monitoring-app
[INFO]   Project name: Spring Boot Monitoring App
[INFO] ...
[INFO] Computing Python AST from /Users/...
[INFO] ANALYSIS SUCCESSFUL
[INFO] Project dashboard available at:
[INFO] http://localhost:9000/dashboard?id=springboot-monitoring-app
```

---

## 📊 Paso 4: Ver Resultados

### Acceder al Dashboard

1. Después de completar el análisis, abre:
   **http://localhost:9000/projects/springboot-monitoring-app**

2. O desde la página principal:
   - Click en **"Spring Boot Monitoring App"**

### Pestaña Overview (Resumen)

Verás:
- 🎯 **Rating General** (A-E)
- 🐛 **Bugs** encontrados
- 🔒 **Vulnerabilities** (Vulnerabilidades)
- 💡 **Code Smells** (Problemas de código)
- 📊 **Coverage** (% tests)
- 📝 **LOC** (Líneas de código)

### Ejemplo de Resultados

```
Rating: A
├── Reliability: A (0 bugs)
├── Security: A (0 vulnerabilities)
├── Maintainability: B (3 code smells)
├── Coverage: 72.5% (27 tests)
└── Duplication: 1.2%
```

---

## 📈 Paso 5: Interpretar Métricas

### Ratings

| Rating | Significado | Acción |
|--------|------------|--------|
| **A** | Excelente | ✓ Sin problemas |
| **B** | Bueno | ⚠ Revisar issues |
| **C** | Aceptable | ⚠ Mejorar código |
| **D** | Deficiente | ❌ Refactorizar |
| **E** | Muy malo | ❌ Reescribir |

### Tipos de Issues

**1. Bugs 🐛**
- Problemas que causan errores
- Ejemplo: Variable no inicializada
- Acción: Fijar inmediatamente

**2. Vulnerabilities 🔒**
- Problemas de seguridad
- Ejemplo: SQL Injection
- Acción: Fijar inmediatamente

**3. Code Smells 💡**
- Código de baja calidad
- Ejemplo: Método muy largo
- Acción: Refactorizar

### Interpretar Coverage

```
Coverage: 72.5%
├── Líneas cubiertas: 174
├── Líneas no cubiertas: 66
└── % = 174 / 240 = 72.5%

Interpretación:
- > 80% = Excelente
- 60-80% = Bueno ✓
- 40-60% = Aceptable
- < 40% = Deficiente
```

### Interpretar Complejidad

```
Complexity: 45
├── Promedio por método: 2.5
├── Máximo en método: 8
└── Mínimo en método: 1

Regla: Complejidad <= 10
✓ Está bien
```

---

## 🎯 Acceso Rápido

### URLs Importantes

```bash
# SonarQube Home
http://localhost:9000

# Dashboard del Proyecto
http://localhost:9000/projects/springboot-monitoring-app

# Issues
http://localhost:9000/issues?resolved=false&projects=springboot-monitoring-app

# Code
http://localhost:9000/code?selected=springboot-monitoring-app

# Measures
http://localhost:9000/measures?id=springboot-monitoring-app
```

### Comandos Útiles

```bash
# Ver estado de SonarQube
docker-compose ps sonarqube

# Ver logs de SonarQube
docker-compose logs -f sonarqube

# Reiniciar SonarQube
docker-compose restart sonarqube

# Parar SonarQube
docker-compose stop sonarqube

# Iniciar nuevamente
docker-compose start sonarqube
```

---

## 🔧 Troubleshooting Rápido

### "Connection refused"
```bash
# SonarQube no está corriendo
docker-compose up -d sonarqube db
sleep 30  # Esperar a que inicie
```

### "Auth failed"
```bash
# Credenciales incorrectas
# Usuario: admin
# Contraseña: admin (por defecto)
```

### "No coverage data"
```bash
# JaCoCo no generó reporte
mvn clean test jacoco:report
```

### "Proyecto no aparece"
```bash
# Ejecutar análisis nuevamente
./sonar-analysis.sh sonar
```

---

## 📝 Checklist

- [ ] Docker está corriendo
- [ ] SonarQube inició: `docker-compose ps`
- [ ] Puedo acceder a http://localhost:9000
- [ ] Credenciales funcionan (admin/admin)
- [ ] Ejecuté análisis: `./sonar-analysis.sh full`
- [ ] Dashboard muestra métricas
- [ ] Veo al menos un issue o metric

---

## 🚀 Siguiente: Mejorar la Calidad

Una vez tengas los resultados:

1. **Fijar Bugs y Vulnerabilities** 🐛🔒
   - Click en cada issue
   - Leer descripción
   - Fijar en código

2. **Refactorizar Code Smells** 💡
   - Simplificar métodos
   - Reducir complejidad
   - Mejorar nombre de variables

3. **Aumentar Coverage** 📊
   - Añadir tests
   - Cubrir líneas rojas
   - Target: >= 80%

4. **Ejecutar análisis nuevamente**
   - `./sonar-analysis.sh full`
   - Ver mejoras en dashboard

---

## 📚 Recursos

- [Guía Completa: SONARQUBE_GUIDE.md](./SONARQUBE_GUIDE.md)
- [SonarQube Docs](https://docs.sonarqube.org/)
- [SonarQube API](https://next.sonarqube.com/sonarqube/web_api)

---

**¡Ahora tienes SonarQube funcionando!** 🎉

Próximo paso: Ejecuta tu primer análisis con `./sonar-analysis.sh full`

# 🤖 Guía de Selenium - Pruebas de la Interfaz Web

Documentación completa para ejecutar y entender las pruebas automatizadas de Selenium.

## 📋 Contenidos

- [¿Qué es Selenium?](#qué-es-selenium)
- [Requisitos](#requisitos)
- [Configuración](#configuración)
- [Ejecutar Tests](#ejecutar-tests)
- [Anatomía de un Test](#anatomía-de-un-test)
- [Ejemplos de Tests](#ejemplos-de-tests)
- [Troubleshooting](#troubleshooting)

---

## 🤔 ¿Qué es Selenium?

**Selenium** es un framework para automatizar pruebas en navegadores web.

### Características

- ✅ Automatización de navegador web
- ✅ Interacción con elementos HTML
- ✅ Validación de contenido
- ✅ Pruebas de responsividad
- ✅ Verificación de estilos CSS
- ✅ Soporte para múltiples navegadores

### En esta Aplicación

**Navegador:** Chrome/Chromium
**WebDriver Manager:** Descarga automática de drivers
**Pruebas incluidas:** 13 tests E2E
**Estado:** Deshabilitadas por defecto (pueden ralentizar build)

---

## 📦 Requisitos

### Software Necesario

- ✅ Java 17+
- ✅ Maven 3.8+
- ✅ Chrome o Chromium instalado
- ✅ Aplicación Spring Boot ejecutándose

### Instalación de Chrome

#### En macOS
```bash
# Con Homebrew
brew install --cask google-chrome

# Verificar instalación
which google-chrome
# Salida: /Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome
```

#### En Linux
```bash
# Debian/Ubuntu
sudo apt-get install google-chrome-stable

# Verificar instalación
which google-chrome
# Salida: /usr/bin/google-chrome
```

#### En Windows
```powershell
# Con Chocolatey
choco install googlechrome

# Verificar
where.exe chrome
```

---

## ⚙️ Configuración

### Dependencias en pom.xml

```xml
<!-- Selenium -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.15.0</version>
</dependency>

<!-- WebDriverManager -->
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.6.2</version>
</dependency>
```

### Configuración en SeleniumUITest.java

```java
@DisplayName("Tests de Selenium - Interfaz Web")
@Disabled("Tests de Selenium deshabilitados por defecto.")
public class SeleniumUITest {
    
    private WebDriver driver;
    private static final String BASE_URL = "http://localhost:8080";
    
    @BeforeEach
    void setUp() {
        // Configurar WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        // Configurar opciones de Chrome
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Descomenta para modo sin interfaz
        
        // Crear driver
        driver = new ChromeDriver(options);
        
        // Esperas implícitas
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    
    @AfterEach
    void tearDown() {
        // Cerrar navegador
        if (driver != null) {
            driver.quit();
        }
    }
}
```

---

## 🚀 Ejecutar Tests

### Opción 1: Habilitar Tests de Forma Permanente

**Paso 1:** Editar `SeleniumUITest.java`
```java
// ❌ Cambiar esto:
@Disabled("Tests de Selenium deshabilitados por defecto.")

// ✅ A esto:
// @Disabled("Tests de Selenium deshabilitados por defecto.")
```

**Paso 2:** Ejecutar aplicación
```bash
mvn spring-boot:run
```

**Paso 3:** En otra terminal, ejecutar tests
```bash
mvn clean test
```

### Opción 2: Ejecutar Selectively (Recomendado)

**Paso 1:** Asegurar que la aplicación está ejecutándose
```bash
mvn spring-boot:run
```

**Paso 2:** En otra terminal
```bash
# Ejecutar SOLO tests de Selenium
mvn test -Dtest=SeleniumUITest -DargLine="--enable-preview"
```

### Opción 3: Usar Script

**Archivo:** `test-selenium.sh`

```bash
#!/bin/bash

echo "🚀 Iniciando tests de Selenium..."

# Verificar que la aplicación está corriendo
echo "✓ Verificando que la aplicación está en puerto 8080..."
if ! curl -s http://localhost:8080/actuator/health > /dev/null; then
    echo "❌ Error: Aplicación no está ejecutándose en http://localhost:8080"
    echo "Inicia la aplicación con: mvn spring-boot:run"
    exit 1
fi

echo "✓ Aplicación detectada"
echo ""
echo "🧪 Ejecutando tests de Selenium..."
mvn test -Dtest=SeleniumUITest

if [ $? -eq 0 ]; then
    echo "✅ Tests completados exitosamente"
else
    echo "❌ Algunos tests fallaron"
    exit 1
fi
```

**Ejecutar:**
```bash
chmod +x test-selenium.sh
./test-selenium.sh
```

---

## 🏗️ Anatomía de un Test

### Estructura Básica

```java
@Test
@DisplayName("Descripción del test")
void nombreDelTest() {
    // ARRANGE: Preparar datos
    String expectedTitle = "Página Principal";
    
    // ACT: Ejecutar acción
    driver.navigate().to(BASE_URL + "/");
    String actualTitle = driver.getTitle();
    
    // ASSERT: Verificar resultado
    assertEquals(expectedTitle, actualTitle);
}
```

### Elementos Comunes

```java
// Encontrar elementos
WebElement element = driver.findElement(By.id("myId"));
WebElement element = driver.findElement(By.name("myName"));
WebElement element = driver.findElement(By.className("myClass"));
WebElement element = driver.findElement(By.xpath("//button[@type='submit']"));
WebElement element = driver.findElement(By.cssSelector(".btn-primary"));

// Interactuar
element.click();                              // Click
element.sendKeys("texto");                    // Escribir
element.clear();                              // Limpiar
element.submit();                             // Enviar form

// Obtener información
String text = element.getText();              // Texto visible
String attr = element.getAttribute("class");  // Atributo
boolean visible = element.isDisplayed();      // ¿Visible?
boolean enabled = element.isEnabled();        // ¿Habilitado?

// Esperar
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(
    ExpectedConditions.presenceOfElementLocated(By.id("myId"))
);
```

---

## 🧪 Ejemplos de Tests

### Test 1: Navegar a Página Principal

```java
@Test
@DisplayName("Navegar a página principal")
void testNavigateToHome() {
    // ACT
    driver.navigate().to(BASE_URL + "/");
    
    // ASSERT
    assertEquals("Monitoreo | Aplicación", driver.getTitle());
    assertTrue(driver.getCurrentUrl().endsWith("/"));
}
```

### Test 2: Crear Usuario desde Formulario

```java
@Test
@DisplayName("Crear usuario desde formulario")
void testCreateUserFromForm() {
    // ARRANGE
    driver.navigate().to(BASE_URL + "/users");
    String userName = "Test User " + System.currentTimeMillis();
    String userEmail = "test" + System.currentTimeMillis() + "@example.com";
    String userAge = "25";
    
    // ACT - Rellenar formulario
    WebElement nameInput = driver.findElement(By.name("name"));
    WebElement emailInput = driver.findElement(By.name("email"));
    WebElement ageInput = driver.findElement(By.name("age"));
    WebElement submitBtn = driver.findElement(By.xpath("//button[@type='submit']"));
    
    nameInput.sendKeys(userName);
    emailInput.sendKeys(userEmail);
    ageInput.sendKeys(userAge);
    
    // ACT - Enviar
    submitBtn.click();
    
    // ASSERT - Esperar a que la tabla se actualice
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    WebElement table = wait.until(
        ExpectedConditions.presenceOfElementLocated(By.id("usersTable"))
    );
    
    String tableText = table.getText();
    assertTrue(tableText.contains(userName));
}
```

### Test 3: Validar Tabla de Usuarios

```java
@Test
@DisplayName("Validar tabla de usuarios cargada")
void testUsersTableLoaded() {
    // ACT
    driver.navigate().to(BASE_URL + "/users");
    
    // ASSERT
    WebElement table = driver.findElement(By.id("usersTable"));
    assertTrue(table.isDisplayed());
    
    List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));
    assertTrue(rows.size() >= 0);
}
```

### Test 4: Navegar a Dashboard

```java
@Test
@DisplayName("Navegar a dashboard de monitoreo")
void testNavigateToMonitoring() {
    // ARRANGE
    driver.navigate().to(BASE_URL + "/");
    
    // ACT
    WebElement monitoringLink = driver.findElement(
        By.linkText("Dashboard de Monitoreo")
    );
    monitoringLink.click();
    
    // ASSERT
    assertEquals("Monitoreo | Metricas", driver.getTitle());
}
```

### Test 5: Validar Responsividad

```java
@Test
@DisplayName("Validar responsividad en mobile")
void testMobileResponsiveness() {
    // ARRANGE
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--start-maximized");
    
    // Simular dispositivo móvil
    Map<String, Object> mobileDevice = new HashMap<>();
    mobileDevice.put("deviceName", "iPhone 12");
    options.setExperimentalOption("mobileEmulation", mobileDevice);
    
    WebDriver mobileDriver = new ChromeDriver(options);
    
    try {
        // ACT
        mobileDriver.navigate().to(BASE_URL + "/");
        
        // ASSERT
        WebElement container = mobileDriver.findElement(By.className("container"));
        assertTrue(container.isDisplayed());
    } finally {
        mobileDriver.quit();
    }
}
```

### Test 6: Validar Estilos CSS

```java
@Test
@DisplayName("Validar estilos CSS aplicados")
void testCSSStyles() {
    // ACT
    driver.navigate().to(BASE_URL + "/");
    WebElement card = driver.findElement(By.className("card"));
    
    // ASSERT
    String backgroundColor = card.getCssValue("background-color");
    String borderRadius = card.getCssValue("border-radius");
    
    assertNotNull(backgroundColor);
    assertTrue(borderRadius.contains("px"));
}
```

### Test 7: Ejecutar JavaScript

```java
@Test
@DisplayName("Ejecutar JavaScript en página")
void testExecuteJavaScript() {
    // ACT
    driver.navigate().to(BASE_URL + "/users");
    
    JavascriptExecutor js = (JavascriptExecutor) driver;
    
    // Scroll to bottom
    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    
    // ASSERT
    Long scrollPosition = (Long) js.executeScript("return window.scrollY;");
    assertTrue(scrollPosition > 0);
}
```

---

## 🎯 Mejores Prácticas

### ✅ Buenas Prácticas

```java
// 1. Usar esperas explícitas
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(
    ExpectedConditions.presenceOfElementLocated(By.id("myId"))
);

// 2. Usar localizadores específicos
By locator = By.cssSelector(".btn-primary[type='submit']");

// 3. Nombres descriptivos
@DisplayName("Crear usuario con nombre válido y email correcto")

// 4. Patrón AAA (Arrange, Act, Assert)
// Preparar -> Actuar -> Verificar
```

### ❌ Malas Prácticas

```java
// 1. Esperas hardcoded
Thread.sleep(5000);  // ❌ No hagas esto

// 2. Localizadores frágiles
By locator = By.xpath("/html/body/div[1]/div[2]/button");  // ❌ Muy específico

// 3. Tests sin descripción
@Test
void test1() { }  // ❌ ¿Qué prueba esto?

// 4. Múltiples asserts sin estructura
assertTrue(...);
assertEquals(...);
assertNotNull(...);
```

---

## 🐛 Troubleshooting

### Problema: ChromeDriver not found

**Causa:** WebDriverManager no puede descargar el driver

**Solución:**
```bash
# Verificar versión de Chrome
google-chrome --version

# Instalar Chrome si no existe
# macOS:
brew install --cask google-chrome

# Linux:
sudo apt-get install google-chrome-stable
```

### Problema: Connection refused (localhost:8080)

**Causa:** Aplicación no está ejecutándose

**Solución:**
```bash
# En otra terminal
mvn spring-boot:run
```

### Problema: Elementos no se encuentran

**Causa:** Selectores incorrectos o elemento no cargado

**Solución:**
```java
// Usar WebDriverWait
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(
    ExpectedConditions.presenceOfElementLocated(By.id("myId"))
);
```

### Problema: TimeoutException

**Causa:** Elemento no aparece en tiempo esperado

**Solución 1:** Aumentar timeout
```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
```

**Solución 2:** Verificar que la aplicación está corriendo
```bash
curl http://localhost:8080/actuator/health
```

### Problema: Test pasa localmente pero falla en CI/CD

**Causa:** Diferencias en el entorno

**Solución:** Usar modo headless
```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--headless");
options.addArguments("--no-sandbox");
driver = new ChromeDriver(options);
```

---

## 📊 Ejecutar Tests con Reporte

### Generar reporte HTML

```bash
mvn test -Dtest=SeleniumUITest surefire-report:report
```

**Reporte en:** `target/site/surefire-report.html`

### Con cobertura

```bash
mvn test -Dtest=SeleniumUITest jacoco:report
```

**Reporte en:** `target/site/jacoco/index.html`

---

## 🔄 Modo Headless (Sin Interfaz)

**Para CI/CD o servidores sin pantalla:**

```java
@BeforeEach
void setUp() {
    WebDriverManager.chromedriver().setup();
    
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");        // Sin interfaz
    options.addArguments("--no-sandbox");      // Para Linux
    options.addArguments("--disable-dev-shm-usage");  // Para Docker
    
    driver = new ChromeDriver(options);
}
```

---

## 📈 Ejemplos de Asserts

```java
// Verificar título
assertEquals("Título esperado", driver.getTitle());

// Verificar URL
assertTrue(driver.getCurrentUrl().contains("/users"));

// Verificar visibilidad
assertTrue(element.isDisplayed());

// Verificar texto
assertEquals("Crear Usuario", button.getText());

// Verificar cantidad
assertEquals(5, elements.size());

// Verificar contenido
assertTrue(table.getText().contains("Juan"));

// Verificar atributo
assertEquals("btn btn-primary", button.getAttribute("class"));
```

---

## 🎓 Flujo Completo de Test

```java
@Test
@DisplayName("Flujo completo: Crear, actualizar y eliminar usuario")
void testCompleteUserFlow() {
    // 1. NAVEGAR
    driver.navigate().to(BASE_URL + "/users");
    
    // 2. CREAR USUARIO
    WebElement nameInput = driver.findElement(By.name("name"));
    nameInput.sendKeys("Juan Pérez");
    
    WebElement emailInput = driver.findElement(By.name("email"));
    emailInput.sendKeys("juan@example.com");
    
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    
    // 3. VERIFICAR EN TABLA
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    WebElement table = wait.until(
        ExpectedConditions.presenceOfElementLocated(By.id("usersTable"))
    );
    assertTrue(table.getText().contains("Juan Pérez"));
    
    // 4. ACTUALIZAR USUARIO
    // ... código para actualizar ...
    
    // 5. ELIMINAR USUARIO
    // ... código para eliminar ...
    
    // 6. VERIFICAR ELIMINACIÓN
    // ... código para verificar ...
}
```

---

**¡Ahora eres un experto en Selenium!** 🚀

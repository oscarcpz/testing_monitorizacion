package com.example.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de Selenium - Interfaz Web")
class SeleniumUITest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        // Usar WebDriverManager para gestionar automáticamente los drivers
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Navegar a página principal")
    void testNavigateToIndex() {
        driver.navigate().to(BASE_URL + "/");
        
        String title = driver.getTitle();
        assertEquals("Aplicación de Monitoreo", title);
        
        WebElement heading = driver.findElement(By.tagName("h1"));
        assertTrue(heading.getText().contains("Aplicación de Monitoreo"));
    }

    @Test
    @DisplayName("Validar navegación desde página principal")
    void testNavigationMenu() {
        driver.navigate().to(BASE_URL + "/");
        
        // Verificar que existen los links de navegación
        WebElement usersLink = driver.findElement(By.linkText("Usuarios"));
        assertNotNull(usersLink);
        
        WebElement monitoringLink = driver.findElement(By.linkText("Monitoreo"));
        assertNotNull(monitoringLink);
    }

    @Test
    @DisplayName("Navegar a página de usuarios")
    void testNavigateToUsersPage() {
        driver.navigate().to(BASE_URL + "/users");
        
        // Esperar a que la tabla esté presente
        WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));
        assertNotNull(table);
        
        WebElement heading = driver.findElement(By.tagName("h1"));
        assertTrue(heading.getText().contains("Gestión de Usuarios"));
    }

    @Test
    @DisplayName("Crear usuario desde formulario")
    void testCreateUserFromForm() {
        driver.navigate().to(BASE_URL + "/users");
        
        // Esperar a que los campos estén presentes
        WebElement nameInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement ageInput = driver.findElement(By.id("age"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        
        // Rellenar el formulario
        nameInput.sendKeys("Test User");
        emailInput.sendKeys("testuser@example.com");
        ageInput.sendKeys("25");
        
        // Enviar el formulario
        submitButton.click();
        
        // Esperar alerta de confirmación
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        assertTrue(alertText.contains("exitosamente") || alertText.contains("creado"));
        alert.accept();
    }

    @Test
    @DisplayName("Validar tabla de usuarios cargada")
    void testUsersTableLoaded() {
        driver.navigate().to(BASE_URL + "/users");
        
        WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));
        WebElement tbody = table.findElement(By.tagName("tbody"));
        
        assertNotNull(tbody);
    }

    @Test
    @DisplayName("Validar contador de usuarios")
    void testUserCountDisplay() {
        driver.navigate().to(BASE_URL + "/users");
        
        WebElement userCountElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userCount")));
        assertNotNull(userCountElement);
        
        String countText = userCountElement.getText();
        assertFalse(countText.isEmpty());
    }

    @Test
    @DisplayName("Navegar a dashboard de monitoreo")
    void testNavigateToMonitoringPage() {
        driver.navigate().to(BASE_URL + "/monitoring");
        
        WebElement heading = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));
        assertTrue(heading.getText().contains("Dashboard"));
    }

    @Test
    @DisplayName("Validar tarjetas de métricas en dashboard")
    void testMetricsCardsPresent() {
        driver.navigate().to(BASE_URL + "/monitoring");
        
        // Esperar a que las tarjetas de métricas estén presentes
        java.util.List<WebElement> metricCards = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("metric-card"))
        );
        
        assertTrue(metricCards.size() >= 4, "Debe haber al menos 4 tarjetas de métricas");
    }

    @Test
    @DisplayName("Validar estado del sistema en dashboard")
    void testSystemStatusDisplay() {
        driver.navigate().to(BASE_URL + "/monitoring");
        
        WebElement statusSection = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.className("status-section"))
        );
        
        assertNotNull(statusSection);
        String statusText = statusSection.getText();
        assertTrue(statusText.contains("Aplicación") || statusText.contains("Sistema"));
    }

    @Test
    @DisplayName("Validar botón de actualización de métricas")
    void testRefreshMetricsButton() {
        driver.navigate().to(BASE_URL + "/monitoring");
        
        WebElement refreshButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.className("refresh-btn"))
        );
        
        assertNotNull(refreshButton);
        assertTrue(refreshButton.isDisplayed());
        assertTrue(refreshButton.isEnabled());
    }

    @Test
    @DisplayName("Validar link a Prometheus")
    void testPrometheusLinkPresent() {
        driver.navigate().to(BASE_URL + "/");
        
        // Buscar link a Prometheus en la navegación
        java.util.List<WebElement> links = driver.findElements(By.tagName("a"));
        boolean prometheusLinkFound = links.stream()
                .anyMatch(link -> link.getAttribute("href") != null && 
                         link.getAttribute("href").contains("prometheus"));
        
        assertTrue(prometheusLinkFound, "Link a Prometheus debe estar presente");
    }

    @Test
    @DisplayName("Validar responsividad de la interfaz")
    void testResponsiveDesign() {
        // Viewport de desktop
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.navigate().to(BASE_URL + "/users");
        
        WebElement container = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.className("container"))
        );
        
        assertNotNull(container);
        assertTrue(container.isDisplayed());
        
        // Viewport de tablet
        driver.manage().window().setSize(new Dimension(768, 1024));
        assertTrue(container.isDisplayed());
        
        // Viewport de mobile
        driver.manage().window().setSize(new Dimension(375, 667));
        assertTrue(container.isDisplayed());
    }

    @Test
    @DisplayName("Validar estilos aplicados correctamente")
    void testCSSStyling() {
        driver.navigate().to(BASE_URL + "/users");
        
        WebElement content = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.className("content"))
        );
        
        String backgroundColor = content.getCssValue("background-color");
        assertNotNull(backgroundColor);
        
        String borderRadius = content.getCssValue("border-radius");
        assertTrue(!borderRadius.isEmpty() && !borderRadius.equals("0px"));
    }
}

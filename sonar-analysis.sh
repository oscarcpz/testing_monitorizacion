#!/bin/bash

# ============================================================================
# Script para ejecutar análisis de calidad con SonarQube
# ============================================================================

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuración
SONARQUBE_URL="${SONARQUBE_URL:-http://localhost:9000}"
SONARQUBE_TOKEN=""
JAVA_HOME=$(/usr/libexec/java_home -v 17)

echo -e "${BLUE}================================${NC}"
echo -e "${BLUE}  SonarQube Analysis Script${NC}"
echo -e "${BLUE}================================${NC}"
echo ""

# Función para solicitar token de SonarQube
request_sonarqube_token() {
    if [ -z "$SONARQUBE_TOKEN" ]; then
        echo -e "${YELLOW}Se requiere un token de SonarQube para ejecutar el análisis${NC}"
        echo -e "${YELLOW}Puedes generarlo en: ${SONARQUBE_URL}/account/security${NC}"
        echo ""
        read -sp "Introduce tu token de SonarQube: " SONARQUBE_TOKEN
        echo ""
        if [ -z "$SONARQUBE_TOKEN" ]; then
            echo -e "${RED}✗ Token requerido${NC}"
            exit 1
        fi
    fi
}

# Función para verificar SonarQube
check_sonarqube() {
    echo -e "${YELLOW}Verificando conexión a SonarQube...${NC}"
    
    if curl -s -f "${SONARQUBE_URL}/" 2>/dev/null | grep -q "SonarQube"; then
        echo -e "${GREEN}✓ SonarQube está disponible en ${SONARQUBE_URL}${NC}"
    else
        echo -e "${RED}✗ Error: No se puede conectar a SonarQube en ${SONARQUBE_URL}${NC}"
        echo -e "${YELLOW}Inicia SonarQube con: docker compose up -d sonarqube${NC}"
        exit 1
    fi
}

# Función para compilar el proyecto
compile_project() {
    echo -e "${YELLOW}Compilando proyecto...${NC}"
    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
    mvn clean package -DskipTests -q
    echo -e "${GREEN}✓ Compilación exitosa${NC}"
}

# Función para ejecutar tests
run_tests() {
    echo -e "${YELLOW}Ejecutando tests...${NC}"
    mvn test -q
    echo -e "${GREEN}✓ Tests ejecutados${NC}"
}

# Función para ejecutar análisis de SonarQube
run_sonarqube_analysis() {
    echo -e "${YELLOW}Ejecutando análisis de SonarQube...${NC}"
    
    request_sonarqube_token
    
    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
    
    mvn sonar:sonar \
        -Dsonar.host.url="${SONARQUBE_URL}" \
        -Dsonar.login="${SONARQUBE_TOKEN}" \
        -Dsonar.projectKey=springboot-monitoring-app \
        -Dsonar.projectName="Spring Boot Monitoring App" \
        -Dsonar.projectVersion=1.0.0 \
        -Dsonar.sources=src/main \
        -Dsonar.tests=src/test \
        -Dsonar.java.source=17 \
        -Dsonar.java.binaries=target/classes \
        -Dsonar.java.test.binaries=target/test-classes \
        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
    
    echo -e "${GREEN}✓ Análisis de SonarQube completado${NC}"
}

# Función para mostrar el resultado
show_result() {
    echo ""
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}  Análisis Completado${NC}"
    echo -e "${BLUE}================================${NC}"
    echo ""
    echo -e "${GREEN}Accede al dashboard de SonarQube:${NC}"
    echo -e "${YELLOW}${SONARQUBE_URL}${NC}"
    echo ""
    echo -e "${GREEN}Proyecto: springboot-monitoring-app${NC}"
    echo ""
}

# Función para mostrar opciones
show_usage() {
    echo "Uso: ./sonar-analysis.sh [opción]"
    echo ""
    echo "Opciones:"
    echo "  full       - Análisis completo (compilar, tests, SonarQube)"
    echo "  compile    - Solo compilar"
    echo "  test       - Solo ejecutar tests"
    echo "  sonar      - Solo análisis de SonarQube"
    echo "  check      - Solo verificar conexión"
    echo "  help       - Mostrar esta ayuda"
    echo ""
    echo "Ejemplo:"
    echo "  ./sonar-analysis.sh full"
    echo ""
}

# Main
main() {
    local option=${1:-full}
    
    case "$option" in
        full)
            check_sonarqube
            compile_project
            run_tests
            run_sonarqube_analysis
            show_result
            ;;
        compile)
            compile_project
            ;;
        test)
            run_tests
            ;;
        sonar)
            check_sonarqube
            run_sonarqube_analysis
            ;;
        check)
            check_sonarqube
            ;;
        help)
            show_usage
            ;;
        *)
            echo -e "${RED}Opción desconocida: $option${NC}"
            show_usage
            exit 1
            ;;
    esac
}

main "$@"

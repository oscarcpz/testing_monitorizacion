#!/bin/bash

# ============================================================================
# Script para gestionar la infraestructura completa
# ============================================================================

set -e

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
MAGENTA='\033[0;35m'
NC='\033[0m'

# Configuración
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_COMPOSE_FILE="${PROJECT_DIR}/docker-compose.yml"

# ============================================================================
# Funciones
# ============================================================================

print_header() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}  $1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

print_section() {
    echo -e "\n${MAGENTA}▶ $1${NC}\n"
}

# Función para mostrar el menú
show_menu() {
    print_header "Gestor de Infraestructura"
    
    echo "Opciones disponibles:"
    echo ""
    echo "  ${BLUE}1${NC}) Iniciar todo (App + Prometheus + SonarQube + BD)"
    echo "  ${BLUE}2${NC}) Iniciar aplicación y Prometheus"
    echo "  ${BLUE}3${NC}) Iniciar SonarQube"
    echo "  ${BLUE}4${NC}) Parar todo"
    echo "  ${BLUE}5${NC}) Ver logs"
    echo "  ${BLUE}6${NC}) Estado de servicios"
    echo "  ${BLUE}7${NC}) Ejecutar tests"
    echo "  ${BLUE}8${NC}) Análisis SonarQube"
    echo "  ${BLUE}9${NC}) Limpiar todo (containers y volúmenes)"
    echo "  ${BLUE}10${NC}) Ver URLs de acceso"
    echo "  ${BLUE}11${NC}) Salir"
    echo ""
}

# Iniciar todo
start_all() {
    print_header "Iniciando Infraestructura Completa"
    
    print_section "Iniciando Docker Compose"
    docker-compose up -d
    
    print_success "Docker Compose iniciado"
    
    print_section "Esperando que los servicios estén listos..."
    sleep 5
    
    # Verificar servicios
    check_services
}

# Iniciar solo App + Prometheus
start_app_prometheus() {
    print_header "Iniciando App + Prometheus"
    
    print_section "Iniciando Docker Compose (sin SonarQube)"
    docker-compose up -d app prometheus
    
    print_success "App y Prometheus iniciados"
    sleep 3
    check_services
}

# Iniciar SonarQube
start_sonarqube() {
    print_header "Iniciando SonarQube"
    
    print_section "Iniciando SonarQube y PostgreSQL"
    docker-compose up -d sonarqube db
    
    print_success "SonarQube iniciado"
    print_info "Esperando a que SonarQube esté listo (esto puede tomar 1-2 minutos)..."
    
    # Esperar a que SonarQube esté listo
    local max_attempts=30
    local attempt=0
    
    while [ $attempt -lt $max_attempts ]; do
        if curl -s -f http://localhost:9000/api/system/health > /dev/null 2>&1; then
            print_success "SonarQube está listo"
            break
        fi
        echo -n "."
        sleep 2
        ((attempt++))
    done
    
    if [ $attempt -eq $max_attempts ]; then
        print_error "Timeout esperando a SonarQube"
    fi
}

# Parar todo
stop_all() {
    print_header "Parando Servicios"
    
    print_section "Deteniendo Docker Compose"
    docker-compose down
    
    print_success "Servicios detenidos"
}

# Ver logs
view_logs() {
    echo ""
    echo "¿Qué logs deseas ver?"
    echo "  1) Aplicación"
    echo "  2) Prometheus"
    echo "  3) SonarQube"
    echo "  4) Todos"
    echo ""
    read -p "Selecciona opción (1-4): " log_option
    
    case $log_option in
        1)
            docker-compose logs -f app
            ;;
        2)
            docker-compose logs -f prometheus
            ;;
        3)
            docker-compose logs -f sonarqube
            ;;
        4)
            docker-compose logs -f
            ;;
        *)
            print_error "Opción inválida"
            ;;
    esac
}

# Ver estado
check_services() {
    print_section "Estado de Servicios"
    docker-compose ps
}

# Ejecutar tests
run_tests() {
    print_header "Ejecutando Tests"
    
    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
    
    print_section "Compilando y ejecutando tests"
    mvn clean test
    
    print_success "Tests completados"
}

# Análisis SonarQube
run_sonarqube_analysis() {
    print_header "Ejecutando Análisis de SonarQube"
    
    # Verificar que SonarQube esté disponible
    if ! curl -s -f http://localhost:9000/api/system/health > /dev/null 2>&1; then
        print_error "SonarQube no está disponible"
        print_info "Inicia SonarQube con: ./manage.sh"
        return 1
    fi
    
    print_section "Ejecutando análisis"
    
    if [ -f "./sonar-analysis.sh" ]; then
        chmod +x ./sonar-analysis.sh
        ./sonar-analysis.sh full
    else
        print_error "Script sonar-analysis.sh no encontrado"
    fi
}

# Ver URLs
show_urls() {
    print_header "URLs de Acceso"
    
    echo ""
    echo -e "${GREEN}Aplicación:${NC}"
    echo "  http://localhost:8080"
    echo "  http://localhost:8080/users"
    echo "  http://localhost:8080/monitoring"
    echo ""
    
    echo -e "${GREEN}Prometheus:${NC}"
    echo "  http://localhost:9090"
    echo "  http://localhost:9090/graph"
    echo ""
    
    echo -e "${GREEN}SonarQube:${NC}"
    echo "  http://localhost:9000"
    echo "  Usuario: admin"
    echo "  Contraseña: admin"
    echo ""
    
    echo -e "${GREEN}Métricas:${NC}"
    echo "  http://localhost:8080/actuator/health"
    echo "  http://localhost:8080/actuator/prometheus"
    echo ""
    
    echo -e "${GREEN}Logs:${NC}"
    echo "  Archivo: logs/app-rolling.log"
    echo "  Comando: tail -f logs/app-rolling.log"
    echo ""
}

# Limpiar todo
cleanup_all() {
    print_header "Limpieza Completa"
    
    print_warning "Esto eliminará todos los containers y volúmenes"
    read -p "¿Estás seguro? (s/n): " confirm
    
    if [ "$confirm" != "s" ]; then
        print_info "Operación cancelada"
        return
    fi
    
    print_section "Deteniendo servicios"
    docker-compose down
    
    print_section "Eliminando volúmenes"
    docker-compose down -v
    
    print_section "Eliminando contenedores dangling"
    docker container prune -f
    
    print_success "Limpieza completada"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

# ============================================================================
# Main
# ============================================================================

main() {
    cd "$PROJECT_DIR"
    
    while true; do
        show_menu
        read -p "Selecciona una opción (1-11): " option
        
        echo ""
        
        case $option in
            1)
                start_all
                ;;
            2)
                start_app_prometheus
                ;;
            3)
                start_sonarqube
                ;;
            4)
                stop_all
                ;;
            5)
                view_logs
                ;;
            6)
                check_services
                ;;
            7)
                run_tests
                ;;
            8)
                run_sonarqube_analysis
                ;;
            9)
                cleanup_all
                ;;
            10)
                show_urls
                ;;
            11)
                print_success "¡Hasta luego!"
                exit 0
                ;;
            *)
                print_error "Opción inválida. Por favor, selecciona 1-11"
                ;;
        esac
        
        read -p "Presiona Enter para continuar..."
    done
}

# Ejecutar main
main

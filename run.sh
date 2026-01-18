#!/bin/bash

# Script para ejecutar la aplicación Spring Boot

export JAVA_HOME=$(/usr/libexec/java_home -v 17)

echo "🚀 Iniciando Aplicación Spring Boot..."
echo "📊 Monitoreo con Log4j2 y Prometheus"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "✅ Aplicación disponible en: http://localhost:8080"
echo "📈 Prometheus en: http://localhost:8080/actuator/prometheus"
echo "📊 Métricas en: http://localhost:8080/actuator/metrics"
echo "❤️ Salud en: http://localhost:8080/actuator/health"
echo ""
echo "📂 Logs disponibles en: ./logs/"
echo ""
echo "Presiona Ctrl+C para detener la aplicación"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

cd "$(dirname "$0")"
mvn spring-boot:run

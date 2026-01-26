#!/bin/bash

# Script para ejecutar todos los tests

export JAVA_HOME=$(/usr/libexec/java_home -v 17)

echo "🧪 Ejecutando Tests Unitarios..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

cd "$(dirname "$0")"

echo "1️⃣  Tests del Modelo (User)..."
mvn test -Dtest=UserTest
echo ""

echo "2️⃣  Tests del Servicio (UserService)..."
mvn test -Dtest=UserServiceTest
echo ""

echo "3️⃣  Tests de Integración (Controller)..."
mvn test -Dtest=ControllerIntegrationTest
echo ""

echo "✅ Tests completados exitosamente!"
echo ""
echo "📊 Reporte de cobertura en: target/site/jacoco/index.html"

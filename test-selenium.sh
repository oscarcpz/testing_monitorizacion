#!/bin/bash

# Script para ejecutar tests con Selenium

export JAVA_HOME=$(/usr/libexec/java_home -v 17)

echo "🌐 Tests de Selenium - Interfaz Web"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "⚠️  La aplicación debe estar ejecutándose en http://localhost:8080"
echo ""
echo "Para iniciar la aplicación en otra terminal:"
echo "  ./run.sh"
echo ""
echo "Presiona Enter para continuar..."
read

cd "$(dirname "$0")"

echo "🚀 Ejecutando tests de Selenium..."
mvn test -Dtest=SeleniumUITest

echo ""
echo "✅ Tests de Selenium completados!"

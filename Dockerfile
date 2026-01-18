# Etapa 1: Construcción
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar archivos de proyecto
COPY pom.xml .
COPY src ./src

# Compilar y empaquetar
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar JAR desde etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Crear directorio de logs
RUN mkdir -p logs

# Exponer puerto
EXPOSE 8080

# Variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

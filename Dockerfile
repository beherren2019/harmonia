# --- Stage 1: Build the application ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
# Cache dependencies before copying source code
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime image ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Run as non-root user for security compliance
RUN addgroup -S harmoniagroup && adduser -S harmoniauser -G harmoniagroup
USER harmoniauser

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]
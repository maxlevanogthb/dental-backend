# Fase 1: Compilar la aplicación con Maven y Java 17 (Temurin)
FROM maven:3.8.6-eclipse-temurin-17 AS build
COPY . .
# Forzamos a Maven a ignorar la fase de ejecución del frontend-maven-plugin
RUN mvn clean package -DskipTests -Dplugins.frontend.skip=true

# Fase 2: Crear el contenedor ligero de ejecución con Java 17
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /target/dental-backend-0.0.1-SNAPSHOT.jar dental-backend.jar

# Exponer el puerto que Render le dará dinámicamente
EXPOSE 10000

# Ejecutar el jar indicando el puerto dinámico de Render
ENTRYPOINT ["java", "-jar", "dental-backend.jar", "--server.port=10000"]
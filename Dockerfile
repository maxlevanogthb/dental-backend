# Fase 1: Compilar la aplicación con Maven y Java 17
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Fase 2: Crear el contenedor ligero de ejecución
FROM openjdk:17-jdk-slim
COPY --from=build /target/dental-backend-0.0.1-SNAPSHOT.jar dental-backend.jar

# Exponer el puerto que Render le dará dinámicamente
EXPOSE 10000

# Ejecutar el jar indicando el puerto dinámico de Render
ENTRYPOINT ["java", "-jar", "dental-backend.jar", "--server.port=10000"]
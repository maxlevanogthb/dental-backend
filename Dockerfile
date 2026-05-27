# Fase 1: Compilar la aplicación con Maven y Java 17 (Temurin)
FROM maven:3.8.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests -Dskip.npm=true -Dskip.node=true -Dfrontend.user.skip=true

# Fase 2: Crear el contenedor ligero de ejecución con Java 17
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /target/dental-backend-0.0.1-SNAPSHOT.jar dental-backend.jar

# Dejamos que Render maneje el puerto de forma dinámica
ENTRYPOINT ["sh", "-c", "java -jar dental-backend.jar --server.port=${PORT}"]
###############################################
# 1) Construir FRONTEND (Angular)
###############################################
FROM node:18 AS frontend-build

WORKDIR /app
COPY almacena/ ./almacena/

WORKDIR /app/almacena
RUN npm install
RUN npm run build --prod

###############################################
# 2) Construir BACKEND (Spring Boot con Gradle)
###############################################
FROM gradle:8.5-jdk21 AS backend-build

WORKDIR /app/almacenaws
COPY almacenaws/ ./

# Copiar el dist del frontend al backend
COPY --from=frontend-build /app/almacena/dist/almacena/ /app/almacenaws/src/main/resources/static/

# Construir el JAR
RUN gradle build --no-daemon

###############################################
# 3) Imagen final para producción
###############################################
FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=backend-build /app/almacenaws/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]


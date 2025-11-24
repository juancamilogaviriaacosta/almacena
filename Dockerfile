###############################################
# 1) Construir FRONTEND (Angular)
###############################################
FROM node:22 AS frontend-build

WORKDIR /app/almacena
COPY almacena/package*.json ./

# Configuración para evitar errores de red en Railway
RUN npm config set fetch-retries 5 \
  && npm config set fetch-timeout 300000 \
  && npm config set fetch-retry-mintimeout 20000 \
  && npm config set registry https://registry.npmjs.org/

# Instalar dependencias de manera confiable
RUN npm ci --prefer-offline --no-audit --progress=false

# Ahora copiar el resto del código
COPY almacena/ .

# Construir Angular
RUN npm run build

###############################################
# 2) Construir BACKEND (Spring Boot + Gradle)
###############################################
FROM gradle:8.5-jdk21 AS backend-build

WORKDIR /app/almacenaws
COPY almacenaws/ ./

# Copiar el dist del frontend al backend
COPY --from=frontend-build /app/almacena/dist/almacena/ /app/almacenaws/src/main/resources/static/

# Construir el JAR
RUN gradle build --no-daemon -x test


###############################################
# 3) Imagen final para producción
###############################################
FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=backend-build /app/almacenaws/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]


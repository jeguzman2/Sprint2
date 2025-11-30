# ---------- STAGE 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Crea el directorio de la app dentro del contenedor
WORKDIR /app

# Copia los archivos de Maven
COPY pom.xml .
COPY src ./src

# Construye el JAR sin correr tests (para que sea más rápido)
RUN mvn -q -e -DskipTests clean package


# ---------- STAGE 2: Runtime ----------
FROM eclipse-temurin:21-jdk

# Crea un directorio para la app
WORKDIR /app

# Copia el jar que se construyó en el stage 1
COPY --from=builder /app/target/*.jar app.jar

# Indica el puerto
EXPOSE 8080

# Variables de entorno que Render te pedirá luego
ENV DB_ADDRESS="dpg-d4ls1deuk2gs738ivaj0-a.oregon-postgres.render.com"
ENV DB_USER="provesi_user"
ENV DB_PASSWORD="9PHcDK46Qk5VXmq112JWUEf41y4dRFAf"

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]

# 1️⃣ Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Correct path
WORKDIR /app/msvc-ususers

# Copy parent POM at root
COPY ./pom.xml /app/pom.xml

# Copy child POM + wrapper
COPY ./msvc-ususers/.mvn ./.mvn
COPY ./msvc-ususers/mvnw .
COPY ./msvc-ususers/pom.xml .

# Download deps
RUN ./mvnw dependency:go-offline

# Copy sources
COPY ./msvc-ususers/src ./src

# Build
RUN ./mvnw clean package -DskipTests

# 2️⃣ Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN mkdir ./logs
COPY --from=build /app/msvc-ususers/target/msvc-ususers-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8001

CMD ["java", "-jar", "app.jar"]

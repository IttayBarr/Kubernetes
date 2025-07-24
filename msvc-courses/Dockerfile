# 1️⃣ Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Correct path
WORKDIR /app/msvc-courses

# Copy parent POM at root
COPY ./pom.xml /app/pom.xml

# Copy child POM + wrapper
COPY ./msvc-courses/.mvn ./.mvn
COPY ./msvc-courses/mvnw .
COPY ./msvc-courses/pom.xml .

# Download deps
RUN ./mvnw dependency:go-offline

# Copy sources
COPY ./msvc-courses/src ./src

# Build
RUN ./mvnw clean package -DskipTests

# 2️⃣ Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN mkdir ./logs
COPY --from=build /app/msvc-courses/target/msvc-courses-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8002
CMD ["java", "-jar", "app.jar"]

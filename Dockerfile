# Etap budowy aplikacji
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package

# Etap uruchamiania aplikacji
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar /app/demo-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/demo-0.0.1-SNAPSHOT.jar"]
# Użyj obrazu Javy jako bazowego
FROM openjdk:17-jdk-slim

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj pliki projektu do kontenera
COPY target/kolejka2-0.0.1-SNAPSHOT.jar /app/kolejka2-0.0.1-SNAPSHOT.jar

# Uruchom aplikację Java
CMD ["java", "-jar", "kolejka2-0.0.1-SNAPSHOT.jar"]
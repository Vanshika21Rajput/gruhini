FROM maven:3.8.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY . /app
RUN mvn -DskipTests clean package

# -------- Runtime Stage --------
FROM eclipse-temurin:21-jdk
WORKDIR /apps

COPY --from=build /app/target/Gruhani-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar","--server.address=0.0.0.0"]
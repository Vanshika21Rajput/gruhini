# ---------- BUILD STAGE ----------
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
# Cache dependencies separately from source code
COPY pom.xml .
RUN mvn dependency:go-offline -q
# Copy source and build, skipping tests for faster builds
COPY src ./src
RUN mvn clean package -DskipTests -q
# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Create a non-root user for security
RUN groupadd -r gruhani && useradd -r -g gruhani gruhani
COPY --from=builder /app/target/Gruhani-0.0.1-SNAPSHOT.jar app.jar
# Fix ownership
RUN chown gruhani:gruhani app.jar
USER gruhani
EXPOSE 8085
# Use exec form with JVM tuning flags
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]

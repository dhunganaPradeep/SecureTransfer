# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY data ./data
RUN mvn clean package -DskipTests

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jdk

# Install dependencies for JavaFX
RUN apt-get update && apt-get install -y \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgl1 \
    libgtk-3-0 \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy built application from the build stage
COPY --from=build /app/target/secure-file-transfer-1.0.0.jar ./secure-file-transfer-1.0.0.jar

# Expose ports for backend and frontend
EXPOSE 8080 8081

# Environment variables for dynamic port and database configuration
ENV SERVER_PORT=8080
ENV DATABASE_URL=jdbc:h2:mem:instance1
ENV DISPLAY=${DISPLAY}

# Run the application
CMD java -jar \
    -Dserver.port=$SERVER_PORT \
    -Dspring.datasource.url=$DATABASE_URL \
    secure-file-transfer-1.0.0.jar


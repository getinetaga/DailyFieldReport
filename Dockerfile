# Multi-stage build for Daily Field Report Application
FROM maven:3.9.4-eclipse-temurin-23 AS build

# Set working directory
WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:23-jre-alpine

# Install required packages
RUN apk --no-cache add \
    curl \
    bash \
    tzdata \
    fontconfig \
    ttf-liberation

# Set timezone
ENV TZ=UTC

# Create app user
RUN addgroup -g 1001 appgroup && \
    adduser -u 1001 -G appgroup -s /bin/bash -D appuser

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/target/lib/ lib/

# Create necessary directories
RUN mkdir -p /app/exports /app/logs /app/config && \
    chown -R appuser:appgroup /app

# Create exports directory with proper permissions
VOLUME ["/app/exports", "/app/logs"]

# Switch to non-root user
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expose port
EXPOSE 8080

# Environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m" \
    SERVER_PORT=8080 \
    LOGGING_LEVEL_ROOT=INFO \
    SPRING_PROFILES_ACTIVE=docker

# Startup command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Labels for metadata
LABEL maintainer="Field Report Systems <support@fieldreport.com>" \
      version="1.0.0" \
      description="Daily Field Report Management System" \
      org.opencontainers.image.source="https://github.com/getinetaga/DailyFieldReport" \
      org.opencontainers.image.documentation="https://github.com/getinetaga/DailyFieldReport/blob/main/README.md" \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.title="Daily Field Report" \
      org.opencontainers.image.description="Comprehensive field reporting system for construction and project management"
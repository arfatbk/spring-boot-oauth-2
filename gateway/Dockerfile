# Build stage
FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /builder

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.javadoc.skip=true

# Rename the jar file to application.jar
RUN mv target/*.jar application.jar
# Extract the jar file using an efficient layout
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# Runtime stage
FROM eclipse-temurin:25-jre
WORKDIR /app

# Add metadata labels
LABEL maintainer="finsolis"
LABEL version="1.0"
LABEL description="Finsolis Gateway Service"

# Create non-root user with explicit UID/GID (use 1001 to avoid conflicts)
RUN groupadd -r spring -g 1001 && useradd -r -u 1001 -g spring spring

# Copy layers from builder stage
COPY --from=builder --chown=1001:1001 /builder/extracted/dependencies/ ./
COPY --from=builder --chown=1001:1001 /builder/extracted/spring-boot-loader/ ./
COPY --from=builder --chown=1001:1001 /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=1001:1001 /builder/extracted/application/ ./

# Switch to non-root user
USER 1001:1001

# Expose port
EXPOSE 8080

# Add healthcheck - install curl first as root, then switch back to user
USER root
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
USER 1001:1001

# Add healthcheck using curl
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1


# Start the application with optimized JVM flags for containers
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    "-XX:+OptimizeStringConcat", \
    "-XX:+UseStringDeduplication", \
    "-jar", \
    "application.jar"]

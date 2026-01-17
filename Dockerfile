#############################################
#              DOCKERFILE                    #
#############################################
#
# A Dockerfile is a recipe for creating a Docker image.
# Each line is an instruction that adds a layer to the image.
#
# WHY DOCKER MATTERS FOR CI/CD:
# 1. Consistency: Same container runs anywhere (dev, test, prod)
# 2. Isolation: App runs in its own environment
# 3. Portability: Easy to deploy to any cloud/server
# 4. Security: Can scan images for vulnerabilities
#
# This uses a MULTI-STAGE BUILD:
# Stage 1: Build the application (needs Maven, JDK)
# Stage 2: Run the application (only needs JRE)
# This makes the final image much smaller and more secure!

#############################################
# STAGE 1: BUILD
#############################################
# We use Maven to build the application
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy pom.xml first (for dependency caching)
# Docker caches each layer - if pom.xml hasn't changed,
# it won't re-download dependencies (faster builds!)
COPY pom.xml .
COPY checkstyle.xml .

# Download dependencies (cached if pom.xml unchanged)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests - they ran earlier in CI)
# This creates a JAR file in target/ directory
RUN mvn package -DskipTests -B

#############################################
# STAGE 2: RUN
#############################################
# Use a minimal JRE image for running (smaller, more secure)
FROM eclipse-temurin:17-jre-alpine

# Add labels for image metadata
# These help identify the image and its contents
LABEL maintainer="student@example.com"
LABEL description="DevOps CI/CD Demo Application"
LABEL version="1.0.0"

# Create a non-root user for security
# Running as root inside containers is a security risk!
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port 8080 (documentation - doesn't actually open the port)
EXPOSE 8080

# Health check - Docker will verify the app is running
# This is used by orchestrators like Kubernetes
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Run the application
# ENTRYPOINT defines the command to run when container starts
ENTRYPOINT ["java", "-jar", "app.jar"]

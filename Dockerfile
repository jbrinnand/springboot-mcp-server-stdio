
# Build stage
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /build

# Copy the Maven configuration files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src src

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create a non-root user
RUN useradd -r -u 1001 -g root mcp-user

# Copy the jar from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Set ownership of the application files
RUN chown mcp-user:root /app \
    && chown mcp-user:root app.jar

# Use non-root user
USER mcp-user

# Set default environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m" \
    SPRING_PROFILES_ACTIVE=docker \
    SPRING_AI_MCP_SERVER_TRANSPORT=stdio \
    SPRING_AI_MCP_SERVER_STDIO_ENABLED=true

# Command to run the jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
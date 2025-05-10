# Use official OpenJDK 21 slim base image
FROM openjdk:21-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the Gradle-built JAR file from the host to the container's working directory
COPY build/libs/payment-0.0.1-SNAPSHOT.jar /app/build/libs/app.jar

# Copy the .env file from the host to the container's working directory
COPY .env /app/.env

# Optional: Define JVM options if needed
ENV JAVA_OPTS=""

# Expose port 8080 so the container can accept requests
EXPOSE 8080

# Run the Spring Boot application without $JAVA_OPTS (for debugging)
ENTRYPOINT ["java", "-jar", "/app/build/libs/app.jar"]

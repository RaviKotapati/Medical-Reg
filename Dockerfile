# Use official OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file into the image
COPY target/medical-register.jar app.jar

# Expose the application port (change if needed)
EXPOSE 8001

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

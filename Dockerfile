FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/medical-register-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "app.jar"]
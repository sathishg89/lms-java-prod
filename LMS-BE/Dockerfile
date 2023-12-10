# Use the official OpenJDK 17 base image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Build the application using Maven wrapper (if required)
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080


# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "target/LMS-0.0.1-SNAPSHOT.jar"]


# Command to run the Spring Boot application when the container starts
# CMD ["java", "-Dspring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:LMS}?allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true&useSSL=false", "-Dspring.datasource.username=${DB_USERNAME:root}", "-Dspring.datasource.password=${DB_PASSWORD:password}", "-jar", "target/LMS-0.0.1-SNAPSHOT.jar"]

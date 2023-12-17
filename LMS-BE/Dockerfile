# Use the official OpenJDK 17 image as the base image
FROM openjdk:17

# Maintainer information
LABEL maintainer="murali muralialakuntla3@gmail.com"

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

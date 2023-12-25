# Use the official OpenJDK 17 image as the base image
FROM openjdk:17

# Maintainer information
LABEL maintainer="praneeth"

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project to the container's working directory
COPY . .

# Run Maven clean package to build the application
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Define the entry point for the container

ENTRYPOINT [ "java","-jar","target/lmsbe.jar" ]





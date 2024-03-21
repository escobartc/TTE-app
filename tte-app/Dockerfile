# Use a base image with Java 17 installed
FROM openjdk:17-jdk-slim

# Set environment variables
ENV URL=$DB_URL
ENV PASSWORD=$DB_PASSWORD
ENV USERNAME=$DB_USERNAME
ENV PORT=$DB_PORT
ENV SECRET_KEY=$SECRET_KEY

# Set the working directory inside the container
WORKDIR /app

# Expose the port that your Spring Boot application runs on
EXPOSE 8080

# Copy the JAR file from the Docker directory into the container
COPY target/tte-app-0.0.1-SNAPSHOT.jar /app/app.jar

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]

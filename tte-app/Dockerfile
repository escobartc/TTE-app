# Use a base image with Java 17 installed
FROM openjdk:17-jdk-slim

# Set environment variables
ENV URL=$DB_URL
ENV PASSWORD=$DB_PASSWORD
ENV USERNAME=$DB_USERNAME
ENV PORT=$DB_PORT
ENV SECRET_KEY=$SECRET_KEY
ENV PATH_STORE=$PATH_STORE

# Set the working directory inside the container
WORKDIR /app

# Expose the port that your Spring Boot application runs on
EXPOSE 8080

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "/app.jar"]

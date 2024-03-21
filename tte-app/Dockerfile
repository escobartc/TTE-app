# Use a base image with Java 17 installed
FROM openjdk:17-jdk-slim

# Set environment variables
ENV URL=$DB_URL
ENV PASSWORD=$DB_PASSWORD
ENV USERNAME=$DB_USERNAME
ENV PORT=$DB_PORT
ENV SECRET_KEY=$SECRET_KEY
ENV PATH_STORE=$PATH_STORE

# Print the contents of the target directory
RUN pwd
RUN ls -l /home/runner/work/TTE-app/TTE-app/tte-app/target
RUN ls -l ./..
RUN pwd
RUN ls -l ./../..



# Copy the JAR file to the app directory
COPY ./target/tte-app-0.0.1-SNAPSHOT.jar /app/app.jar

# Set the working directory inside the container
WORKDIR /app

# Expose the port that your Spring Boot application runs on
EXPOSE 8080

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "/app/app.jar"]

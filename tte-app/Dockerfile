# Use a base image with Java 17 installed
FROM openjdk:17-jdk-slim

# Set environment variables
ENV URL=$DB_URL
ENV PASSWORD=$DB_PASSWORD
ENV USERNAME=$DB_USERNAME
ENV PORT=$DB_PORT
ENV SECRET_KEY=$SECRET_KEY

WORKDIR /app

COPY target/tte-app-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
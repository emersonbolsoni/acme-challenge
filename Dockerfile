# Using maven to build the application
FROM maven:3.9.8-eclipse-temurin-21 as build

# Set the working directory for build
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package

# Use java to the final image
FROM openjdk:21-jdk-slim

# Add a Volume
VOLUME /tmp

#App Jar File
ARG JAR_FILE=target/challenge-0.0.1-SNAPSHOT.jar

#Copy jar to build stage
COPY --from=build /app/${JAR_FILE} app.jar

# Run Jar File
ENTRYPOINT ["java", "-jar", "/app.jar"]
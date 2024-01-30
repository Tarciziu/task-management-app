# Stage 1: Build the application
FROM gradle:jdk21 as builder

# download wait library
COPY --from=ghcr.io/ufoscout/docker-compose-wait:2.12.0 /wait /wait

# Set the working directory
WORKDIR /app

# Copy only the necessary files for dependency resolution
COPY *.gradle /app/

# Copy the source code
COPY src /app/src

# Download dependencies and build the application
RUN gradle clean build --no-daemon

# Stage 2: Create a smaller image for running the application
FROM openjdk:21-slim as runner

# Set the working directory
WORKDIR /app

# Copy only the built JAR file and wait library from the builder stage
COPY --from=builder /wait /wait
COPY --from=builder /app/build/libs/*SNAPSHOT.jar /app/ROOT.jar

# Expose the port the application will run on
EXPOSE 8080

# Run the application
CMD /wait && java -jar /app/ROOT.jar
# Loads a Slim Alpine JDK11 Base Image for smallest possible image size
FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR app
# Run the application with non-Root Privileges
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
# Copy over .jar-file
ARG JAR_FILE_NAME=auth
COPY ./target/${JAR_FILE_NAME}.jar .
# Defines the entrypoint for the container
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

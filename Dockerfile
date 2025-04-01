FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/scala-2.13/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]


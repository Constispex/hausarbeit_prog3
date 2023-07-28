FROM amazoncorretto:17-alpine-jdk

COPY target/Server-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "/app.jar"]

EXPOSE 8080

# Path: docker-compose.yml
version: '3.8'


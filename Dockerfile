FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY Server/build/libs/Server-1.0-SNAPSHOT.jar .
COPY Server/src/main/resources/ /app/resources/

COPY Common/build/libs/Common-1.0-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "Server-1.0-SNAPSHOT.jar"]

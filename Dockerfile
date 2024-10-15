FROM openjdk:17-jdk-alpine
ENV SERVER_PORT=8089
EXPOSE 8081
COPY target/tpAchatProject-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java" , "-jar" , "/app.jar"]
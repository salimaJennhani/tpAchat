FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/tpAchatProject-0.0.1-SNAPSHOT 5DS3-G4-TPACHAT.jar
ENTRYPOINT ["java" , "-jar" , "/app.jar"]

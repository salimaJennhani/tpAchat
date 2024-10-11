FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/tpAchatProject-0.0.1-SNAPSHOT app.jar
ENTRYPOINT ["java" , "-jar" , "/app.jar"]

FROM openjdk:17-jdk-alpine
ENV SERVER_PORT=8089
EXPOSE 8081
COPY target/tpAchatBuild.jar /5ds3-g4-tpachat.jar
ENTRYPOINT ["java" , "-jar" , "/5ds3-g4-tpachat.jar"]



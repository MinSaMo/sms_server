FROM openjdk:17-jdk
LABEL authors="jaemin"

ARG JAR_FILE=build/libs/sms.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]
FROM maven:3.8.6-openjdk-18 AS build
COPY src /home/events/src
COPY pom.xml /home/events
RUN mvn -f /home/events/pom.xml clean package


FROM openjdk:13-jdk-alpine
MAINTAINER cofran.com
COPY target/transaction-logger-0.0.1.jar transaction-logger-0.0.1.jar
ENTRYPOINT ["java","-jar","/transaction-logger-0.0.1.jar"]
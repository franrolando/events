FROM maven:3.8.6-openjdk-18 AS build

ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN mvn package

FROM openjdk:13-jdk-alpine
MAINTAINER cofran.com
COPY --from=build /usr/app/target/transaction-logger-1.0.0.jar transaction-logger-1.0.0.jar
ENTRYPOINT ["java","-jar","/transaction-logger-1.0.0.jar"]
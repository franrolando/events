FROM maven:3.8.6-openjdk-18 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip

FROM openjdk:13-jdk-alpine
MAINTAINER cofran.com
COPY --from=build /home/app/target/transaction-logger-1.0.0.jar /usr/local/lib/transaction-logger-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/transaction-logger-1.0.0.jar"]
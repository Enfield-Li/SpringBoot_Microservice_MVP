FROM openjdk:8-jdk-alpine

EXPOSE 8081

COPY /target/fraud-0.0.1-SNAPSHOT.jar fraud.jar

ENTRYPOINT ["java","-jar","fraud.jar"]
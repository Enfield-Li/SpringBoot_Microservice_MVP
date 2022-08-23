FROM openjdk:8-jdk-alpine

EXPOSE 8080

COPY /target/customer-0.0.1-SNAPSHOT.jar customer.jar

ENTRYPOINT ["java","-jar","customer.jar"]
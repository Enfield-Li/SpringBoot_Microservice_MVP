FROM openjdk:8-jdk-alpine

EXPOSE 8761

COPY /target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar

ENTRYPOINT ["java","-jar","eureka-server.jar"]
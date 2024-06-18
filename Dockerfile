FROM openjdk:21-jdk-oraclelinux8
ARG JAR_FILE=target/*.jar
COPY ./target/ECommerceStore-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
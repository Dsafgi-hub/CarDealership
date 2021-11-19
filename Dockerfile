FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080
ARG JAR_FILE=/target/carDealership-0.0.1-SNAPSHOT.jar
COPY /target/carDealership-0.0.1-SNAPSHOT.jar car-dealership.jar
ENTRYPOINT ["java","-jar","car-dealership.jar"]
# JVM
FROM adoptopenjdk/openjdk11:alpine-jre

# PORT
EXPOSE 8099

# Refer to Maven build -> finalName
ARG JAR_FILE=target/carDealership-0.0.1-SNAPSHOT.jar

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
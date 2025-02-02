FROM openjdk:11.0.7-jre-slim
COPY build/libs/ms-booking-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/ms-booking-0.0.1-SNAPSHOT.jar"]

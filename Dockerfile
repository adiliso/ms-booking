FROM eclipse-temurin:21-jre-alpine
COPY build/libs/ms-booking-0.0.1-SNAPSHOT.jar ms-booking-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/ms-booking-0.0.1-SNAPSHOT.jar"]

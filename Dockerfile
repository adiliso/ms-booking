FROM eclipse-temurin:21-jre-alpine
COPY build/libs/ms-booking-0.0.1.jar ms-booking-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/ms-booking-0.0.1.jar"]

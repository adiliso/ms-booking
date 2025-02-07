# Flight Booking System #

<pre>
Total test count: 54
Used:
    - Java 21, Gradle
    - Spring Boot -> Web, AOP, Data JPA, Test, Validation
    - Postgres db
    - Liquibase as db migration tool
    - DockerFile, Docker Compose
    - Project Lombok
    - MapStruct
    - Spring Fox (Swagger)
    - Slf4j
    - SonarQube
    - Test admin: id: 1; username: `admin@gmail.com`; password: `Admin@2005`; status: `ACTIVE`
    - Test user #1: 2; username: `anar@gmail.com`; status: `ACTIVE`
    - Test user #2: 3; username: `adil@gmail.com`; status: `ACTIVE`
    - Test user #3: 4; username: `ayten@gmail.com`; status: `ACTIVE`
    - Test user #4: 5; username: `joshgun@gmail.com`; status: `ACTIVE`
</pre>

## Getting Started ##

Open terminal and:

- `git clone https://github.com/adiliso/ms-booking.git` - clone the project
- `cd ./ms-booking` - change directory into the project
- `chmod +x start.sh` - add permission to `start.sh` file to be able to run command inside it
- `chmod +x stop.sh` - add permission to `stop.sh` file to be able to run command inside it
- `chmod +x gradlew`  - add permission to `gradlew` file to be able to clean and build
- `./start.sh` - start app
- `./stop.sh`  - stop app


## Swagger Docs: ##

```
http://localhost:8080/swagger-ui/index.html#
```

### Ms-Booking Rest API ###

![ms-booking](./_diagram/booking.png)
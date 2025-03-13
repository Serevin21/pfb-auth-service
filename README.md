# Auth Service Template

This is a basic example of a Spring Boot application that allows:

- User registration
- Token exchange (sign-in) using credentials or a Google OAuth2 access token

Feel free to use this project for your needs.

## Requirements

- Java 21
- Gradle
- PostgreSQL

## Configuration

Open `application.yml` and set the following properties:

```yaml
app:
  token:
    secret: 

spring:
  datasource:
    url: 
    username: 
    password: 
  liquibase:
    change-log:
    
```

## Build

To build the project, run:

```sh
gradle build
```

## Run

To start the application, use:

```sh
gradle bootRun
```



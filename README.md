# Conversations App

## Requirements
This demo is build with with Maven 3.6.x and Java 14.

## Usage
Just start the application with the Spring Boot maven plugin (`mvn spring-boot:run`). The application is
running at [http://localhost:8080](http://localhost:8080).

## Backend

This app uses JWT auth with Bearer token

### Generating password hashes for new users

I'm using [bcrypt](https://en.wikipedia.org/wiki/Bcrypt) to encode passwords. Your can generate your hashes with this simple 
tool: [Bcrypt Generator](https://www.bcrypt-generator.com)

### Using another database

If you want to connect 
to another database you have to specify the connection in the *application.yml* in the resource directory. Here is an example for a MySQL DB:

```
spring:
  jpa:
    hibernate:
      # possible values: validate | update | create | create-drop
      ddl-auto: create-drop
  datasource:
    url: jdbc:mysql://localhost/myDatabase
    username: myUser
    password: myPassword
    driver-class-name: com.mysql.jdbc.Driver
```

You can find a reference of all application properties [here](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).

## Docker
This project has a docker image. You can find it at [https://hub.docker.com/r/hubae/jwt-spring-security-demo/](https://hub.docker.com/r/hubae/jwt-spring-security-demo/).

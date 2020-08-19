# Conversations App

## Requirements
This demo is build with with Maven 3.6.x and Java 14.

## Usage
Just start the application with the Spring Boot maven plugin (`mvn spring-boot:run`). The application is
running at [http://localhost:8080](http://localhost:8080).

You can use the **H2-Console** for exploring the database under [http://localhost:8080/h2-console](http://localhost:8080/h2-console):

![Screenshot from h2-console login](etc/screenshot-h2-console-login.png?raw=true "Screenshot H2-Console login")

## Backend
There are three user accounts present to demonstrate the different levels of access to the endpoints in
the API and the different authorization exceptions:
```
Admin - admin:admin
User - user:password
Disabled - disabled:password (this user is deactivated)
```

There are four endpoints that are reasonable for the demo:
```
/api/authenticate - authentication endpoint with unrestricted access
/api/user - returns detail information for an authenticated user (a valid JWT token must be present in the request header)
/api/persons - an example endpoint that is restricted to authorized users with the role 'ROLE_USER' (a valid JWT token must be present in the request header)
/api/hiddenmessage - an example endpoint that is restricted to authorized users with the role 'ROLE_ADMIN' (a valid JWT token must be present in the request header)
```

### Generating password hashes for new users

I'm using [bcrypt](https://en.wikipedia.org/wiki/Bcrypt) to encode passwords. Your can generate your hashes with this simple 
tool: [Bcrypt Generator](https://www.bcrypt-generator.com)

### Using another database

Actually this demo is using an embedded H2 database that is automatically configured by Spring Boot. If you want to connect 
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

*Hint: For other databases like MySQL sequences don't work for ID generation. So you have to change the GenerationType in the entity beans to 'AUTO' or 'IDENTITY'.*

You can find a reference of all application properties [here](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).

## Docker
This project has a docker image. You can find it at [https://hub.docker.com/r/hubae/jwt-spring-security-demo/](https://hub.docker.com/r/hubae/jwt-spring-security-demo/).

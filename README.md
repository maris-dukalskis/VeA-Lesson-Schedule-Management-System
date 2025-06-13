# VeA-Lesson-Schedule-Management-System Setup Guide

## Backend application.properties
```
spring.application.name=Backend
#DB launch mode example- create (clear all data on start), validate (only validate data, used for production environment)
spring.jpa.hibernate.ddl-auto=validate
#DB URL example- jdbc:mysql://${MYSQL_HOST:localhost}:3306/lsms?createDatabaseIfNotExist=true
spring.datasource.url=
#DB username
spring.datasource.username=
#DB password
spring.datasource.password=
#DB driver type
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

#OAuth2 URL
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://www.googleapis.com/oauth2/v1/certs
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://accounts.google.com

#OAuth2 keys (https://developers.google.com/identity/protocols/oauth2)
spring.security.oauth2.client.registration.google.client-id=
spring.security.oauth2.client.registration.google.client-secret=

#Base64-encoded string for JWT generation
jwt.secret=
```

## Frontend .env file
```
#Backend URL
REACT_APP_BACKEND_URL=http://localhost:8080

## OAuth2 key, same as backend
REACT_APP_GOOGLE_CLIENT_ID=
```

## Backend start
```
\Backend> mvn spring-boot:run
```

## Frontend start
```
\frontend> npm start
```
## Frontend start production
```
\frontend> npm run build (creates build file)
\frontend> npm install -g serve (only once)
\frontend> server -s build (runs build file)
```
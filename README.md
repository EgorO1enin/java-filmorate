# Filmorate

Filmorate is an educational project that represents a backend for a service that, based on user preferences and the ratings of their friends, will suggest a top of films to watch.

## Project Highlights:

*   Implemented film and user models, application memory storage, annotations, validation, custom validation, logging, and validation tests. All logic is in the controllers.
*   Improved architecture by moving storage and business logic into separate layers. Implemented dependency injection. Implemented models for likes and adding friends. Worked with Optionals, path variables, and request parameters. Created an ExceptionHandler.
*   Implemented genre and rating models, database storage, DAO and mappers, CRUD operations using JdbcTemplate, and integration testing.

**Stack: Java 21, Spring Boot, Maven, REST, Lombok, Postman, H2, JdbcTemplate, JUnit5**

## How to Run the Application

1.  **Prerequisites:**
    *   Java 21 or higher
    *   Maven
2.  **Build the Application:**

    ```
    mvn clean install
    ```
3.  **Run the Application:**

    *   You can run the application using the Spring Boot Maven plugin:

    ```
    mvn spring-boot:run
    ```
    *   Alternatively, you can run the packaged JAR file:

    ```
    java -jar target/filmorate-0.0.1-SNAPSHOT.jar
    ```

    *Note:  The JAR file name `filmorate-0.0.1-SNAPSHOT.jar` may vary depending on your project's version.  Check your `target` directory for the correct name.*

## Project Structure

*   `src/main/java`: Contains the source code for the application.
*   `src/test/java`: Contains the tests for the application.
*   `pom.xml`: Maven project file.

## Dependencies

Here is a summary of the key dependencies used in this project (as defined in `pom.xml`):

*   **org.springframework.boot:spring-boot-starter-web:** For building web applications with Spring MVC.
*   **io.swagger.core.v3:swagger-annotations:** For adding OpenAPI annotations to your code.
*   **org.springdoc:springdoc-openapi-starter-webmvc-ui:** For auto-generating API documentation using Swagger UI.
*   **org.slf4j:slf4j-api:** Logging abstraction.
*   **org.hibernate.validator:hibernate-validator:** For implementing validation logic.
*   **org.projectlombok:lombok:** For reducing boilerplate code.
*   **org.springframework.boot:spring-boot-starter-test:** For writing unit and integration tests.
*   **org.zalando:logbook-spring-boot-starter:** For logging HTTP requests and responses.
*   **org.springframework.boot:spring-boot-starter-data-jpa:** For using Spring Data JPA with database access.
*   **com.h2database:h2:** An in-memory database for development and testing.

## Useful links

*   [Official Apache Maven documentation](http://maven.apache.org/POM/4.0.0)
*   [Spring Boot Maven Plugin Reference](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/)


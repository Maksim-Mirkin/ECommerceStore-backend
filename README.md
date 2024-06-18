# E-Commerce backend

This REST API Java Spring application powers an e-commerce store, offering various electronics including laptops, cell
phones, TVs, and headphones.
The application supports comprehensive CRUD operations for product management, user and order handling, and allows
customers to rate products.
It is equipped with Spring Security ensuring robust authentication and authorization, and uses Spring Data JPA for
efficient database operations.
The project leverages Lombok to reduce boilerplate code, enhancing readability and maintainability.
API documentation is neatly handled with Swagger, providing a clear, interactive interface for developers.
Key features include advanced sorting and filtering capabilities that enhance user experience by making product searches
more intuitive and efficient.
Designed for scalability, this server-side application can integrate seamlessly with various front-end technologies.

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Live Deployment](#live-deployment)
- [Design Patterns](#design-patterns)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Dependencies](#dependencies)
- [Setup](#setup)
- [API Documentation](#api-documentation)
- [See also](#see-also)
- [License](#license)

## Live Deployment
[Live Documentation](https://ecommerce-store-e805668c131b.herokuapp.com/swagger-ui/index.html)

**Base URL:** https://ecommerce-store-e805668c131b.herokuapp.com

**Hosting Providers:** Heroku

#### Deployment Details

1. **Database**: MySQL
2. **Build Tools**: Maven

## Design Patterns

In E-Commerce backend project, various design patterns were implemented to enhance modularity, maintainability, and
performance:

### 3 Layer Architecture:

The project adheres to the Model-View-Controller (MVC) architectural pattern, organizing the application into three
layers: Repository, Service, and Controller. This segregation fosters a clear division of responsibilities, enhancing
the modularity and maintainability of the codebase. Each layer is designed to handle specific aspects of the
application: the Repository layer interacts with the database, the Service layer contains business logic, and the
Controller layer manages the HTTP request and response.

### Singleton

The Singleton pattern ensures that a class has only one instance, and provides a global point of access to it. In this
project, Singleton is utilized in managing configurations and pooled resources, such as database connections. This
pattern is crucial in maintaining consistency across operations and managing resources efficiently.

### Dependency Injection(DI):

Dependency Injection is employed extensively within the framework to manage class dependencies. By injecting
dependencies at runtime rather than at compile time, the application becomes more modular and easier to test. Spring's
Inversion of Control (IoC) container facilitates this pattern, allowing components to be developed and tested in
isolation.

### Data Transfer Objects(DTO):

DTOs are used to encapsulate data and send it from the server to the client, reducing the number of method calls and
simplifying the data exchanged. They help in tailoring data sent over the network to the client's needs, improving
performance and security by segregating the raw database entities from the client-facing data models.

### Builder:

The Builder pattern is utilized to construct complex objects step by step. It is particularly useful in this project for
creating product or order objects that may require various optional and required parameters. Using the Builder pattern
simplifies object creation and enhances code readability and maintainability.

## Technologies

- **Java 21**: Modern version of Java ensuring high performance and security.
- **Spring Boot 3.2.2**: Provides a powerful suite of default configurations and utilities to simplify bootstrapping and
  developing new Spring applications.
- **Spring Data JPA**: Streamlines database operations with robust ORM support.
- **Spring Boot Starter Web**: Aids in creating web applications and RESTful services with Spring MVC.
- **Spring Boot Starter Validation**: Adds automatic model validation using Java Bean Validation API.
- **Spring Boot Starter Security and OAuth2 Resource Server**: Secures applications with standardized OAuth2 protocols.
- **Spring Boot Devtools**: Enhances development efficiency with features like automatic restart and live reloading.
- **Spring Boot Configuration Processor**: Manages application configurations dynamically.
- **MySQL Connector-J**: Provides JDBC connection to MySQL databases.
- **Lombok**: Simplifies code with automatic generation of getters, setters, constructors, and more.
- **ModelMapper**: Facilitates object mapping to easily convert between model objects.
- **Springdoc OpenAPI UI**: Automatically generates and serves interactive API documentation using Swagger UI.
- **Auth0 MVC Commons**: Offers integration solutions for authentication and authorization using Auth0.

## Project Structure

<details closed>

    ├── src                                            # Source files
    │   ├── main
    │   │   ├── java
    │   │   │   ├── config
    │   │   │   ├── controller
    │   │   │   ├── dto
    │   │   │   ├── entity
    │   │   │   ├── enums
    │   │   │   ├── error
    │   │   │   ├── repository
    │   │   │   ├── security
    │   │   │   ├── service
    │   │   │   ├── validator
    │   │   │   ├── ECommerceStoreApplication.java
    │   │   ├── resources
    │   │   │   ├── certs                              # RSA keys
    │   │   │   ├── META-INF                           # Swagger
    │   │   │   ├── rest                               # http files
    │   │   │   ├── application.properties
    │   │   │   ├── http-client.private.env.json       # jwt for authentication
    ├── pom.xml                                        # Tools and utilities
    ├── LICENSE
    └── README.md

</details>

## Dependencies

This section outlines the key libraries and frameworks upon which the E-Commerce Store application depends. Each
dependency is crucial for the application's operation, enhancing its functionality and performance.

| Dependency                                     | Version | Description                                                                                                   |
|------------------------------------------------|---------|---------------------------------------------------------------------------------------------------------------|
| **Spring Boot Starter Data JPA**               | 3.2.2   | Facilitates database operations and ORM capabilities using JPA to simplify data access layers.                |
| **Spring Boot Starter Validation**             | 3.2.2   | Provides support for validation using the Java Bean Validation API.                                           |
| **Spring Boot Starter Web**                    | 3.2.2   | Enables building web applications and RESTful services using Spring MVC.                                      |
| **Spring Boot Devtools**                       | 3.2.2   | Supports automatic application restarts and live reloading during development.                                |
| **MySQL Connector-J**                          |         | Connects the application to MySQL databases, ensuring JDBC support for database operations.                   |
| **Spring Boot Configuration Processor**        |         | Enhances configuration management within Spring Boot applications.                                            |
| **Lombok**                                     |         | Reduces boilerplate code with annotations for getters, setters, constructors, and more.                       |
| **ModelMapper**                                | 3.2.0   | Simplifies object mapping, essential for transforming data between API DTOs and domain models.                |
| **Spring Boot Starter Test**                   | 3.2.2   | Bundles essential testing tools like JUnit and Mockito for comprehensive unit and integration tests.          |
| **Spring Boot Starter OAuth2 Resource Server** | 3.2.2   | Integrates OAuth2 for authentication, securing the application via tokens.                                    |
| **Springdoc OpenAPI Starter Webmvc UI**        | 2.3.0   | Automatically generates OpenAPI documentation for REST APIs, providing a Swagger UI.                          |
| **MVC Auth Commons**                           | 1.2.0   | Offers tools for integrating Auth0, facilitating authentication and authorization in Spring MVC applications. |

Each component is selected to ensure robust, scalable, and secure operations, aligned with modern software development
best practices.

## Setup

Ensure that you have Java 21 and Maven installed on your machine.

1. Open a terminal or command prompt.

2. Navigate to the directory where you want to clone the repository:

```bash
cd path/to/your/directory
```

3. Clone the repository using the following command:

```bash
git clone https://github.com/Maksim-Mirkin/ECommerceStore-backend.git
```

4. Proceed to the project directory by entering:

```bash
cd ECommerceStore-backend
```

5. Build the project with:

```bash
mvn clean install
```

6. Run the project with:

```bash
mvn spring-boot:run
```

The application will be accessible at http://localhost:8080.

## API Documentation

All information about the API, including endpoints and schemas, is available
here: http://localhost:8080/swagger-ui/index.html

## See also

Check out my frontend project that interfaces with this
application: [E-Commerce Store](https://github.com/Maksim-Mirkin/ECommerceStore-Frontend)

## License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE](LICENSE) file for details.

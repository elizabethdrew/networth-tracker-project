## Introduction

The aim of the Networth Tracker project is to create a microservice application designed to enable users to monitor and manage their financial net worth effectively. 

This application would assist users in tracking the balances of their asset and liability accounts, providing a comprehensive overview of their financial health. 

Additionally, it will offer features to track ISA contributions and calculate taxable savings interest based on personal tax rates.

## Project Overview

The architecture of the Networth Tracker follows a microservices design, segmented into function services like User and Account Services, alongside core services including Config Server and Gateway Server. Monitoring and observability services have also been added. 

Each service plays a vital role in ensuring the application's functionality and resilience.

### Services

**Function Services**
- **Account Service**: The Account Service is central to the Networth Tracker, enabling users to manage their financial accounts. Key functionalities include:
    - **Account Registration**: Allows users to create new accounts, providing a unique identifier for each account.
    - **Account Retrieval**: Users can retrieve all accounts associated with them, or get details of a specific account by its ID.
    - **Account Update**: Offers the functionality to update account details.
    - **Soft Deletion of Account**: Enables users to archive (soft delete) an account, thereby marking it as inactive.
    - **Balance Management**: Includes adding new balances to accounts, retrieving balance history, and accessing specific balance entries.
- **User Service**: The User Service in the Networth Tracker application plays a pivotal role in managing user-related functionalities. Key features include:
    - **User Authentication (Login/Logout)**:
      - **Login**: Users can authenticate themselves by providing their username and password. Upon successful authentication, an access token is returned for subsequent API calls. This process involves checking username existence, validating passwords, and obtaining access tokens from Keycloak.
      - **Logout**: Enables users to log out, effectively invalidating their current access token and ending their session. The logout process includes revoking the access token and logging out the user from Keycloak.
    - **User Registration and Management**:
      - **User Registration**: New users can create an account by providing necessary details. The service then registers the user with Keycloak and returns a unique identifier for the user.
      - **Get User by Keycloak ID**: Retrieves user details based on the Keycloak ID.
      - **Update User**: Allows updating user information, such as email or other personal details. This also involves updating user details in Keycloak.
      - **Delete User**: Facilitates the deletion of a user account, removing their details from the application and Keycloak.
    - **Keycloak Integration**:
      - The service integrates closely with Keycloak for identity and access management. It handles tasks like obtaining admin and user access tokens, creating and logging out users in Keycloak, updating Keycloak user details, and revoking access tokens.
    - **Error Handling and Logging**:
      - The service is equipped with comprehensive error handling and logging mechanisms. It gracefully handles exceptions related to authentication, user management, and communication with Keycloak.
- **ISA Service**: The ISA Service in the Networth Tracker application is dedicated to managing various aspects of Individual Savings Accounts (ISAs). Its core functionalities include:
    - **ISA Account Management**:
      - **Add ISA Account**: This function allows for the addition of new ISA accounts to the system. It handles different types of ISA accounts, such as Cash ISA, Stocks & Shares ISA, Innovative Finance ISA, and Lifetime ISA. For each account type, it updates the ISA tracker with the relevant account ID.
    - **ISA Balance Management**:
      - **Add ISA Balance**: Responsible for updating the balance of ISA accounts. It accommodates the balance changes for different types of ISA accounts by calculating deposits, withdrawals, and the current balance. The service ensures that the total deposits for a tax year do not exceed the maximum ISA allowance, throwing exceptions if limits are breached.
    - **Tax Year and Allowance Handling**: The service operates with a focus on the current tax year, using predefined values for maximum ISA and Lifetime ISA (LISA) allowances. It ensures that all transactions and balance updates are consistent with the limits set for the current tax year.
    - **Calculation of Total ISA Balance**: It calculates the total ISA balance across all account types, considering the current balances of Cash ISA, Stocks & Shares ISA, Innovative Finance ISA, and Lifetime ISA.
    - **Error Handling and Logging**: The service is equipped with robust error handling for scenarios like exceeding ISA allowances and attempting to add multiple accounts of the same type in a tax year. It also maintains detailed logs for all operations, ensuring traceability and aiding in debugging.
    - **Integration with Kafka**: The ISA Service integrates with Kafka for handling balance data, indicating a robust, event-driven architecture that ensures real-time data processing and updates.
- **Common Library**: Holds common DTO's and Enums for use across services.

**Core Services**
- **Config Server**: Centralizes configuration properties for other services.
- **Gateway Server**: Acts as the entry point to the application, routing requests to appropriate services.
- **Kafka**: Handles messaging and data streaming between services.
- **Keycloak**: Provides identity and access management, ensuring secure user authentication.

**Monitoring & Observability Services**
- **Grafana**: Offers analytics and interactive visualization for monitoring the application.
- **Loki**: Log aggregation system integrated with Grafana.
- **Tempo**: Traces and monitors application performance.
- **Prometheus**: Monitors the health and performance of services.

**Database Services**
- **MySQL**: Hosts the application's relational database.

**Running Services**
- **Docker**: Manages containerized instances of services.
- **Kubernetes**: Orchestration platform for automating deployment, scaling, and management of containerized applications.
- **Helm & Helmfile**: Tools for managing and deploying applications on Kubernetes.

## Next Steps:

### Account Service

- **Enhanced Testing**: Develop comprehensive tests for account and balance management functionalities to ensure robustness and reliability.
- **Editable Balance Entries**: Consider implementing editable balance entries with mechanisms for tracking changes and rollback capabilities.
- **Liquibase**: Needs to be added once entity class is firmly set in stone

### User Service

- **Continuous Testing Improvement**: Identify and implement enhancements in testing strategies to cover edge cases and improve code reliability.

### ISA Service

- **Implementing Comprehensive Testing**: Establish a thorough testing framework to validate all aspects of the ISA service.
- **Change History and Rollback**: Explore the feasibility of maintaining a log of updates for change history tracking and potential rollback capabilities.
- **User Communication Strategy**: Develop methods for effectively communicating errors, warnings, and informational messages to users. This includes implementing retrieval functions and a dedicated message service.
- **Liquibase**: Needs to be added once entity class is firmly set in stone

### Overall Application Development

- **Net Worth Calculation**: Implement a feature to calculate and display the user's total net worth by aggregating balances across different accounts.
- **Handling Historical ISA Accounts**: Develop strategies for updating historical ISA accounts that no longer receive deposits but require updates for other factors.
- **Transfers Between Accounts**: Design a system to manage transfers between accounts, especially within ISA accounts, ensuring compliance with financial regulations.
- **Percentage Ownership for Net Worth Calculation**: Incorporate percentage ownership considerations into net worth calculations to accurately reflect shared account ownership.
- **Account Value and Balance Information**: Evaluate the benefits of providing users with real-time account value and balance information.
- **Asset and Liability Management**: Establish robust rules for categorizing and managing assets and liabilities within the application.

### Future Considerations

- **User Experience Enhancement**: Focus on improving the user interface and overall user experience.
- **Scalability and Performance**: Assess and enhance the scalability and performance of the application to handle increased user load and data volume.
- **Regulatory Compliance**: Ensure continuous compliance with financial regulations and data privacy laws.

## Learning Outcomes

### Core Frameworks and Libraries

1. **Spring Boot**: Foundation for building stand-alone, production-grade Spring-based applications.
2. **Spring Cloud**: Simplifies the development of distributed systems and microservices with patterns such as service discovery, circuit breakers, and configuration management.
3. **Spring Data JPA**: Provides a more sophisticated integration between Spring Boot and JPA for data handling in SQL databases.
4. **Spring WebFlux**: Enables building reactive applications on the Spring ecosystem.
5. **Spring Cloud Stream**: Framework for building event-driven microservices connected with shared messaging systems.
6. **Spring Cloud Config Client**: Client support for externalized configuration in a distributed system.
7. **Spring Cloud Gateway**: Implements API Gateway patterns for microservices.

### Database and Persistence

1. **MySQL Connector**: JDBC driver for MySQL.
2. **Jakarta Persistence API**: Standardises the persistence layer in Java EE applications.

### Security and Authentication

1. **Keycloak**: Open-source identity and access management for modern applications and services.
2. **Spring Security**: Security framework for authentication and authorization.
3. **OAuth2 OIDC SDK**: Library for OpenID Connect and OAuth 2.0.

### API and Documentation

1. **Swagger Annotations**: Annotations to describe and document RESTful APIs.

### Messaging and Streaming

1. **Spring Cloud Stream Binder Kafka**: Integration of Spring Cloud Stream with Apache Kafka.
2. **Apache Kafka**: Messaging system for building real-time data pipelines and streaming apps.

### Logging and Monitoring

1. **SLF4J**: Simple Logging Facade for Java for abstracting various logging frameworks.
2. **Micrometer**: Application metrics facade for integrating with monitoring systems like Prometheus.
3. **OpenTelemetry JavaAgent**: Provides automatic instrumentation for monitoring application performance.
4. **Prometheus**: Monitoring system and time series database.
5. **Grafana**: Analytics and monitoring platform.
6. **Loki**: Log aggregation system compatible with Grafana.
7. **Promtail**: Log collector for Loki.

### Infrastructure and Service Discovery

1. **Spring Cloud Netflix Eureka**: Service discovery tool for microservices.
2. **Spring Cloud Kubernetes**: Kubernetes integration with Spring Cloud.
3. **Redis**: In-memory data store used as a database, cache, and message broker.
4. **Docker**: Platform for developing, shipping, and running applications.
5. **Docker Compose**: Tool for defining and running multi-container Docker applications.

### Resilience and Circuit Breakers

1. **Resilience4j**: Fault tolerance library designed for Java8 and functional programming.

### Data Serialization and Web Clients

1. **Jackson Databind**: JSON parser and generator for converting Java objects to/from JSON.

### Testing Tools

1. **JUnit Jupiter**: The new programming model and extension model for JUnit 5.
2. **Mockito**: Mocking framework for unit tests in Java.
3. **Rest Assured**: Java DSL for simplifying testing of REST services.
4. **Testcontainers**: Provides lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container for JUnit tests.
5. **Spring Boot Starter Test**: Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest, and Mockito.

### Build and Deployment

1. **Jib**: Containerization tool for Java applications.
2. **Maven**: Software project management and comprehension tool.

### Additional Tools and Libraries

1. **Lombok**: Library to reduce boilerplate code in Java with annotations.
2. **MapStruct**: Code generator for Java bean mappings.
3. **Passay**: Password security library for enforcing password policies.
4. **Feign**: Declarative web service client for simplifying HTTP API clients.
5. **Liquibase**: Source control for database schema changes.
6. **JWT (JSON Web Token)**: Library for creating and decoding JSON Web Tokens.
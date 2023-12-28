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
- **ISA Service**: Manages ISA contributions and calculates taxable savings interest.
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

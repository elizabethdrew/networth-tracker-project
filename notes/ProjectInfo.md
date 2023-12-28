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

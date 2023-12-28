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
- **User Service**: Handles user account functionality including registration, authentication, and profile management.
- **ISA Service**: Manages ISA contributions and calculates taxable savings interest.

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

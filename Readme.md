# Networth Tracker Application

The Networth Tracker project is a microservice application designed to provide users with a way to keep track of their financial net worth. Users will be able to track the balances of their asset and liability accounts, giving them a top level view of their current financial health. It will also provide a way to track ISA contributions across accounts, as well as taxable savings interest based on their personal tax rate.

---
## Version Information

Current Version: v1.3.0

Please see [Versions.md](/notes/Versions.md) for detailed changelog.

---
## Service Structure
**Funtion Services**
+ User Service - Handles user account functionality

**Core Services**
+ Config Server - Provides centralised configuration properties for other services
+ Gateway Server - Entry point to the application, providing a single point of access to external clients.
+ Discovery Server
  + Eureka Server - Service registration, discovery, and load balancing (For Use With Docker Compose)
  + Kubernetes Discovery Server - Service registration, discovery, and load balancing (For Use With Kubernetes)
+ Keycloak Server - Open-source identity and access management system that provides single sign-on capabilities and security features.

**Monitoring & Observability Services**
+ 
  
---
## Built With

## Built With

The Networth Tracker is powered by a robust stack of technologies, ensuring a scalable, efficient, and secure user experience.

### Frameworks and Libraries
- [Spring Boot](<https://spring.io/projects/spring-boot>): Simplifies the bootstrapping and development of new Spring applications.
- [Spring Webflux](<https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html>): Reactive-stack web framework to build asynchronous non-blocking applications.
- [Spring Security](<https://spring.io/projects/spring-security>): Provides authentication and authorization support.
- [Spring Data JPA](<https://spring.io/projects/spring-data-jpa>): Simplifies data access for Java Persistence API.
- [Hibernate](<https://hibernate.org/orm/documentation/5.4/>): Object-relational mapping tool for Java.
- [Lombok](<https://projectlombok.org/>): Java library that automatically plugs into your editor and build tools, spicing up your java.
- [MapStruct](<https://mapstruct.org/>): Code generator that simplifies the implementation of mappings between Java bean types.
- [OpenAPI](<https://swagger.io/specification/>): Defines a standard, language-agnostic interface to RESTful APIs.

### Database
- [MySQL](<https://www.mysql.com/>): Open-source relational database management system.

### Tools and Utilities
- [Liquibase](<https://www.liquibase.org/>): Source control for your database.
- [TestContainers](<https://www.testcontainers.org/>): Java library that supports JUnit tests with lightweight, throwaway instances of common databases.
- [Actuator](<https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html>): Features to help you monitor and manage your application.
- [Jib](<https://github.com/GoogleContainerTools/jib>): Containerize your Java applications for Docker and Kubernetes without a Dockerfile.

### Configuration and Discovery
- [Spring Config Server](<https://cloud.spring.io/spring-cloud-config/reference/html/>): Centralized external configuration management backed by a version control system.
- [Spring Cloud Eureka Server](<https://cloud.spring.io/spring-cloud-netflix/reference/html/>): REST based service for locating services for the purpose of load balancing and failover of middle-tier servers.
- [Kubernetes Discovery Server](<https://kubernetes.io/docs/concepts/services-networking/service-discovery/>): Provides service discovery within a Kubernetes cluster.

### Monitoring and Logging
- [Grafana](<https://grafana.com/>): Analytics and interactive visualization web application.
- [Loki](<https://grafana.com/oss/loki/>): Horizontally-scalable, highly-available, multi-tenant log aggregation system.
- [Promtail](<https://grafana.com/docs/loki/latest/clients/promtail/>): Logs collector for Loki.
- [Prometheus](<https://prometheus.io/>): Open-source monitoring system with a dimensional data model.

---
## Helpful Dashboards

Dashboards provide a visual interface to monitor the services and infrastructure of your application. Here's how to access the dashboards for the Networth Tracker:

### Service Discovery

- **Eureka Dashboard**: Monitors service registration and discovery.
  - **URL**: [<http://localhost:8761>](<http://localhost:8761>) (when running via Docker Compose)
  - **Port**: 8761

### Configuration Management

- **Spring Config Server**: Centralized configuration management for all services.
  - **Example URL**: [<http://localhost:8071/user-service/default>](<http://localhost:8071/user-service/default>) (replace `user-service` with the actual service name)
  - **Port**: 8071

### Observability and Monitoring

- **Grafana**: Provides beautiful analytics and monitoring. Visualize metrics, logs, and traces from different sources like Prometheus and Loki.
  - **URL**: Typically available at [<http://localhost:3000>](<http://localhost:3000>) after deployment.
  - **Default Port**: 3000

- **Prometheus**: Monitoring system and time series database that works with Grafana for visualizing data.
  - **URL**: [<http://localhost:9090>](<http://localhost:9090>) (if running locally)
  - **Port**: 9090

### Logging

- **Loki**: A horizontally-scalable, highly-available, multi-tenant log aggregation system.
  - **Note**: Loki is used with Grafana for log aggregation. Access logs in the Grafana dashboard by selecting the Loki data source.

### Application Diagnostics

- **Spring Boot Actuator**: Gain insight into the internals of your application, such as health, metrics, info, and more.
  - **Health Endpoint**: Check the health of your application by accessing [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health). This will provide a basic health check.
  - **Metrics Endpoint**: View detailed metrics by visiting [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics). This endpoint exposes various metrics information that can be further queried to dive into specific metrics.

---
## Getting Started

This section guides you through the prerequisites and steps needed to get the Networth Tracker application up and running on your local machine.

### Prerequisites

To run the Networth Tracker, ensure you have the following software installed:

- **Java 17**: [Download Java](<https://www.oracle.com/java/technologies/downloads/>) - Required to run the Java applications.
- **Maven**: [Download Maven](<https://maven.apache.org/download.cgi>) - Used for project dependency management and build automation.
- **Docker**: [Download Docker](<https://www.docker.com/products/docker-desktop>) - Creates isolated containers for each microservice.
- **Docker Compose**: [Install Docker Compose](<https://docs.docker.com/compose/install/>) - Manages multi-container Docker applications.
- **Kubernetes**: Orchestration tool for managing containerized applications. Docker Desktop includes a standalone Kubernetes server. Enable it in the Docker Desktop settings.
- **Helm**: A package manager for Kubernetes. [Install Helm](<https://helm.sh/docs/intro/install/>).
- **Helmfile**: A declarative spec for deploying helm charts. [Install Helmfile](<https://github.com/roboll/helmfile#installation>).

### Installation Guide

#### Java and Maven Installation

##### Using SDKMAN! (Recommended for Unix-like platforms, including Linux and macOS)

SDKMAN! is a version manager for Java and JVM-based applications, which simplifies the installation and management of multiple SDKs on Unix systems.

1. Install SDKMAN! with the following command:
```
curl -s "<https://get.sdkman.io>" | bash
```
2. Initialize SDKMAN!:
```
source "$HOME/.sdkman/bin/sdkman-init.sh"
```
3. Install Java and Maven using SDKMAN!:
```
sdk install java
sdk install maven
```

### Manual Installation (For Windows, or if you prefer not to use SDKMAN!)

- **Java**: Follow the instructions on the [Java download page](https://www.oracle.com/java/technologies/downloads/).
- **Maven**:
  - **Windows/Linux**: Download from the [Maven official website](https://maven.apache.org/download.cgi).
  - **macOS**: If Homebrew is installed, you can use `brew install maven`. If not, install Homebrew first with the command below, then install Maven:
```
/bin/bash -c "$(curl -fsSL <https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh>)"
brew install maven
```

### Docker and Docker Compose Installation

Follow the instructions on the [Docker website](https://docs.docker.com/get-docker/) to install Docker. Docker Compose is included in Docker Desktop for Mac and Windows.

### Kubernetes Installation

For local development, Docker Desktop includes a standalone Kubernetes server that runs on your development machine. Enable it in the Docker Desktop preferences.

### Helm Installation

Helm helps you manage Kubernetes applications — Helm Charts help you define, install, and upgrade even the most complex Kubernetes applications.

**macOS** (using Homebrew):
```
brew install helm
```
### Helmfile Installation

Helmfile is a tool for templating and deploying Helm Chart definitions and can be installed as follows:

**macOS/Linux**:
```
brew install helmfile
```
---
### Running the Application

After installing the prerequisites, you can run the application as follows:

1. Clone the project repository:
```
git clone <https://gitlab.com/wcdio_backendprojects/networth>
```
2. Clone the project configurations repository:
```
git clone <https://gitlab.com/wcdio_backendprojects/networth-tracker-configurations>
```
3. Change into the project directory:
```
cd networth
```

4. Build the project with Maven:
```
mvn clean install
```

5. Build the Docker images for each service (if your project is containerized):
```
mvn compile jib:dockerBuild
```

6. Start the application using Docker Compose:
```
docker-compose up -d
```
Alternatively, deploy the application using Helm in a Kubernetes cluster (make sure Helm and Helmfile are installed):
```
helmfile -f ./helm/helmfile.services.yaml apply
helmfile -f ./helm/helmfile.observe.yaml apply
```
---

## Kubernetes Discovery Server Setup

The Kubernetes Discovery Server is used for service discovery within your Kubernetes (K8s) cluster, facilitating communication and load balancing between your microservices.

To integrate the Discovery Server with your Kubernetes cluster, you'll deploy it using the provided YAML configuration file. Ensure that your `kubectl` context is set to the target cluster where you wish to deploy the services.

1. Apply the Discovery Server configuration with the following command:
```
kubectl apply -f kubernetes/kubernetes-discoveryserver.yml
```
---

## Configuring Keycloak Realm in Kubernetes

Keycloak uses realms to create isolated groups of users and applications. To set up a Keycloak realm in your Kubernetes environment:

1. First, ensure you have the `realm-export.json` file that contains the exported realm data. This file should be located in the `./helm/keycloak/realm/` directory relative to your current working directory.
2. Once Keycloak is running in your Kubernetes cluster, sign into the Keycloak Dashboard. You will then be able to add the new realm by uploading the realm-export.json file.
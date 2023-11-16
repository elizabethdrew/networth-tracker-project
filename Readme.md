# Networth Tracker Application

The Networth Tracker project is a microservice application designed to provide users with a way to keep track of their financial net worth. Users will be able to track the balances of their asset and liability accounts, giving them a top level view of their current financial health. It will also provide a way to track ISA contributions across accounts, as well as taxable savings interest based on their personal tax rate.

## Version Information

Current Version: v1.2.0

Please see [Versions.md](/notes/Versions.md) for detailed changelog.

## Service Structure
+ User Service - Handles user account functionality
+ Config Server - Provides centralised configuration properties for other services
+ Discovery Server
  + Eureka Server - Service registration, discovery, and load balancing (For Use With Docker Compose)
  + Kubernetes Discovery Server - Service registration, discovery, and load balancing (For Use With Kubernetes)
  

## Built With
+ Spring Boot
+ Spring Webflux
+ Spring Security
+ MySQL
+ Spring Data JPA
+ Hibernate
+ Lombok
+ Jakarta
+ MapStruct
+ OpenAPI
+ Liquibase
+ TestContainers
+ Actuator
+ Jib
+ Spring Config Server
+ Spring Cloud Eureka Server

## Helpful Dashboards
+ Eureka Dashboard: Available at port 8761 (When running via Docker Compose)
+ Config Server: Available at port 8071 - example: localhost:8761/user-service/default

## Getting Started
### Prerequisites

You should have Java, Maven, Docker and Kubernetes installed on your machine. Follow the steps below to install them:

- [Java 17](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/products/docker-desktop)
- [Docker Compose](https://docs.docker.com/compose/install/)

### **Installing Java and Maven**

### Using SDKMAN!

SDKMAN! is a tool for managing parallel versions of multiple Software Development Kits on Unix based systems. It provides a convenient Command Line Interface (CLI) and API for installing, switching, removing and listing Candidates.

To install SDKMAN!, open a new terminal and enter:

```
curl -s "https://get.sdkman.io" | bash
```

Then, open a new terminal or enter:

```
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Now, you can install Java and Maven using SDKMAN!:

```
sdk install java
sdk install maven
```

### **Manually**
### Installing Java

Download and install Java 17 from the [official website](https://www.oracle.com/java/technologies/downloads/).

### Installing Maven

### For Windows and Linux:

Download and install Maven from the [official website](https://maven.apache.org/download.cgi).

### For macOS:

If you have Homebrew installed, you can install Maven by running:

```
brew install maven

```

If you do not have Homebrew installed, you can install it by running:

```
/bin/bash -c "$(curl -fsSL <https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh>)"
brew install maven

```

### Installing Docker

Follow the instructions on the [official website](https://docs.docker.com/get-docker/) to install Docker and Docker Compose.

### Installing Kubernetes
If you are using Docker Desktop, you can enable Kubernetes via the settings page.

## Running the Application

1. First, clone the project repository:

```
git clone https://gitlab.com/elizabethdrew/networth
```

2. Navigate to the project root directory.

3. Build the project with seed data (UPDATE NEEDED: HOW TO BUILD EACH SERVICE)

```
mvn spring-boot:build-image -Dmaven.test.skip=true 
```

4. You can then run the Docker compose configuration (UPDATE NEEDED - ADD HELM OPTION)

```
docker-compose up --build -d
```

---
## Kubernetes Discovery Server

To install Kubernetes Discovery Server into your K8 cluster please use the following command: 
kubectl apply -f kubernetes/kubernetes-discoveryserver.yml

---
## Kubernetes Keycloak Realm

To create a config map based on the realm-export.json file run the following command:
```
kubectl create configmap keycloak-realm --from-file=./helm/keycloak/realm/realm-export.json
```


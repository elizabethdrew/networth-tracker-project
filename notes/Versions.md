# Versions

## v1.3.0
**Date: 16/11/2023**

Gateway Server, Keycloak Server, and Monitoring tools added
+ The microservice endpoints are now externally available through the Gateway Server at port 8080.
+ Keycloak Server has been added to provide security to the application.
+ Grafana, Loki, Prometheus and Promtail have been added to provide better monitoring and observability functionality.


## v1.2.0
**Date: 14/11/2023**

Discovery Server Added
+ When loaded from Docker Compose, the microservices use Eureka as the Discovery server. This has been implemented using spring profiles.
+ When loaded in Kubernetes, the microservices use Kubernetes Discovery Server instead.

## v1.1.0
**Date: 13/11/2023**

Config Server has been added.
+ Usable via Docker and Helm
+ User Service configurations have been tidied

## v1.0.0
**Date: 09/11/2023**

A basic version of the User Service has been created with minimal CRUD endpoints.
+ MySQL and Liquibase setup and confirmed working.
+ Docker Compose setup and confirmed working.
+ Helm charts setup and confirmed working.
+ Testcontainers and Integration tests setup and confirmed working (although issue with data pollution when running consecutive tests persists.)

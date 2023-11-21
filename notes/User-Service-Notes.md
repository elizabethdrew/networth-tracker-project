# User Service Notes

## User Registration Flow

### Overview
The user service is responsible for creating new user accounts. It interacts with Keycloak to create the user within Keycloak at the same time.

### Keycloak Integration
This function uses Keycloak's admin API to register users. This involves obtaining an admin access token and creating a user in the Keycloak realm.

### WebClient
`WebClient` is a non-blocking, reactive client for performing HTTP requests with Spring WebFlux. It's used to communicate with Keycloak's admin API asynchronously.

#### Usage
To make a POST request with `WebClient`, you build a request with the desired URI, headers, and body. You then call `retrieve()` to execute the request and process the response reactively.

#### Error Handling
When the response indicates an error (4xx or 5xx status codes), we handle it by logging the error and throwing a `KeycloakException`.

#### Configuration
`WebClient` configurations, such as Keycloak's URI and credentials, are externalized in `application.yaml` for easier management and to support different environments.
## User Registration Flow

### Overview
The user service is tasked with the creation of new user accounts. It works in tandem with Keycloak to simultaneously create the user within Keycloak's system.

### Keycloak Integration
To register users, the service leverages the administrative API provided by Keycloak. This process requires an admin access token followed by the user's creation within Keycloak's realm.

### RestTemplate
`RestTemplate` is a synchronous client for performing HTTP requests and is used for communication with Keycloak's admin API in a synchronous manner.

#### Usage
A POST request is made using `RestTemplate` by setting up an `HttpEntity` with headers and the request body. The `exchange` method or any suitable method like `postForEntity` is then used to execute the request and obtain a response.

#### Error Handling
Errors, signified by 4xx or 5xx status codes, are handled synchronously. The service logs the error details and throws a `KeycloakException` if it encounters issues during the user registration process.

#### Configuration
The configurations for `RestTemplate`, including Keycloak's URI and credentials, are specified in the `application.yaml`. This ensures easy management and adaptability across different deployment environments.
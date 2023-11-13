# Development Steps For Service

1. Create the parent SpringBoot Application
2. Create the child SpringBoot Application
   + Remember to update the poms with relevant child / parent information
   + Create a simple output in the child app to make sure the application loads and runs
   + Create a Docker Image of the child app
     + mvn compile jib:dockerBuild
3. Set up a basic Helm to run the child app
   + kubectl port-forward service/test-service 8080:8080
4. Basic Service HTTP methods setup
5. Mysql Database Setup
6. Basic Security Config added
7. Setup TestContainers for Integration Tests
8. Add ConfigClient to POM
9. Add Configs to config server for service
10. Add Config Server url to service application.yml


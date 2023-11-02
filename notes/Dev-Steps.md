# Development Steps

1. Create the parent SpringBoot Application
2. Create the child SpringBoot Application
   + Remember to update the poms with relevant child / parent information
   + Create a simple output in the child app to make sure the application loads and runs
   + Create a Docker Image of the child app
     + mvn compile jib:dockerBuild
3. Set up a basic Helm to run the child app
   + kubectl port-forward service/test-service 8080:8080


- Get database set up
- Get database working with Helm
- Improve Helm configurations for more services
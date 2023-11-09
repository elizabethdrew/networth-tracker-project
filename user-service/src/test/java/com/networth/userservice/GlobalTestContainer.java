package com.networth.userservice;

import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = DockerMysqlDataSourceInitializer.class)
public abstract class GlobalTestContainer {

    @Autowired
    SpringLiquibase liquibase;

    @Autowired
    EntityManagerFactory emf;

    public static MySQLContainer<?> container = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("users_db")
            .withUsername("admin")
            .withPassword("password")
            .waitingFor(Wait.forListeningPort())
            .withEnv("MYSQL_ROOT_HOST", "%");


    @BeforeAll
    public static void setUp(){

        if(!container.isRunning()) {
            container.start();
        }
        RestAssured.baseURI = "http://localhost:8081";

    }

    @AfterAll
    public static void tearDown(){
        container.stop();
    }

}

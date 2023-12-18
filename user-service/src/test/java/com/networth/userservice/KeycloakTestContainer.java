package com.networth.userservice;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = DockerMysqlDataSourceInitializer.class)
public abstract class KeycloakTestContainer {

    public static KeycloakContainer keycloakContainer = new KeycloakContainer()
            .withRealmImportFile("keycloak/realm-export.json");

    @BeforeAll
    public static void setUp(){

        if (!keycloakContainer.isRunning()) {
            keycloakContainer.start();
        }
    }

    @AfterAll
    public static void tearDown(){
        keycloakContainer.stop();
    }

}

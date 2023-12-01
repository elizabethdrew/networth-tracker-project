package com.networth.userservice;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

public class DockerMysqlDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @LocalServerPort
    private int port;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + GlobalTestContainer.container.getJdbcUrl(),
                "spring.datasource.username=" + GlobalTestContainer.container.getUsername(),
                "spring.datasource.password=" + GlobalTestContainer.container.getPassword(),
                "server.port=8081",
                "spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.yml",
                "spring.liquibase.contexts=default,test",
                "keycloak.base-uri=http://localhost:" + GlobalTestContainer.keycloakContainer.getFirstMappedPort()
        );

        System.out.println("spring.datasource.url=" + GlobalTestContainer.container.getJdbcUrl());
        System.out.println("keycloak.base-uri=http://localhost:" + GlobalTestContainer.keycloakContainer.getFirstMappedPort());
    }
}
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
                "spring.datasource.url=" + MysqlTestContainer.container.getJdbcUrl(),
                "spring.datasource.username=" + MysqlTestContainer.container.getUsername(),
                "spring.datasource.password=" + MysqlTestContainer.container.getPassword(),
                "server.port=8081",
                "spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.yml",
                "spring.liquibase.contexts=default,test",
                "keycloak.base-uri=http://localhost:8081",
                "keycloak.logout-redirect-url=http://localhost:8080/contactSupport",
                "keycloak.keyAdmin.realm=master",
                "keycloak.keyAdmin.clientId=admin-cli",
                "keycloak.keyAdmin.username=admin",
                "keycloak.keyAdmin.password=password",
                "keycloak.keyUser.realm=networth",
                "keycloak.keyUser.clientId=apigateway",
                "keycloak.keyUser.clientSecret=Ow1dayvip5w4BHJeacRzVLHLCfJNCm3W"
        );

        System.out.println("spring.datasource.url=" + MysqlTestContainer.container.getJdbcUrl());
    }
}
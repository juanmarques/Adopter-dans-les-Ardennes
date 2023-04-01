package com.jme.adopterdla.adopterdla;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class AbstractContainerBaseTest {

    static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer =
                new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
                        .withDatabaseName("test")
                        .withUsername("duke")
                        .withPassword("s3cret")
                        .withReuse(true);

        postgreSQLContainer.start();
    }

    private static String r2dbcUrl() {
        return String.format("r2dbc:postgresql://%s:%s/%s", postgreSQLContainer.getHost(),
                postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgreSQLContainer.getDatabaseName());
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", AbstractContainerBaseTest::r2dbcUrl);
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
    }

}

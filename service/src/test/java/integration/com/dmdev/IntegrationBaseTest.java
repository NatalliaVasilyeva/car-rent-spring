package integration.com.dmdev;

import integration.com.dmdev.annotation.IT;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@IT
@TestPropertySource(locations = "/application-test.yaml")
@Sql(scripts = "/insert_data.sql")
public abstract class IntegrationBaseTest {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName("rentcar")
            .withUsername("postgres")
            .withPassword("pass")
            .withInitScript("init.sql");

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
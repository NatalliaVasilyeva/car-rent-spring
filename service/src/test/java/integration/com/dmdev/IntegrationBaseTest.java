package integration.com.dmdev;

import com.dmdev.configuration.BeansConfiguration;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

public abstract class IntegrationBaseTest {
    private final static String INSERT_DATA_PATH = "insert_data.sql";
    private final static String TRUNCATE_TABLES_PATH = "truncate_tables.sql";
    private static final String INSERT_SQL = loadSqlScript(INSERT_DATA_PATH);
    private static final String DELETE_SQL = loadSqlScript(TRUNCATE_TABLES_PATH);
    protected static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void setUp() {
        context = new AnnotationConfigApplicationContext(BeansConfiguration.class);
        Session session = context.getBean(Session.class);
        session.beginTransaction();
        session.createSQLQuery(INSERT_SQL).executeUpdate();
        session.getTransaction().commit();
    }

    @AfterAll
    @SneakyThrows
    static void tearDown() {
        Session session = context.getBean(Session.class);
        session.beginTransaction();
        session.createSQLQuery(DELETE_SQL).executeUpdate();
        session.getTransaction().commit();
    }

    @SneakyThrows
    private static String loadSqlScript(String filePath) {
        try (InputStream inputStream = IntegrationBaseTest.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
             Stream<String> result = bufferedReader.lines()) {
            return result.collect(joining());
        }
    }
}
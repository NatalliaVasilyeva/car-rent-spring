package integration.com.dmdev;

import com.dmdev.utils.HibernateSessionFactoryUtil;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public abstract class IntegrationBaseTest {
    private final String INSERT_DATA_PATH = "insert_data.sql";
    private final String TRUNCATE_TABLES_PATH = "truncate_tables.sql";
    private final String insert_sql = loadSqlScript(INSERT_DATA_PATH);
    private final String delete_sql = loadSqlScript(TRUNCATE_TABLES_PATH);
    protected static SessionFactory sessionFactory;

    @BeforeAll
    static void setUp() {
        sessionFactory = HibernateSessionFactoryUtil.buildSessionAnnotationFactory();
    }

    @BeforeEach
    void insertData() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(insert_sql).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @AfterEach
    void deleteDataFromAllTables() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(delete_sql).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @AfterAll
    @SneakyThrows
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private String loadSqlScript(String filePath) {
        InputStream inputStream = IntegrationBaseTest.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return bufferedReader.lines().collect(Collectors.joining());
    }
}
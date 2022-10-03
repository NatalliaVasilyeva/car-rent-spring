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

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

public abstract class IntegrationBaseTest {
    private final static String INSERT_DATA_PATH = "insert_data.sql";
    private final static String TRUNCATE_TABLES_PATH = "truncate_tables.sql";
    private final String insertSql = loadSqlScript(INSERT_DATA_PATH);
    private final String deleteSql = loadSqlScript(TRUNCATE_TABLES_PATH);
    protected static SessionFactory sessionFactory;

    @BeforeAll
    static void setUp() {
        sessionFactory = HibernateSessionFactoryUtil.buildSessionAnnotationFactory();
    }

    @BeforeEach
    void insertData() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(insertSql).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @AfterEach
    void deleteDataFromAllTables() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(deleteSql).executeUpdate();
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

    @SneakyThrows
    private String loadSqlScript(String filePath) {
        try (InputStream inputStream = IntegrationBaseTest.class.getClassLoader().getResourceAsStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {
            return bufferedReader.lines().collect(joining());
        }
    }
}
package integration.com.dmdev;

import com.dmdev.utils.HibernateSessionFactoryUtil;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

public abstract class IntegrationBaseTest {
    private final static String INSERT_DATA_PATH = "insert_data.sql";
    private final static String TRUNCATE_TABLES_PATH = "truncate_tables.sql";
    private static final String INSERT_SQL = loadSqlScript(INSERT_DATA_PATH);
    private static  final String DELETE_SQL = loadSqlScript(TRUNCATE_TABLES_PATH);
    protected static SessionFactory sessionFactory;

    @BeforeAll
    static void setUp() {

        sessionFactory = HibernateSessionFactoryUtil.buildSessionAnnotationFactory();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(INSERT_SQL).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @AfterAll
    @SneakyThrows
    static void tearDown() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(DELETE_SQL).executeUpdate();
            session.getTransaction().commit();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @SneakyThrows
    private static String loadSqlScript(String filePath) {
        try {
            InputStream inputStream = IntegrationBaseTest.class.getClassLoader().getResourceAsStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
            return bufferedReader.lines().collect(joining());
        } catch (Exception exception) {
            throw new IOException("Exception have appeared while read data from file");
        }
    }
}
package integration.com.dmdev;

import com.dmdev.utils.HibernateSessionFactoryUtil;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;

public abstract class IntegrationBaseTest {

    protected static SessionFactory sessionFactory;
    private static List tableNames;
    private static final String DELETE_ALL_SQL = "delete from %s";

    private static final String INSERT_SQL = "" +
            "   INSERT INTO car_rent.users (login, email, password, role) VALUES ('Admin', 'admin@gmail.com', 'VasilechekBel123!', 'ADMIN');\n" +
            "   INSERT INTO car_rent.userdetails (user_id, name, surname, address, phone, birthday, registration_date) VALUES ('1', 'Ivan', 'Ivanov', 'Minsk', '+375 29 124 56 78', '1986-07-02 00:00:00', '2022-09-22 20:30:00');\n" +
            "   INSERT INTO car_rent.driverlicense (user_details_id, number, issue_date, expired_date) VALUES ('1', 'AB12345', '2015-03-02 00:00:00', '2025-03-01 00:00:00');\n" +
            "   INSERT INTO car_rent.price (price) VALUES (50);\n" +
            "   INSERT INTO car_rent.categories (name, price_id) VALUES ('ECONOMY', '1');\n" +
            "   INSERT INTO car_rent.brand (name) VALUES ('audi');\n" +
            "   INSERT INTO car_rent.model (brand_id, category_id, name, transmission, engine_type) VALUES ('1', '1', 'A8', 'MANUAL', 'FUEL');\n" +
            "   INSERT INTO car_rent.car (model_id, color, year, car_number, vin, is_repaired) VALUES ('1', 'white', '2020', '7865AE-7', 'AmhBHqJ8BgD0p3PRgkoi', 'false');\n" +
            "   INSERT INTO car_rent.orders (date, user_id, car_id, passport, insurance, order_status, sum) VALUES ('2022-07-01 00:00:00', '1', '1', 'MP1234567', 'true', 'CONFIRMATION_WAIT', 1020);\n" +
            "   INSERT INTO car_rent.carrentaltime (order_id, start_rental_date, end_rental_date) VALUES ('1', '2022-07-02 00:00:00', '2022-07-03 23:59:00');\n" +
            "   INSERT INTO car_rent.accident (order_id, accident_date, description, damage) VALUES ('1', '2022-07-02 16:34:00', 'faced tree', 75.50);";

    @BeforeAll
    @SneakyThrows
    static void setUp() {
        sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            tableNames = Arrays.asList(session.getMetamodel().getEntities().stream().map(table -> table.getName().toLowerCase()).toArray());
        }
    }

    @BeforeEach
    void insertData() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(INSERT_SQL).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @AfterEach
    void deleteDataFromAllTables() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (Object tableName : tableNames) {
                session.createSQLQuery(String.format(DELETE_ALL_SQL, tableName));
            }
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
}
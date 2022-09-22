package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Accident;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class AccidentTestIT extends IntegrationBaseTest {

    @Test
    public void createAccident() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Accident accident = Accident.builder()
                    .orderId(1L)
                    .accidentDate(LocalDate.of(2022, 9, 22))
                    .description("Accident with car")
                    .damage(BigDecimal.valueOf(25.98))
                    .build();

            session.save(accident);
            session.getTransaction().commit();
            Accident actualAccident= session.get(Accident.class, 1L);
            assertThat(actualAccident.getId()).isNotNull();

        }
    }
}